package com.project.HotelManagementSystem.service;


import com.project.HotelManagementSystem.dto.region.RegionCreateDTO;
import com.project.HotelManagementSystem.dto.region.RegionDTO;
import com.project.HotelManagementSystem.dto.region.RegionResponse;
import com.project.HotelManagementSystem.dto.region.RegionUpdateDTO;
import com.project.HotelManagementSystem.entity.Region;
import com.project.HotelManagementSystem.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
public class RegionService {

    private final RegionRepository regionRepository;
    private final ModelMapper modelMapper;

    public RegionCreateDTO createRegion(RegionCreateDTO regionCreateDTO) {
        Region region = modelMapper.map(regionCreateDTO, Region.class);
        this.regionRepository.save(region);
        return modelMapper.map(region, RegionCreateDTO.class);
    }

    public RegionUpdateDTO updateRegion(Long id, RegionUpdateDTO regionUpdateDTO) {
        Optional<Region> regionOp = regionRepository.findById(id);
        Region region = modelMapper.map(regionUpdateDTO, Region.class);
        if(regionOp.isPresent()){
            Region updatedRegion = regionOp.get();
            updatedRegion.setName(region.getName());
            updatedRegion.setCountry(region.getCountry());
            Region savedRegion = regionRepository.save(updatedRegion);
            return modelMapper.map(savedRegion, RegionUpdateDTO.class);
        }
        return null;
    }

    public void deleteRegion(Long id) {
        Optional<Region> regionOp = regionRepository.findById(id);
        if(regionOp.isPresent()){
            regionRepository.deleteById(id);
        }
    }

    public RegionDTO findRegionById(Long id) {
        Region region = regionRepository.findById(id).orElse(null);
        return modelMapper.map(region, RegionDTO.class);
    }

    public List<RegionDTO> findAllRegion() {
        List<Region> regions = regionRepository.findAll();
        return regions.stream().map(region -> modelMapper.map(region,RegionDTO.class)).toList();
    }

    public RegionResponse findAllRegionWithPagination(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Region> regionPage = regionRepository.findAll(pageable);
        List<Region> regions = regionPage.getContent();
        List<RegionDTO> regionDTOList = regions.stream().map(region -> modelMapper.map(region,RegionDTO.class)).toList();
        RegionResponse regionResponse = new RegionResponse();
        regionResponse.setRegions(regionDTOList);
        regionResponse.setPageNumber(regionPage.getNumber());
        regionResponse.setPageSize(regionPage.getSize());
        regionResponse.setTotalElements(regionPage.getTotalElements());
        regionResponse.setTotalPages(regionPage.getTotalPages());
        regionResponse.setLastPage(regionPage.isLast());
        return regionResponse;
    }
}