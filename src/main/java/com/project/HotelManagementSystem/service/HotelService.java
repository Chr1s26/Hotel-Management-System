    package com.project.HotelManagementSystem.service;

    import com.project.HotelManagementSystem.dto.hotel.HotelCreateDTO;
    import com.project.HotelManagementSystem.dto.hotel.HotelDTO;
    import com.project.HotelManagementSystem.dto.hotel.HotelResponse;
    import com.project.HotelManagementSystem.dto.hotel.HotelUpdateDTO;
    import com.project.HotelManagementSystem.entity.Hotel;
    import com.project.HotelManagementSystem.entity.constants.FileType;
    import com.project.HotelManagementSystem.exception.DuplicateException;
    import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
    import com.project.HotelManagementSystem.repository.AmenitiesRepository;
    import com.project.HotelManagementSystem.repository.HotelRepository;
    import com.project.HotelManagementSystem.repository.PolicyRepository;
    import com.project.HotelManagementSystem.repository.PromotionRepository;
    import lombok.RequiredArgsConstructor;
    import org.modelmapper.ModelMapper;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.domain.Sort;
    import org.springframework.stereotype.Service;

    import java.util.ArrayList;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Optional;
    import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    public class HotelService {

        private final HotelRepository hotelRepository;
        private final PolicyRepository policyRepository;
        private final PromotionRepository promotionRepository;
        private final ModelMapper modelMapper;
        private final FileService fileService;

        public HotelCreateDTO createHotel(HotelCreateDTO hotelCreateDTO) {
            Optional<Hotel> hotelOp = hotelRepository.findHotelByNameAndCoordinates(hotelCreateDTO.getName(),hotelCreateDTO.getAddress().getLatitude(), hotelCreateDTO.getAddress().getLongitude());
            if(hotelOp.isPresent()) {
                throw new DuplicateException("Hotel with name " + hotelCreateDTO.getName() + " And with same latitude and longitude already exists");
            }
            Hotel hotel = modelMapper.map(hotelCreateDTO, Hotel.class);
            hotel.setPolicies(new HashSet<>(policyRepository.findAllById(hotelCreateDTO.getPolicyIds())));
            hotel.setPromotions(new HashSet<>(promotionRepository.findAllById(hotelCreateDTO.getPromotionIds())));
            Hotel savedHotel = hotelRepository.save(hotel);
            fileService.handleFileUpload(hotelCreateDTO.getFile(), FileType.USER,savedHotel.getId(),"local");
            return modelMapper.map(savedHotel,HotelCreateDTO.class);
        }

        public HotelUpdateDTO updateHotel(Long id, HotelUpdateDTO hotelUpdateDTO) {
            Optional<Hotel> hotelOp = hotelRepository.findHotelByNameAndCoordinates(hotelUpdateDTO.getName(),hotelUpdateDTO.getAddress().getLatitude(), hotelUpdateDTO.getAddress().getLongitude());
            if(hotelOp.isPresent()) {
                throw new DuplicateException("Hotel with name " + hotelUpdateDTO.getName() + " And with same latitude and longitude already exists");
            }
            Hotel optionalHotel = this.hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", id));
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
            Hotel savedHotel = this.hotelRepository.save(optionalHotel);
            fileService.handleFileUpload(hotelUpdateDTO.getFile(), FileType.USER,savedHotel.getId(),"s3");
            return modelMapper.map(savedHotel,HotelUpdateDTO.class);
        }

        public void deleteHotel(Long id) {
            Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", id));
            this.hotelRepository.delete(hotel);
        }

        public HotelUpdateDTO findHotelById(Long id) {
            Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", id));
            HotelUpdateDTO hotelUpdateDTO = modelMapper.map(hotel, HotelUpdateDTO.class);
            return hotelUpdateDTO;
        }

        public HotelResponse findAllHotelsWithPagination(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
            Sort sortByAndSortOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndSortOrder);
            Page<Hotel> page = this.hotelRepository.findAll(pageable);
            List<HotelDTO> hotelDTOS = page.getContent().stream().map(hotel -> modelMapper.map(hotel, HotelDTO.class)).toList();
            HotelResponse hotelResponse = new HotelResponse();
            hotelResponse.setHotels(hotelDTOS);
            hotelResponse.setPageNumber(page.getNumber());
            hotelResponse.setPageSize(page.getSize());
            hotelResponse.setTotalPages(page.getTotalPages());
            hotelResponse.setTotalElements(page.getTotalElements());
            hotelResponse.setLastPage(page.isLast());
            return hotelResponse;
        }

        public List<HotelDTO> findAllHotels() {
            List<Hotel> hotels = this.hotelRepository.findAll();
            List<HotelDTO> hotelDTOs = hotels.stream().map(h -> modelMapper.map(h, HotelDTO.class)).toList();
            return hotelDTOs;
        }
    }
