package com.project.HotelManagementSystem.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    public Long id;
    public String customerName;
    public String comment;
    public double rating;
    public int likeCount;
    public boolean likedByMe;
    public boolean mine;
}
