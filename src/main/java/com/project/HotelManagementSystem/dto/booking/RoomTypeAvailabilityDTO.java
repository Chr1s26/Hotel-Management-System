package com.project.HotelManagementSystem.dto.booking;

public class RoomTypeAvailabilityDTO {
    private Long roomTypeId;
    private String name;
    private int roomSize;
    private int capacity;
    private double price;
    private long availableRooms;

    public RoomTypeAvailabilityDTO(Long roomTypeId, String name, int capacity, int roomSize, double price, long availableRooms) {
        this.roomTypeId = roomTypeId;
        this.name = name;
        this.capacity = capacity;
        this.roomSize = roomSize;
        this.price = price;
        this.availableRooms = availableRooms;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public int getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(int roomSize) {
        this.roomSize = roomSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(long availableRooms) {
        this.availableRooms = availableRooms;
    }
}
