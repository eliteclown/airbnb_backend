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
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne // it owns the relatonship
    @JoinColumn(name="hotel_id",nullable=false)
    private Hotel hotelEntity;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false,precision =  10,scale=2)
    private BigDecimal basePrice;

//    @Column(columnDefinition = "TEXT[]")
//    private List<String> photos;
    @ElementCollection
    @CollectionTable(name = "room_photos", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "photo")
    private List<String> photos;

//    @Column(columnDefinition = "TEXT[]")
//    private List<String> amenties;
    @ElementCollection
    @CollectionTable(name = "room_amenities", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "amenities")
    private List<String> amenities;

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
