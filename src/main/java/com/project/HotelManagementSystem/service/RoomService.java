package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.room.RoomCreateDTO;
import com.project.HotelManagementSystem.dto.room.RoomDTO;
import com.project.HotelManagementSystem.dto.room.RoomResponse;
import com.project.HotelManagementSystem.dto.room.RoomUpdateDTO;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.Room;
import com.project.HotelManagementSystem.entity.RoomAttachment;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.entity.constants.RoomMediaType;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final AmenitiesRepository amenitiesRepository;
    private final PromotionRepository promotionRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private RoomAttachmentRepository roomAttachmentRepository;
    @Autowired
    private FileService fileService;

    public RoomCreateDTO createRoom(RoomCreateDTO roomCreateDTO) {
        Room room = modelMapper.map(roomCreateDTO, Room.class);
        room.setAmenities(new HashSet<>(amenitiesRepository.findAllById(roomCreateDTO.getAmenityIds())));
        room.setPromotions(new HashSet<>(promotionRepository.findAllById(roomCreateDTO.getPromotionIds())));
        Room savedRoom = roomRepository.save(room);
        this.addAttachment(roomCreateDTO.getFiles(),savedRoom.getId(),roomCreateDTO.getRoomMediaType(),roomCreateDTO.getHotel().getId());
        return modelMapper.map(savedRoom, RoomCreateDTO.class);
    }

    public RoomUpdateDTO updateRoom(Long id, RoomUpdateDTO roomUpdateDTO) {
        Room roomOp = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));
        Room room = modelMapper.map(roomUpdateDTO, Room.class);

        roomOp.setPrice(room.getPrice());
        roomOp.setAvailable(room.isAvailable());
        roomOp.setDescription(room.getDescription());
        roomOp.setRoomType(room.getRoomType());
        roomOp.setMaxCapacity(room.getMaxCapacity());
        roomOp.setHotel(room.getHotel());
        roomOp.setAmenities(new HashSet<>(amenitiesRepository.findAllById(roomUpdateDTO.getAmenityIds())));
        roomOp.setPromotions(new HashSet<>(promotionRepository.findAllById(roomUpdateDTO.getPromotionIds())));
        Room savedRoom = roomRepository.save(roomOp);
        this.addAttachment(roomUpdateDTO.getFiles(),savedRoom.getId(),roomUpdateDTO.getRoomMediaType(),roomUpdateDTO.getHotel().getId());
        return modelMapper.map(savedRoom, RoomUpdateDTO.class);
    }

    private void addAttachment(List<MultipartFile> files, Long roomId, @NotNull(message = "Media type must be selected") RoomMediaType roomMediaType,long hotelId) {
        if(files==null || files.isEmpty()) return ;

        Room optionalRoom = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room","id",roomId));
        Hotel optionalHotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel","id",hotelId));
        for(MultipartFile multipartFile : files) {
            if(multipartFile.isEmpty()) continue;
            RoomAttachment roomAttachment = new RoomAttachment();
            roomAttachment.setRoom(optionalRoom);
            roomAttachment.setHotel(optionalHotel);
            roomAttachment.setRoomMediaType(roomMediaType);
            roomAttachment.setStatus(StatusType.ACTIVE);
            roomAttachment.setCreatedAt(LocalDateTime.now());
            roomAttachment.setCreatedBy(authService.getCurrentUser());
            roomAttachment = roomAttachmentRepository.save(roomAttachment);
            fileService.handleFileUpload(multipartFile, FileType.ROOM_ATTACHMENT, roomAttachment.getId(), "s3");
        }
    }



    public void deleteRoom(Long id) {
        Room roomOp = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));
        roomRepository.deleteById(id);
    }

    public RoomUpdateDTO findRoomById(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));
        return modelMapper.map(room, RoomUpdateDTO.class);
    }

    public List<RoomDTO> findAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream().map(room -> modelMapper.map(room, RoomDTO.class)).collect(Collectors.toList());
    }

    public RoomResponse findAllRoomsWithPagination(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndSortOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndSortOrder);
        Page<Room> roomPage = roomRepository.findAll(pageable);
        List<Room> roomList = roomPage.getContent();
        List<RoomDTO> roomDTOList = roomList.stream().map(room -> modelMapper.map(room, RoomDTO.class)).toList();
        RoomResponse roomResponse = new RoomResponse();
        roomResponse.setRooms(roomDTOList);
        roomResponse.setPageNumber(roomPage.getNumber());
        roomResponse.setPageSize(roomPage.getSize());
        roomResponse.setTotalPages(roomPage.getTotalPages());
        roomResponse.setTotalElements(roomPage.getTotalElements());
        roomResponse.setLastPage(roomPage.isLast());
        return  roomResponse;
    }
}