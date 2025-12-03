    package com.project.HotelManagementSystem.service;

    import co.elastic.clients.elasticsearch.ElasticsearchClient;
    import co.elastic.clients.elasticsearch.core.IndexResponse;
    import com.amazonaws.services.s3.AmazonS3;
    import com.amazonaws.services.s3.model.DeleteObjectRequest;
    import com.project.HotelManagementSystem.dto.document.hotel.HotelSearchDocument;
    import com.project.HotelManagementSystem.dto.hotel.*;
    import com.project.HotelManagementSystem.entity.FileStorage;
    import com.project.HotelManagementSystem.entity.Hotel;
    import com.project.HotelManagementSystem.entity.HotelAttachment;
    import com.project.HotelManagementSystem.entity.constants.FileType;
    import com.project.HotelManagementSystem.entity.constants.StatusType;
    import com.project.HotelManagementSystem.entity.constants.HotelMediaType;
    import com.project.HotelManagementSystem.exception.DuplicateException;
    import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
    import com.project.HotelManagementSystem.repository.*;
    import lombok.RequiredArgsConstructor;
    import org.modelmapper.ModelMapper;
    import org.springframework.stereotype.Service;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.IOException;
    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Optional;

    @Service
    @RequiredArgsConstructor
    public class HotelService {

        private final HotelRepository hotelRepository;
        private final PolicyRepository policyRepository;
        private final PromotionRepository promotionRepository;
        private final ModelMapper modelMapper;
        private final FileService fileService;
        private final AuthService authService;
        private final HotelAttachmentRepository hotelAttachmentRepository;
        private final FileStorageRepository fileStorageRepository;
        private final AmazonS3 amazonS3;
        private final ElasticsearchClient elasticsearchClient;
        private static final String INDEX_NAME = "hotels";

        public HotelCreateDTO createHotel(HotelCreateDTO hotelCreateDTO) throws IOException {
            Optional<Hotel> hotelOp = hotelRepository.findHotelByAddress(hotelCreateDTO.getAddress());
            if(hotelOp.isPresent()) {
                throw new DuplicateException("hotel",hotelCreateDTO,"name","hotels/create","Hotel with this address already exists");
            }
            Hotel hotel = modelMapper.map(hotelCreateDTO, Hotel.class);
            hotel.setPolicies(new HashSet<>(policyRepository.findAllById(hotelCreateDTO.getPolicyIds())));
            hotel.setPromotions(new HashSet<>(promotionRepository.findAllById(hotelCreateDTO.getPromotionIds())));

            hotel.setCreatedAt(LocalDateTime.now());
            hotel.setCreatedBy(authService.getCurrentUser());
            hotel.setStatus(StatusType.ACTIVE);
            Hotel savedHotel = hotelRepository.save(hotel);
            HotelSearchDocument hotelSearchDocument = this.mapToSearchDoc(savedHotel);
            IndexResponse response = elasticsearchClient.index(i -> i.index(INDEX_NAME)
                                            .id(savedHotel.getId().toString())
                                            .document(hotelSearchDocument));
            this.addAttachment(hotelCreateDTO.getFiles(), savedHotel.getId(), hotelCreateDTO.getHotelMediaType());
            return modelMapper.map(savedHotel,HotelCreateDTO.class);
        }

        public HotelUpdateDTO updateHotel(Long id, HotelUpdateDTO hotelUpdateDTO) {
            Optional<Hotel> hotelOp = hotelRepository.findHotelByAddressAndIdNot(hotelUpdateDTO.getAddress(),hotelUpdateDTO.getId());
            if(hotelOp.isPresent()) {
                throw new DuplicateException("hotel",hotelUpdateDTO,"name","hotels/edit","Hotel with this address already exists");
            }
            Hotel optionalHotel = this.hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("hotel",hotelUpdateDTO,"id","hotels/edit"," A hotel with the id cannot be found"));
            Hotel hotel = modelMapper.map(hotelUpdateDTO, Hotel.class);
            optionalHotel.setName(hotel.getName());
            optionalHotel.setPhoneNumber(hotel.getPhoneNumber());
            optionalHotel.setEmail(hotel.getEmail());
            optionalHotel.setDescription(hotel.getDescription());
            optionalHotel.setRating(hotel.getRating());
            optionalHotel.setHotelType(hotel.getHotelType());
            optionalHotel.setAddress(hotel.getAddress());
            optionalHotel.setPropertyDescription(hotel.getPropertyDescription());
            optionalHotel.setPolicies(new HashSet<>(policyRepository.findAllById(hotelUpdateDTO.getPolicyIds())));
            optionalHotel.setPromotions(new HashSet<>(promotionRepository.findAllById(hotelUpdateDTO.getPromotionIds())));
            optionalHotel.setUpdatedAt(LocalDateTime.now());
            optionalHotel.setUpdatedBy(authService.getCurrentUser());
            optionalHotel.setStatus(StatusType.ACTIVE);
            Hotel savedHotel = this.hotelRepository.save(optionalHotel);
            this.addAttachment(hotelUpdateDTO.getFiles(), savedHotel.getId(),hotelUpdateDTO.getHotelMediaType());
            return modelMapper.map(savedHotel,HotelUpdateDTO.class);
        }

        public void deleteHotel(Long id) {
            Optional<Hotel> optionalHotel = hotelRepository.findById(id);
            if(optionalHotel.isEmpty()) {
                throw new ResourceNotFoundException("hotels",optionalHotel,"id","hotels"," A hotel with the id cannot be found");
            }
            this.hotelRepository.delete(optionalHotel.get());
        }

        public HotelUpdateDTO findHotelById(Long id) {
            Optional<Hotel> optionalHotel = hotelRepository.findById(id);
            if(optionalHotel.isEmpty()) {
                throw new ResourceNotFoundException("hotels",optionalHotel,"id","hotels"," A hotel with the id cannot be found");
            }
            HotelUpdateDTO hotelUpdateDTO = modelMapper.map(optionalHotel.get(), HotelUpdateDTO.class);
            return hotelUpdateDTO;
        }

        public HotelDTO findById(Long id) {
            Optional<Hotel> optionalHotel = hotelRepository.findById(id);
            if(optionalHotel.isEmpty()) {
                throw new ResourceNotFoundException("hotels",optionalHotel,"id","hotels"," A hotel with the id cannot be found");
            }
            return toDTO(optionalHotel.get());
        }

