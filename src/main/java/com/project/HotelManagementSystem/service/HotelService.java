    package com.project.HotelManagementSystem.service;

    import com.project.HotelManagementSystem.dto.hotel.HotelCreateDTO;
    import com.project.HotelManagementSystem.dto.hotel.HotelDTO;
    import com.project.HotelManagementSystem.dto.hotel.HotelResponse;
    import com.project.HotelManagementSystem.dto.hotel.HotelUpdateDTO;
    import com.project.HotelManagementSystem.entity.Hotel;
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

        @Autowired
        private ModelMapper modelMapper;

        public HotelCreateDTO createHotel(HotelCreateDTO hotelCreateDTO) {
            Hotel hotel = modelMapper.map(hotelCreateDTO, Hotel.class);
            hotel.setPolicies(new HashSet<>(policyRepository.findAllById(hotelCreateDTO.getPolicyIds())));
            hotel.setPromotions(new HashSet<>(promotionRepository.findAllById(hotelCreateDTO.getPromotionIds())));
            Hotel savedHotel = hotelRepository.save(hotel);
            return modelMapper.map(savedHotel,HotelCreateDTO.class);
        }

        public HotelUpdateDTO updateHotel(Long id, HotelUpdateDTO hotelUpdateDTO) {
            Optional<Hotel> optionalHotel = this.hotelRepository.findById(id);
            Hotel hotel = modelMapper.map(hotelUpdateDTO, Hotel.class);
            if(optionalHotel.isPresent()) {
                Hotel updatedHotel = optionalHotel.get();
                updatedHotel.setName(hotel.getName());
                updatedHotel.setPhoneNumber(hotel.getPhoneNumber());
                updatedHotel.setEmail(hotel.getEmail());
                updatedHotel.setDescription(hotel.getDescription());
                updatedHotel.setRating(hotel.getRating());
                updatedHotel.setHotelType(hotel.getHotelType());
                updatedHotel.setAddress(hotel.getAddress());
                updatedHotel.setPropertyDescription(hotel.getPropertyDescription());
                updatedHotel.setPolicies(new HashSet<>(policyRepository.findAllById(hotelUpdateDTO.getPolicyIds())));
                updatedHotel.setPromotions(new HashSet<>(promotionRepository.findAllById(hotelUpdateDTO.getPromotionIds())));
                Hotel savedHotel = this.hotelRepository.save(updatedHotel);
                return modelMapper.map(savedHotel,HotelUpdateDTO.class);
            }
            return null;
        }

        public void deleteHotel(Long id) {
            Optional<Hotel> optionalHotel = hotelRepository.findById(id);
            if(optionalHotel.isPresent()) {
                this.hotelRepository.deleteById(id);
            }
        }

        public HotelUpdateDTO findHotelById(Long id) {
            Hotel savedHotel = this.hotelRepository.findById(id).get();
            HotelUpdateDTO hotelUpdateDTO = modelMapper.map(savedHotel, HotelUpdateDTO.class);
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
