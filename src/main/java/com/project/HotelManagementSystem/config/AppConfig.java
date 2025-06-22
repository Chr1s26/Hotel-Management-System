package com.project.HotelManagementSystem.config;

import com.project.HotelManagementSystem.dto.hotel.HotelCreateDTO;
import com.project.HotelManagementSystem.dto.hotel.HotelDTO;
import com.project.HotelManagementSystem.dto.hotel.HotelUpdateDTO;
import com.project.HotelManagementSystem.dto.room.RoomDTO;
import com.project.HotelManagementSystem.dto.room.RoomUpdateDTO;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.Policy;
import com.project.HotelManagementSystem.entity.Promotion;
import com.project.HotelManagementSystem.entity.Room;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        configureRoomMappings(mapper);
        configureRoomToUpdateDtoConverter(mapper);

        configureHotelDtoToEntityMappings(mapper);
        configureHotelToUpdateDtoConverter(mapper);

        return mapper;
    }

    private void configureRoomMappings(ModelMapper mapper) {
        mapper.typeMap(RoomDTO.class, Room.class).addMappings(m -> {
            m.skip(Room::setAmenities);
            m.skip(Room::setPromotions);
        });
        mapper.typeMap(RoomUpdateDTO.class, Room.class).addMappings(m -> {
            m.skip(Room::setAmenities);
            m.skip(Room::setPromotions);
        });
    }

    private void configureHotelDtoToEntityMappings(ModelMapper mapper) {
        mapper.typeMap(HotelDTO.class, Hotel.class).addMappings(m -> {
            m.skip(Hotel::setPolicies);
            m.skip(Hotel::setPromotions);
        });

        mapper.typeMap(HotelCreateDTO.class, Hotel.class).addMappings(m -> {
            m.skip(Hotel::setPolicies);
            m.skip(Hotel::setPromotions);
        });

        mapper.typeMap(HotelUpdateDTO.class, Hotel.class).addMappings(m -> {
            m.skip(Hotel::setPolicies);
            m.skip(Hotel::setPromotions);
        });
    }

    private void configureHotelToUpdateDtoConverter(ModelMapper mapper) {
        Converter<Hotel, HotelUpdateDTO> hotelToUpdateDtoConverter = new Converter<Hotel, HotelUpdateDTO>() {
            @Override
            public HotelUpdateDTO convert(MappingContext<Hotel, HotelUpdateDTO> context) {
                Hotel hotel = context.getSource();
                HotelUpdateDTO dto = new HotelUpdateDTO();

                dto.setId(hotel.getId());
                dto.setName(hotel.getName());
                dto.setPhoneNumber(hotel.getPhoneNumber());
                dto.setEmail(hotel.getEmail());
                dto.setDescription(hotel.getDescription());
                dto.setRating(hotel.getRating());
                dto.setHotelType(hotel.getHotelType());
                dto.setAddress(hotel.getAddress());
                dto.setPropertyDescription(hotel.getPropertyDescription());

                dto.setPromotionIds(hotel.getPromotions()
                        .stream().map(Promotion::getId).collect(Collectors.toSet()));
                dto.setPolicyIds(hotel.getPolicies()
                        .stream().map(Policy::getId).collect(Collectors.toSet()));

                return dto;
            }
        };

        mapper.addConverter(hotelToUpdateDtoConverter);
    }

    private void configureRoomToUpdateDtoConverter(ModelMapper mapper) {
        Converter<Room, RoomUpdateDTO> roomToUpdateDtoConverter = new Converter<Room, RoomUpdateDTO>() {
            @Override
            public RoomUpdateDTO convert(MappingContext<Room, RoomUpdateDTO> context) {
                Room room = context.getSource();
                RoomUpdateDTO dto = new RoomUpdateDTO();

                dto.setId(room.getId());
                dto.setPrice(room.getPrice());
                dto.setAvailable(room.isAvailable());
                dto.setDescription(room.getDescription());
                dto.setRoomType(room.getRoomType().toString()); // assuming RoomType is an enum
                dto.setMaxCapacity(room.getMaxCapacity());
                dto.setHotel(room.getHotel());

                dto.setAmenityIds(room.getAmenities()
                        .stream().map(a -> a.getId()).collect(Collectors.toSet()));

                dto.setPromotionIds(room.getPromotions()
                        .stream().map(p -> p.getId()).collect(Collectors.toSet()));

                return dto;
            }
        };

        mapper.addConverter(roomToUpdateDtoConverter);
    }

}