//        public HotelResponse findAllHotelsWithPagination(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
//            Sort sortByAndSortOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
//            Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndSortOrder);
//            Page<Hotel> page = this.hotelRepository.findAll(pageable);
//            List<HotelDTO> hotelDTOS = page.getContent().stream().map(hotel -> modelMapper.map(hotel, HotelDTO.class)).toList();
//            HotelResponse hotelResponse = new HotelResponse();
//            hotelResponse.setHotels(hotelDTOS);
//            hotelResponse.setPageNumber(page.getNumber());
//            hotelResponse.setPageSize(page.getSize());
//            hotelResponse.setTotalPages(page.getTotalPages());
//            hotelResponse.setTotalElements(page.getTotalElements());
//            hotelResponse.setLastPage(page.isLast());
//            return hotelResponse;
//        }

        public List<HotelDTO> findAllHotels() {
            List<Hotel> hotels = this.hotelRepository.findAll();
            List<HotelDTO> hotelDTOs = hotels.stream().map(this::toDTO).toList();
            return hotelDTOs;
        }

        public List<HotelPhotoDTO> getHotelPhotos(Long hotelId, HotelMediaType hotelMediaType)  {
            List<HotelAttachment> hotelAttachments = hotelAttachmentRepository.findByHotelIdAndHotelMediaType(hotelId, hotelMediaType);
            List<HotelPhotoDTO> hotelPhotos = new ArrayList<>();
            for(HotelAttachment attachment : hotelAttachments){
                String fileUrls = fileService.getFileNames(FileType.HOTEL_ATTACHMENT, attachment.getId());
                hotelPhotos.add(new HotelPhotoDTO(attachment.getId(), fileUrls));
            }
            return hotelPhotos;
        }

        public void deleteHotelAttachmentAndFiles(Long attachmentId) {
            List<FileStorage> files = fileStorageRepository.findAllByFileTypeAndFileIdOrderByCreatedAtDesc(FileType.HOTEL_ATTACHMENT, attachmentId);
            for(FileStorage file : files) {
                try{
                    if("S3".equalsIgnoreCase(file.getServiceName())){
                        amazonS3.deleteObject(new DeleteObjectRequest(fileService.getBucketName(), file.getKey()));
                    }
                    fileStorageRepository.delete(file);
                }catch (Exception e){
                    throw new RuntimeException("Failed to delete hotel file: " + e.getMessage());
                }
            }
            hotelAttachmentRepository.deleteById(attachmentId);
        }

        public void addAttachment(List<MultipartFile> files, Long hotelId, HotelMediaType hotelMediaType){
            if(files==null || files.isEmpty()) { return;}

            Optional<Hotel> optionalHotel = this.hotelRepository.findById(hotelId);
            if(optionalHotel.isEmpty()) {
                throw new ResourceNotFoundException("hotels",optionalHotel,"id","hotels/edit"," A hotel with the id cannot be found");
            }
            Hotel hotel = optionalHotel.get();
            for(MultipartFile multipartFile : files) {
                if(multipartFile.isEmpty()) continue;
                HotelAttachment hotelAttachment = new HotelAttachment();
                hotelAttachment.setHotel(hotel);
                hotelAttachment.setHotelMediaType(hotelMediaType);
                hotelAttachment.setStatus(StatusType.ACTIVE);
                hotelAttachment.setCreatedAt(LocalDateTime.now());
                hotelAttachment.setCreatedBy(authService.getCurrentUser());
                hotelAttachment = hotelAttachmentRepository.save(hotelAttachment);
                fileService.handleFileUpload(multipartFile, FileType.HOTEL_ATTACHMENT, hotelAttachment.getId(), "s3");
            }
        }

        private HotelDTO toDTO(Hotel hotel) {
            HotelDTO hotelDTO = new HotelDTO();
            hotelDTO.setId(hotel.getId());
            hotelDTO.setName(hotel.getName());
            hotelDTO.setPhoneNumber(hotel.getPhoneNumber());
            hotelDTO.setEmail(hotel.getEmail());
            hotelDTO.setDescription(hotel.getDescription());
            hotelDTO.setRating(hotel.getRating());
            hotelDTO.setHotelType(hotel.getHotelType());
            hotelDTO.setAddress(hotel.getAddress());
            hotelDTO.setPropertyDescription(hotel.getPropertyDescription());
            hotelDTO.setPromotions(hotel.getPromotions());
            hotelDTO.setPolicies(hotel.getPolicies());
            hotelDTO.setCreatedBy(hotel.getCreatedBy());
            hotelDTO.setCreatedAt(hotel.getCreatedAt());
            hotelDTO.setUpdatedAt(hotel.getUpdatedAt());
            hotelDTO.setUpdatedBy(hotel.getUpdatedBy());
            hotelDTO.setStatus(hotel.getStatus());
            return hotelDTO;
        }

        private HotelSearchDocument mapToSearchDoc(Hotel hotel) {
            if (hotel == null) {
                return null; // or throw IllegalArgumentException
            }

            HotelSearchDocument doc = new HotelSearchDocument();

            // id
            doc.setId(hotel.getId() != null ? hotel.getId().toString() : null);

            // name
            doc.setName(hotel.getName());  // nullable is fine if ES mapping allows it

            // city name (guard nested nulls)
            String cityName = null;
            if (hotel.getAddress() != null &&
                    hotel.getAddress().getCity() != null &&
                    hotel.getAddress().getCity().getName() != null) {

                cityName = hotel.getAddress().getCity().getName();
            }
            doc.setCity(cityName);

            // description
            doc.setDescription(hotel.getDescription());

            return doc;
        }

    }
