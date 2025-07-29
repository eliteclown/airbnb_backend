package com.karthik.projects.airBnbApp.services;

import com.karthik.projects.airBnbApp.Strategy.PricingService;
import com.karthik.projects.airBnbApp.dtos.BookingDTO;
import com.karthik.projects.airBnbApp.dtos.BookingRequestDTO;
import com.karthik.projects.airBnbApp.dtos.GuestDTO;
import com.karthik.projects.airBnbApp.entities.*;
import com.karthik.projects.airBnbApp.entities.enums.BookingStatus;
import com.karthik.projects.airBnbApp.exceptions.ResourceNotFoundException;

import com.karthik.projects.airBnbApp.exceptions.UnAuthorisedException;
import com.karthik.projects.airBnbApp.repositories.*;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final GuestRepository guestRepository;
    private final InventoryRepository inventoryRepository;
    private final CheckoutService checkoutService;
    private final PricingService pricingService;
    private  final ModelMapper modelMapper ;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    @Transactional
    public BookingDTO initialiseBooking(BookingRequestDTO bookingRequestDTO) {

        log.info("Initialising booking for hotel : {}, room: {}, date {}-{} ",bookingRequestDTO.getHotelId(),bookingRequestDTO.getRoomId(),bookingRequestDTO.getCheckInDate(),bookingRequestDTO.getCheckOutDate());

        Hotel hotel = hotelRepository.findById(bookingRequestDTO.getHotelId()).orElseThrow(()->
                new ResourceNotFoundException("Hotel not found with id  "+bookingRequestDTO.getHotelId()));

        Room room = roomRepository.findById(bookingRequestDTO.getRoomId()).orElseThrow(()->
                new ResourceNotFoundException("Room not found with id "+bookingRequestDTO.getRoomId()));

        List<Inventory> inventoryList = inventoryRepository.findAndLockAvailableInventory(bookingRequestDTO.getRoomId(),bookingRequestDTO.getCheckInDate(),bookingRequestDTO.getCheckOutDate(),bookingRequestDTO.getRoomsCount());


        // Reserve the room/ update the booked count  of inventories

        for(Inventory inventory : inventoryList){
            inventory.setReservedCount(inventory.getReservedCount() + bookingRequestDTO.getRoomsCount());
        }

        inventoryRepository.saveAll(inventoryList);

        // create the booking


        // TODO: calculate dynamic amount

        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingRequestDTO.getCheckInDate())
                .checkOutDate(bookingRequestDTO.getCheckOutDate())
                .userEntity(getCurrentUser())
                .roomsCount(bookingRequestDTO.getRoomsCount())
                .amount(BigDecimal.TEN)
                .build();

        bookingRepository.save(booking);
        return modelMapper.map(booking, BookingDTO.class);

    }

    @Override
    @Transactional
    public BookingDTO addGuests(Long bookingId, List<GuestDTO> guestDTOList) {

        log.info("Adding guests for booking id : {} ",bookingId);

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()->
                new ResourceNotFoundException("Booking not found with id  "+bookingId));
        User user = getCurrentUser();

        if(!user.equals(booking.getUserEntity())){
            throw new UnAuthorisedException("Booking does not belong to this user with id: "+user.getId());
        }

        if(hasBookingExpired(booking)){
            throw  new IllegalStateException("Booking has already expired");
        }

        if(booking.getBookingStatus() != BookingStatus.RESERVED){
            throw  new IllegalStateException("Booking is not under reserved state, cannot add guests");
        }

        for(GuestDTO guestDTO : guestDTOList){
            Guest guest = modelMapper.map(guestDTO,Guest.class);
            guest.setUserEntity(getCurrentUser());
            guestRepository.save(guest);
            booking.getGuests().add(guest);
        }

        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
        booking= bookingRepository.save(booking);


        return modelMapper.map(booking, BookingDTO.class);
    }

    @Override
    @Transactional
    public String initiatePayments(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()-> new ResourceNotFoundException("Booking not found with id:  "+bookingId));
        User user = getCurrentUser();
        if(!user.equals(booking.getUserEntity())){
            throw new UnAuthorisedException("Booking does not belong to this user with id: "+user.getId());
        }

        if(hasBookingExpired(booking)){
            throw  new IllegalStateException("Booking has already expired");
        }

        String sessionUrl = checkoutService.getCheckoutSession(booking,
                frontendUrl+"/payments/success",frontendUrl+"/payments/failure");

        booking.setBookingStatus(BookingStatus.PAYMENTS_PENDING);
        bookingRepository.save(booking);

        return sessionUrl;
    }

    @Override
    @Transactional
    public void capturePayments(Event event) {
        if("checkout.session.completed".equals(event.getType())){
//           Session session = (Session) event.getDataObjectDeserializer().getObject();
        }
    }

    @Override
    public void cancelBooking(Long bookingId) {

    }

    @Override
    public String getBookingStatus(Long bookingId) {
        return "";
    }

    public boolean hasBookingExpired(Booking booking){
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }

    public User getCurrentUser(){
//       User user = new User();
//       user.setId(1L);
//       return user;

        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
