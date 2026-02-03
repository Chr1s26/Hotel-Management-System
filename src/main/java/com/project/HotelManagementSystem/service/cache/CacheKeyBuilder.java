package com.project.HotelManagementSystem.service.cache;

import com.project.HotelManagementSystem.dto.booking.HotelSearchDTO;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CacheKeyBuilder {

    public static String build(HotelSearchDTO dto) {
        return String.join(":",
                "HOTEL_SEARCH",
                safe(dto.getKeyword()),
                safe(dto.getHotelId()),
                safe(dto.getCityId()),
                safe(dto.getRegionId()),
                safe(dto.getCountryId()),
                safe(dto.getCheckIn()),
                safe(dto.getCheckOut()),
                safe(dto.getNumberOfGuests()),
                safe(dto.getHotelTypes()),
                safe(dto.getMinPrice()),
                safe(dto.getMaxPrice()),
                safe(dto.getMinRating()),
                safe(dto.getRoomTypeNames()),
                safe(dto.getAmenities())
        );
    }

    private static String safe(Object o) {
        if (o == null) return "_";
        if (o instanceof Iterable<?> it) {
            return String.join(",",
                    ((Iterable<?>) it)
                            .iterator()
                            .hasNext()
                            ? ((Iterable<?>) it).toString()
                            : "_");
        }
        return Objects.toString(o);
    }

    // in the future, should use below method for amenities and room type Names
    // because they are list and might become different order so we should sort before cache
    private static String safeCollection(Iterable<?> iterable) {
        if (iterable == null) return "_";
        return StreamSupport.stream(iterable.spliterator(), false)
                .map(Object::toString)
                .sorted()
                .collect(Collectors.joining(","));
    }
}
