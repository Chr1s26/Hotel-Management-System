package com.project.HotelManagementSystem.config;

import com.project.HotelManagementSystem.dto.admin.AdminCreateDTO;
import com.project.HotelManagementSystem.dto.admin.AdminDTO;
import com.project.HotelManagementSystem.dto.admin.AdminUpdateDTO;
import com.project.HotelManagementSystem.dto.editor.EditorCreateDTO;
import com.project.HotelManagementSystem.dto.editor.EditorDTO;
import com.project.HotelManagementSystem.dto.editor.EditorUpdateDTO;
import com.project.HotelManagementSystem.dto.hotel.HotelCreateDTO;
import com.project.HotelManagementSystem.dto.hotel.HotelDTO;
import com.project.HotelManagementSystem.dto.hotel.HotelUpdateDTO;
import com.project.HotelManagementSystem.dto.room.RoomCreateDTO;
import com.project.HotelManagementSystem.dto.room.RoomDTO;
import com.project.HotelManagementSystem.dto.room.RoomUpdateDTO;
import com.project.HotelManagementSystem.dto.user.UserCreateDTO;
import com.project.HotelManagementSystem.dto.user.UserUpdateDTO;
import com.project.HotelManagementSystem.entity.*;
import com.project.HotelManagementSystem.entity.constants.RoomType;
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
        configureHotelMappings(mapper);

        configureAdminMappings(mapper);
        configureEditorMappings(mapper);
        configureUserMappings(mapper);

        return mapper;
    }

    private void configureRoomMappings(ModelMapper mapper) {

        mapper.typeMap(RoomCreateDTO.class, Room.class)
                .addMappings(m -> {
                    m.skip(Room::setAmenities);
                    m.skip(Room::setPromotions);
                    m.skip(Room::setRoomAttachments);
                    m.skip(Room::setId);
                    m.map(src -> RoomType.valueOf(src.getRoomType()), Room::setRoomType);
                });

        mapper.typeMap(RoomUpdateDTO.class, Room.class)
                .addMappings(m -> {
                    m.skip(Room::setAmenities);
                    m.skip(Room::setPromotions);
                    m.skip(Room::setRoomAttachments);
                    m.map(src -> RoomType.valueOf(src.getRoomType()), Room::setRoomType);
                });

        mapper.typeMap(Room.class, RoomUpdateDTO.class)
                .addMappings(m -> {
                    m.map(src -> src.getRoomType().toString(), RoomUpdateDTO::setRoomType);
                    m.map(src -> src.getHotel(), RoomUpdateDTO::setHotel);
                    m.map(src -> src.getAmenities()
                                    .stream().map(a -> a.getId()).collect(Collectors.toSet()),
                            RoomUpdateDTO::setAmenityIds);
                    m.map(src -> src.getPromotions()
                                    .stream().map(p -> p.getId()).collect(Collectors.toSet()),
                            RoomUpdateDTO::setPromotionIds);
                });
    }

    private void configureHotelMappings(ModelMapper mapper) {

        mapper.typeMap(HotelCreateDTO.class, Hotel.class)
                .addMappings(m -> {
                    m.skip(Hotel::setPolicies);
                    m.skip(Hotel::setPromotions);
                    m.skip(Hotel::setHotelAttachments);
                    m.skip(Hotel::setRoomAttachments);
                    m.skip(Hotel::setReviews);
                    m.skip(Hotel::setBookings);
                    m.skip(Hotel::setInvoices);
                    m.skip(Hotel::setEditors);
                    m.skip(Hotel::setRooms);
                });

        mapper.typeMap(HotelUpdateDTO.class, Hotel.class)
                .addMappings(m -> {
                    m.skip(Hotel::setPolicies);
                    m.skip(Hotel::setPromotions);
                    m.skip(Hotel::setHotelAttachments);
                    m.skip(Hotel::setRoomAttachments);
                    m.skip(Hotel::setReviews);
                    m.skip(Hotel::setBookings);
                    m.skip(Hotel::setInvoices);
                    m.skip(Hotel::setEditors);
                    m.skip(Hotel::setRooms);
                });

        mapper.typeMap(Hotel.class, HotelDTO.class)
                .addMappings(m -> {
                    m.skip(HotelDTO::setProfileUrl);
                });
    }

    private void configureAdminMappings(ModelMapper mapper) {

        mapper.typeMap(Admin.class, AdminCreateDTO.class)
                .addMapping(src -> src.getUser().getId(), AdminCreateDTO::setUser);

        mapper.typeMap(Admin.class, AdminUpdateDTO.class)
                .addMapping(src -> src.getUser().getId(), AdminUpdateDTO::setUser);

        mapper.typeMap(Admin.class, AdminDTO.class)
                .addMapping(src -> src.getUser().getId(), AdminDTO::setUser);

        mapper.typeMap(AdminCreateDTO.class, Admin.class)
                .addMappings(m -> m.skip(Admin::setUser));

        mapper.typeMap(AdminUpdateDTO.class, Admin.class)
                .addMappings(m -> m.skip(Admin::setUser));
    }

    private void configureEditorMappings(ModelMapper mapper) {

        mapper.typeMap(Editor.class, EditorCreateDTO.class)
                .addMapping(src -> src.getUser().getId(), EditorCreateDTO::setUser);

        mapper.typeMap(Editor.class, EditorUpdateDTO.class)
                .addMapping(src -> src.getUser().getId(), EditorUpdateDTO::setUser);

        mapper.typeMap(Editor.class, EditorDTO.class)
                .addMapping(src -> src.getUser().getId(), EditorDTO::setUser);

        mapper.typeMap(EditorCreateDTO.class, Editor.class)
                .addMappings(m -> m.skip(Editor::setUser));

        mapper.typeMap(EditorUpdateDTO.class, Editor.class)
                .addMappings(m -> m.skip(Editor::setUser));
    }

    private void configureUserMappings(ModelMapper mapper) {

        mapper.typeMap(UserCreateDTO.class, User.class)
                .addMappings(m -> {
                    m.skip(User::setRoles);
                    m.skip(User::setPassword);
                });

        mapper.typeMap(UserUpdateDTO.class, User.class)
                .addMappings(m -> {
                    m.skip(User::setRoles);
                    m.skip(User::setPassword);
                });

        mapper.typeMap(User.class, UserUpdateDTO.class);
    }
}
