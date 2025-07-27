package com.karthik.projects.airBnbApp.services;

import com.karthik.projects.airBnbApp.dtos.HotelDTO;
import com.karthik.projects.airBnbApp.dtos.HotelInfoDTO;
import com.karthik.projects.airBnbApp.dtos.RoomDTO;
import com.karthik.projects.airBnbApp.entities.Hotel;
import com.karthik.projects.airBnbApp.entities.Room;
import com.karthik.projects.airBnbApp.entities.User;
import com.karthik.projects.airBnbApp.exceptions.ResourceNotFoundException;
import com.karthik.projects.airBnbApp.exceptions.UnAuthorisedException;
import com.karthik.projects.airBnbApp.repositories.HotelRepository;
import com.karthik.projects.airBnbApp.repositories.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;//constructor injection
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final RoomRepository roomRepository;

    @Override
    public HotelDTO createNewHotel(HotelDTO hotelDTO) {
        log.info("Creating a new hotel with name: {}",hotelDTO.getName());
        Hotel hotel=modelMapper.map(hotelDTO,Hotel.class);
        hotel.setActive(false);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotel.setOwner(user);

        hotel = hotelRepository.save(hotel);
        log.info("Created a new hotel with ID: {}",hotel.getId());
        return modelMapper.map(hotel,HotelDTO.class);
    }

    @Override
    public HotelDTO getHotelById(Long id) {
        log.info("Getting  the  hotel with Id: {}",id);
        Hotel hotel =hotelRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with id: "+id));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id: "+id);
        }

        return modelMapper.map(hotel,HotelDTO.class);
    }

    @Override
    public HotelDTO updateHotelById(Long id, HotelDTO hotelDTO) {
        log.info("Updating the hotel with Id: {}",id);
        Hotel hotel =hotelRepository.
                findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with id: "+id));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id: "+id);
        }

        modelMapper.map(hotelDTO,hotel);
        hotel.setId(id);
        hotel = hotelRepository.save(hotel);
        return modelMapper.map(hotel,HotelDTO.class);
    }

    @Override
    @Transactional //used where we make more  than one database call
    public void deleteHotelById(Long id) {
        Hotel hotel =hotelRepository.
                findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with id: "+id));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id: "+id);
        }



        // delete the future inventories for this hotel

        for(Room room : hotel.getRooms()){
            inventoryService.deleteFutureInventories(room);

            //delete the corresponding room

            roomRepository.deleteById(room.getId());
        }

        hotelRepository.deleteById(id);

    }

    @Override
    @Transactional
    public void activateHotelById(Long id) {
        log.info("Activating the hotel with Id: {}",id);
        Hotel hotel =hotelRepository.
                findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with id: "+id));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id: "+id);
        }

        hotel.setActive(true);

        // assuming only do it once
        for(Room room : hotel.getRooms()){
            inventoryService.initializeRoomForAYear(room);
        }
    }

    //public method
    @Override
    public HotelInfoDTO getHotelInfoById(Long hotelId) {
        Hotel hotel =hotelRepository.
                findById(hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with id: "+hotelId));


        List<RoomDTO> rooms = hotel.getRooms()
                .stream()
                .map(room -> modelMapper.map(room,RoomDTO.class))
                .collect(Collectors.toList());
        return  new HotelInfoDTO(modelMapper.map(hotel,HotelDTO.class),rooms);
    }
}
