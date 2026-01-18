package com.project.HotelManagementSystem.config;

import com.project.HotelManagementSystem.dto.admin.AdminCreateDTO;
import com.project.HotelManagementSystem.dto.admin.AdminUpdateDTO;
import com.project.HotelManagementSystem.dto.editor.EditorCreateDTO;
import com.project.HotelManagementSystem.dto.editor.EditorUpdateDTO;
import com.project.HotelManagementSystem.dto.hotel.HotelCreateDTO;
import com.project.HotelManagementSystem.dto.hotel.HotelDTO;
import com.project.HotelManagementSystem.dto.hotel.HotelUpdateDTO;
import com.project.HotelManagementSystem.dto.room.RoomDTO;
import com.project.HotelManagementSystem.dto.room.RoomUpdateDTO;
import com.project.HotelManagementSystem.entity.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

@Configuration
public class  AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        configureRoomMappings(mapper);
        configureRoomToUpdateDtoConverter(mapper);

        configureHotelDtoToEntityMappings(mapper);
        configureHotelToUpdateDtoConverter(mapper);

        configureAdminMappings(mapper);
        configureEditorMappings(mapper);

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
                dto.setAvailable(room.isAvailable());
                dto.setDescription(room.getDescription());
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


    private void configureAdminMappings(ModelMapper mapper) {

        TypeMap<AdminCreateDTO, Admin> createMap = mapper.createTypeMap(AdminCreateDTO.class, Admin.class);
        createMap.setPostConverter(ctx -> {
            return ctx.getDestination();
        });

        TypeMap<AdminUpdateDTO, Admin> updateMap = mapper.createTypeMap(AdminUpdateDTO.class, Admin.class);
        updateMap.setPostConverter(ctx -> {
            return ctx.getDestination();
        });

        Converter<Admin, AdminCreateDTO> toCreateDto = ctx -> {
            Admin admin = ctx.getSource();
            AdminCreateDTO dto = new AdminCreateDTO();
            dto.setId(admin.getId());
            dto.setName(admin.getName());
            dto.setPhone(admin.getPhone());
            dto.setDateOfBirth(admin.getDateOfBirth());
            dto.setNationality(admin.getNationality());
            dto.setPassportNumber(admin.getPassportNumber());
            dto.setNationalIdNumber(admin.getNationalIdNumber());
            dto.setAdminType(admin.getAdminType());
            if (admin.getUser() != null) {
                dto.setUser(admin.getUser().getId());
            }
            return dto;
        };
        mapper.addConverter(toCreateDto);

        Converter<Admin, AdminUpdateDTO> toUpdateDto = ctx -> {
            Admin admin = ctx.getSource();
            AdminUpdateDTO dto = new AdminUpdateDTO();
            dto.setId(admin.getId());
            dto.setName(admin.getName());
            dto.setPhone(admin.getPhone());
            dto.setDateOfBirth(admin.getDateOfBirth());
            dto.setNationality(admin.getNationality());
            dto.setPassportNumber(admin.getPassportNumber());
            dto.setNationalIdNumber(admin.getNationalIdNumber());
            dto.setAdminType(admin.getAdminType());
            if (admin.getUser() != null) {
                dto.setUser(admin.getUser().getId());
            }
            return dto;
        };
        mapper.addConverter(toUpdateDto);
    }

    private void configureEditorMappings(ModelMapper mapper) {

        TypeMap<EditorCreateDTO, Editor> createMap = mapper.createTypeMap(EditorCreateDTO.class, Editor.class);
        createMap.setPostConverter(ctx -> {
            return ctx.getDestination();
        });

        TypeMap<EditorUpdateDTO, Editor> updateMap = mapper.createTypeMap(EditorUpdateDTO.class, Editor.class);
        updateMap.setPostConverter(ctx -> {
            return ctx.getDestination();
        });

        Converter<Editor, EditorCreateDTO> toCreateDto = ctx -> {
            Editor editor = ctx.getSource();
            EditorCreateDTO dto = new EditorCreateDTO();
            dto.setId(editor.getId());
            dto.setName(editor.getName());
            dto.setPhone(editor.getPhone());
            dto.setDateOfBirth(editor.getDateOfBirth());
            dto.setNationality(editor.getNationality());
            dto.setPassportNumber(editor.getPassportNumber());
            dto.setNationalIdNumber(editor.getNationalIdNumber());
            dto.setEditorType(editor.getEditorType());

            if (editor.getUser() != null) {
                dto.setUser(editor.getUser().getId());
            }
            return dto;
        };
        mapper.addConverter(toCreateDto);

        Converter<Editor, EditorUpdateDTO> toUpdateDto = ctx -> {
            Editor editor = ctx.getSource();
            EditorUpdateDTO dto = new EditorUpdateDTO();
            dto.setId(editor.getId());
            dto.setName(editor.getName());
            dto.setPhone(editor.getPhone());
            dto.setDateOfBirth(editor.getDateOfBirth());
            dto.setNationality(editor.getNationality());
            dto.setPassportNumber(editor.getPassportNumber());
            dto.setNationalIdNumber(editor.getNationalIdNumber());
            dto.setEditorType(editor.getEditorType());

            if (editor.getUser() != null) {
                dto.setUser(editor.getUser().getId());
            }
            return dto;
        };
        mapper.addConverter(toUpdateDto);
    }

}
