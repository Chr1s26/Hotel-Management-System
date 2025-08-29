package com.project.HotelManagementSystem.entity.constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EditorType implements BaseEnum<Integer>{
    ImageEditor(1),
    RoomEditor(2),
    HotelEditor(3);

    private final int value;

    @Override
    public Integer getValue() {
        return this.value;
    }
}
