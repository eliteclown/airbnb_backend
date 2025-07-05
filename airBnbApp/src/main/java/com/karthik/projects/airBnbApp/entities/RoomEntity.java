package com.karthik.projects.airBnbApp.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rooms")
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne // it owns the relatonship
    @JoinColumn(name="hotel_id",nullable=false)
    private HotelEntity hotelEntity;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false,precision =  10,scale=2)
    private BigDecimal basePrice;

    @Column(columnDefinition = "TEXT[]")
    private List<String> photos;

    @Column(columnDefinition = "TEXT[]")
    private List<String> amenties;

    @Column(nullable = false)
    private Integer totalCount;

    @Column(nullable = false)
    private Integer capacity;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime creationAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
