package com.project.HotelManagementSystem.entity.specification;

import com.project.HotelManagementSystem.dto.searchFilter.room.RoomSearchFilter;
import com.project.HotelManagementSystem.entity.Room;
import com.project.HotelManagementSystem.entity.constants.RoomType;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import org.springframework.data.jpa.domain.Specification;

public class RoomSpecification {

    public static Specification<Room> fromFilter(RoomSearchFilter f) {
        if (f == null || f.getField() == null) return null;

        return switch (f.getField()) {
            case PRICE -> priceSpec(f);
            case IS_AVAILABLE -> availabilitySpec(f);
            case STATUS -> statusSpec(f);
            case ROOM_TYPE -> roomTypeSpec(f);
            case MAX_CAPACITY -> maxCapacitySpec(f);
        };
    }

    private static Specification<Room> priceSpec(RoomSearchFilter f) {
        return (root, q, cb) -> {
            if (f.getValue() == null || f.getValue().isBlank()) return null;
            try {
                double price = Double.parseDouble(f.getValue());
                return cb.equal(root.get("price"), price);
            } catch (NumberFormatException e) {
                return null;
            }
        };
    }

    private static Specification<Room> availabilitySpec(RoomSearchFilter f) {
        return (root, query, cb) -> {
            if (f.getValue() == null || f.getValue().isBlank()) {
                return null;
            }
            boolean available = "true".equalsIgnoreCase(f.getValue());
            return cb.equal(root.get("isAvailable"), available);
        };
    }


    private static Specification<Room> statusSpec(RoomSearchFilter f) {
        return (root, q, cb) -> {
            if (f.getValue() == null || f.getValue().isBlank()) return null;
            try {
                StatusType status = StatusType.valueOf(f.getValue().toUpperCase());
                return cb.equal(root.get("status"), status);
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }

    private static Specification<Room> roomTypeSpec(RoomSearchFilter f) {
        return (root, q, cb) -> {
            if (f.getValue() == null || f.getValue().isBlank()) return null;
            try {
                RoomType type = RoomType.valueOf(f.getValue().toUpperCase());
                return cb.equal(root.get("roomType"), type);
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }

    private static Specification<Room> maxCapacitySpec(RoomSearchFilter f) {
        return (root, q, cb) -> {
            if (f.getValue() == null || f.getValue().isBlank()) return null;
            try {
                int capacity = Integer.parseInt(f.getValue());
                return cb.equal(root.get("maxCapacity"), capacity);
            } catch (NumberFormatException e) {
                return null;
            }
        };
    }
}
