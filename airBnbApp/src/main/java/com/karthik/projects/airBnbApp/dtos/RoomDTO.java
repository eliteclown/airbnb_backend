package com.karthik.projects.airBnbApp.dtos;

import com.karthik.projects.airBnbApp.entities.Hotel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private Long id;
    private String type;
    private BigDecimal basePrice;
    private List<String> photos;
    private List<String> amenities;
    private Integer totalCount;
    private Integer capacity;
}
