package com.project.HotelManagementSystem.converter;

import com.project.HotelManagementSystem.entity.constants.BookingStatus;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BookingStatusConverter extends BaseEnumConverter<BookingStatus,Integer>{
    public BookingStatusConverter(){
        super(BookingStatus.class);
    }
}
