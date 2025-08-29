package com.project.HotelManagementSystem.converter;

import com.project.HotelManagementSystem.entity.constants.EditorType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EditorTypeConverter extends BaseEnumConverter<EditorType,Integer> {
    public EditorTypeConverter() {
        super(EditorType.class);
    }
}
