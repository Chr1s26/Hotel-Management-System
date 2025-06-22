    package com.project.HotelManagementSystem.service;

    import com.project.HotelManagementSystem.dto.hotel.HotelDTO;
    import com.project.HotelManagementSystem.dto.hotel.HotelResponse;
    import com.project.HotelManagementSystem.entity.Hotel;
    import com.project.HotelManagementSystem.repository.HotelRepository;
    import lombok.RequiredArgsConstructor;
    import org.modelmapper.ModelMapper;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.domain.Sort;
    import org.springframework.stereotype.Service;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Optional;
    import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    public class HotelService {

        private final HotelRepository hotelRepository;

        @Autowired
        private ModelMapper modelMapper;

        public HotelDTO createHotel(Hotel hotel) {
            Hotel savedHotel = hotelRepository.save(hotel);
            return modelMapper.map(savedHotel,HotelDTO.class);
        }

        public HotelDTO updateHotel(Long id,Hotel hotel) {
            Optional<Hotel> optionalHotel = this.hotelRepository.findById(id);
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
                Hotel savedHotel = this.hotelRepository.save(updatedHotel);
                return modelMapper.map(savedHotel,HotelDTO.class);
            }
            return null;
        }

        public void deleteHotel(Long id) {
            Optional<Hotel> optionalHotel = hotelRepository.findById(id);
            if(optionalHotel.isPresent()) {
                this.hotelRepository.deleteById(id);
            }
        }

        public HotelDTO findHotelById(Long id) {
            Hotel savedHotel = this.hotelRepository.findById(id).get();
            return modelMapper.map(savedHotel,HotelDTO.class);
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
