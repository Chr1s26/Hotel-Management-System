package com.project.HotelManagementSystem.converter;

import com.project.HotelManagementSystem.entity.constants.BaseEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public abstract class BaseEnumConverter<E extends Enum<E> & BaseEnum<V>,V> implements AttributeConverter<E,V>{

    public final Class<E> enumClass;

    public BaseEnumConverter(Class<E> enumClass){
        this.enumClass = enumClass;
    }

    @Override
    public V convertToDatabaseColumn(E attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public E convertToEntityAttribute(V dbData) {
        return dbData != null ? BaseEnum.fromValue(enumClass, dbData) : null;
    }
}
