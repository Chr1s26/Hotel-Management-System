package com.project.HotelManagementSystem.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.document.room.RoomSearchDocument;
import com.project.HotelManagementSystem.dto.room.*;
import com.project.HotelManagementSystem.entity.*;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.entity.constants.RoomMediaType;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
    @Autowired
    private FileStorageRepository fileStorageRepository;
    @Autowired
    private AmazonS3 amazonS3;
    @Autowired
    private ElasticsearchClient elasticsearchClient;
    @Autowired
    private RoomTypeService roomTypeService;

    public RoomCreateDTO createRoom(RoomCreateDTO roomCreateDTO) throws IOException {
        RoomType roomType = roomTypeService.findOrCreate(roomCreateDTO.getRoomTypeName(),roomCreateDTO.getPrice(),roomCreateDTO.getRoomSize(),roomCreateDTO.getCapacity());
        Hotel hotel = hotelRepository.findById(roomCreateDTO.getHotel().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "hotel", roomCreateDTO.getHotel().getId(), "id", "rooms/create",
                        "Hotel not found"));
        Room room = new Room();
        room.setAvailable(roomCreateDTO.isAvailable());
        room.setDescription(roomCreateDTO.getDescription());
        room.setRoomType(roomType);
        room.setHotel(hotel);
        room.setAmenities(new HashSet<>(amenitiesRepository.findAllById(roomCreateDTO.getAmenityIds())));
        room.setPromotions(new HashSet<>(promotionRepository.findAllById(roomCreateDTO.getPromotionIds())));
        room.setStatus(StatusType.ACTIVE);
        room.setCreatedAt(LocalDateTime.now());
        room.setCreatedBy(authService.getCurrentUser());
        Room savedRoom = roomRepository.save(room);
        RoomSearchDocument roomSearchDocument = this.mapToSearchDoc(savedRoom);
        IndexResponse response = elasticsearchClient.index(i -> i.index(AppConstants.ROOM_INDEX_NAME)
                .id(savedRoom.getId().toString())
                .document(roomSearchDocument));
        this.addAttachment(roomCreateDTO.getFiles(),savedRoom.getId(),roomCreateDTO.getRoomMediaType(),roomCreateDTO.getHotel().getId());
        return modelMapper.map(savedRoom, RoomCreateDTO.class);
    }

    public RoomUpdateDTO updateRoom(Long id, RoomUpdateDTO roomUpdateDTO) throws IOException {
        RoomType roomType = roomTypeService.findOrCreate(roomUpdateDTO.getRoomTypeName(),roomUpdateDTO.getPrice(),roomUpdateDTO.getRoomSize(),roomUpdateDTO.getCapacity());
        Room roomOp = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("room",roomUpdateDTO,"id","rooms/edit","A room with this id cannot be found"));
        Hotel hotel = hotelRepository.findById(roomUpdateDTO.getHotel().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "hotel", roomUpdateDTO.getHotel().getId(), "id", "rooms/edit",
                        "Hotel not found"));

        roomOp.setAvailable(roomUpdateDTO.isAvailable());
        roomOp.setDescription(roomUpdateDTO.getDescription());
        roomOp.setRoomType(roomType);
        roomOp.setHotel(hotel);
        roomOp.setAmenities(new HashSet<>(amenitiesRepository.findAllById(roomUpdateDTO.getAmenityIds())));
        roomOp.setPromotions(new HashSet<>(promotionRepository.findAllById(roomUpdateDTO.getPromotionIds())));
        roomOp.setStatus(StatusType.ACTIVE);
        roomOp.setUpdatedAt(LocalDateTime.now());
        roomOp.setUpdatedBy(authService.getCurrentUser());
        Room savedRoom = roomRepository.save(roomOp);
        RoomSearchDocument roomSearchDocument = this.mapToSearchDoc(savedRoom);
        IndexResponse response = elasticsearchClient.index(i -> i.index(AppConstants.ROOM_INDEX_NAME)
                .id(savedRoom.getId().toString())
                .document(roomSearchDocument));
        this.addAttachment(roomUpdateDTO.getFiles(),savedRoom.getId(),roomUpdateDTO.getRoomMediaType(),roomUpdateDTO.getHotel().getId());
        return modelMapper.map(savedRoom, RoomUpdateDTO.class);
    }

    public void addAttachment(List<MultipartFile> files, Long roomId, RoomMediaType roomMediaType,long hotelId) {
        if(files==null || files.isEmpty()) return ;

        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if(optionalRoom.isEmpty()) throw new ResourceNotFoundException("room",optionalRoom,"id","rooms","A room with this id cannot be found");
        Optional<Hotel> optionalHotel = hotelRepository.findById(hotelId);
        if(optionalHotel.isEmpty()) throw new ResourceNotFoundException("hotel",optionalHotel,"id","hotels","A hotel with this id cannot be found");
        for(MultipartFile multipartFile : files) {
            if(multipartFile.isEmpty()) continue;
            RoomAttachment roomAttachment = new RoomAttachment();
            roomAttachment.setRoom(optionalRoom.get());
            roomAttachment.setHotel(optionalHotel.get());
            roomAttachment.setRoomMediaType(roomMediaType);
            roomAttachment.setStatus(StatusType.ACTIVE);
            roomAttachment.setCreatedAt(LocalDateTime.now());
            roomAttachment.setCreatedBy(authService.getCurrentUser());
            roomAttachment = roomAttachmentRepository.save(roomAttachment);
            fileService.handleFileUpload(multipartFile, FileType.ROOM_ATTACHMENT, roomAttachment.getId(), "s3");
        }
    }

    public void deleteRoom(Long id) {
        Optional<Room> roomOp = roomRepository.findById(id);
        if (roomOp.isEmpty()) {
            throw new ResourceNotFoundException("room",roomOp,"id","rooms","A room with this id cannot be found");
        }
        roomRepository.deleteById(id);
    }

    public RoomUpdateDTO findRoomById(Long id) {
        Optional<Room> roomOp = roomRepository.findById(id);
        if (roomOp.isEmpty()) {
            throw new ResourceNotFoundException("room",roomOp,"id","rooms","A room with this id cannot be found");
        }
        Room room = roomOp.get();
        RoomUpdateDTO dto = new RoomUpdateDTO();
        dto.setAvailable(room.isAvailable());
        dto.setDescription(room.getDescription());
        dto.setRoomSize(room.getRoomType().getRoomSize());
        dto.setHotel(room.getHotel());
        dto.setPrice(room.getRoomType().getPrice());
        dto.setRoomTypeName(room.getRoomType().getName());
        dto.setCapacity(room.getRoomType().getCapacity());
        dto.setAmenityIds(room.getAmenities().stream().map(Amenities::getId).collect(Collectors.toSet()));
        dto.setPromotionIds(room.getPromotions().stream().map(Promotion::getId).collect(Collectors.toSet()));
        return dto;
    }

    public RoomDTO findById(Long id) {
        Optional<Room> roomOp = roomRepository.findById(id);
        if (roomOp.isEmpty()) {
            throw new ResourceNotFoundException("room",roomOp,"id","rooms","A room with this id cannot be found");
        }
        return toDTO(roomOp.get());
    }

    public RoomDTO toDTO(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setAvailable(room.isAvailable());
        roomDTO.setDescription(room.getDescription());
        roomDTO.setRoomSize(room.getRoomType().getRoomSize());
        roomDTO.setRoomTypeName(room.getRoomType().getName());
        roomDTO.setPrice(room.getRoomType().getPrice());
        roomDTO.setHotel(room.getHotel());
        roomDTO.setAmenities(room.getAmenities());
        roomDTO.setPromotions(room.getPromotions());
        roomDTO.setStatus(room.getStatus());
        roomDTO.setCreatedAt(room.getCreatedAt());
        roomDTO.setCreatedBy(room.getCreatedBy());
        roomDTO.setUpdatedAt(room.getUpdatedAt());
        roomDTO.setUpdatedBy(room.getUpdatedBy());
        return roomDTO;
    }

    public List<RoomPhotoDTO> getRoomPhotos(Long roomId,RoomMediaType roomMediaType) {
        List<RoomAttachment> roomAttachments = roomAttachmentRepository.findByRoomIdAndRoomMediaType(roomId,roomMediaType);
        List<RoomPhotoDTO> roomPhotos = new ArrayList<>();
        for(RoomAttachment attachment : roomAttachments){
            String fileUrl = fileService.getFileNames(FileType.ROOM_ATTACHMENT, attachment.getId());
            roomPhotos.add(new RoomPhotoDTO(attachment.getId(),fileUrl));
        }
        return roomPhotos;
    }

    public void deleteRoomAttachmentAndFiles(Long attachmentId){
        List<FileStorage> files = fileStorageRepository.findAllByFileTypeAndFileIdOrderByCreatedAtDesc(FileType.ROOM_ATTACHMENT,attachmentId);
        for(FileStorage file : files){
            try{
                if("S3".equalsIgnoreCase(file.getServiceName())) amazonS3.deleteObject(new DeleteObjectRequest(fileService.getBucketName(), file.getKey()));
                fileStorageRepository.delete(file);
            }catch (Exception e){
                throw new RuntimeException("Failed to delete file",e);
            }
        }
        roomAttachmentRepository.deleteById(attachmentId);
    }

    public List<RoomDTO> findAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream().map(room -> modelMapper.map(room, RoomDTO.class)).collect(Collectors.toList());
    }

    private RoomSearchDocument mapToSearchDoc(Room room) {
        if(room == null) return null;

        RoomSearchDocument roomSearchDocument = new RoomSearchDocument();
        roomSearchDocument.setId(room.getId() != null ? room.getId().toString() : null);
        roomSearchDocument.setPrice(room.getRoomType().getPrice());
        roomSearchDocument.setAvailable(room.isAvailable());
        roomSearchDocument.setRoomType(room.getRoomType().getName());
        roomSearchDocument.setMaxCapacity(room.getRoomType().getCapacity());
        return roomSearchDocument;
    }

//    public RoomResponse findAllRoomsWithPagination(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
//        Sort sortByAndSortOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
//        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndSortOrder);
//        Page<Room> roomPage = roomRepository.findAll(pageable);
//        List<Room> roomList = roomPage.getContent();
//        List<RoomDTO> roomDTOList = roomList.stream().map(room -> modelMapper.map(room, RoomDTO.class)).toList();
//        RoomResponse roomResponse = new RoomResponse();
//        roomResponse.setRooms(roomDTOList);
//        roomResponse.setPageNumber(roomPage.getNumber());
//        roomResponse.setPageSize(roomPage.getSize());
//        roomResponse.setTotalPages(roomPage.getTotalPages());
//        roomResponse.setTotalElements(roomPage.getTotalElements());
//        roomResponse.setLastPage(roomPage.isLast());
//        return  roomResponse;
//    }
}