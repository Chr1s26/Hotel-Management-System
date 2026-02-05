package com.project.HotelManagementSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "review_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"review_id", "customer_id"})
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewLike extends MasterData {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
