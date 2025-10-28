package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.region.*;
import com.project.HotelManagementSystem.entity.Region;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.entity.specification.RegionSpecification;
import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.RegionRepository;
import com.project.HotelManagementSystem.service.excelExport.ColumnSpec;
import com.project.HotelManagementSystem.service.excelExport.CommonExportProcess;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegionService extends CommonExportProcess<Region> {

    private final RegionRepository regionRepository;
    private final ModelMapper modelMapper;
    private final AuthService authService;

    public RegionCreateDTO createRegion(RegionCreateDTO regionCreateDTO) {
        Optional<Region> regionOp = regionRepository.findByNameIgnoreCase(regionCreateDTO.getName());
        if(regionOp.isPresent()) {
            throw new DuplicateException("regions",regionCreateDTO,"name","regions/create","A region with this name already exists");
        }
        Region region = modelMapper.map(regionCreateDTO, Region.class);
        region.setCreatedAt(LocalDateTime.now());
        region.setCreatedBy(authService.getCurrentUser());
        region.setStatus(StatusType.ACTIVE);
        this.regionRepository.save(region);
        return modelMapper.map(region, RegionCreateDTO.class);
    }

    public RegionUpdateDTO updateRegion(Long id, RegionUpdateDTO regionUpdateDTO) {
        Optional<Region> regionOp = regionRepository.findByNameIgnoreCase(regionUpdateDTO.getName());
        if(regionOp.isPresent() && !regionOp.get().getId().equals(id)) {
            throw new DuplicateException("regions",regionUpdateDTO,"name","regions/edit","A region with this name already exists");
        }
        Region region = modelMapper.map(regionUpdateDTO, Region.class);
        Region updatedRegionOp = this.regionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Region","id",id));
        updatedRegionOp.setName(region.getName());
        updatedRegionOp.setCountry(region.getCountry());
        updatedRegionOp.setStatus(StatusType.ACTIVE);
        updatedRegionOp.setUpdatedAt(LocalDateTime.now());
        updatedRegionOp.setUpdatedBy(authService.getCurrentUser());
        Region savedRegion = regionRepository.save(updatedRegionOp);
        return modelMapper.map(savedRegion, RegionUpdateDTO.class);

    }

    public void deleteRegion(Long id) {
        Region regionOp = regionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Region","id",id));
        regionRepository.deleteById(id);
    }

    public RegionDTO findRegionById(Long id) {
        Region region = regionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Region","id",id));
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

    public RegionResponse search(RegionSearchCriteria criteria) {
        Sort sortByAndOrder = criteria.getSortOrder().equalsIgnoreCase("asc") ? Sort.by(criteria.getSortBy()).ascending() : Sort.by(criteria.getSortBy()).descending();
        Pageable pageable = PageRequest.of(criteria.getPageNumber(),criteria.getPageSize(),sortByAndOrder);

        Specification<Region> spec = Specification.where(RegionSpecification.findByName(criteria.getName()))
                .and(RegionSpecification.findByCountry(criteria.getCountryName()));

        Page<Region> regionPage = regionRepository.findAll(spec,pageable);
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


    @Override
    public String getSheetName() {
        return "Regions";
    }

    @Override
    public List<Region> fetchData() {
        return regionRepository.findAll();
    }

    @Override
    public List<ColumnSpec<Region>> columns() {
        return List.of(
                new ColumnSpec<>("ID", r -> String.valueOf(r.getId()), null),
                new ColumnSpec<>("Name", Region::getName, null),
                new ColumnSpec<>("Country", r -> r.getCountry() != null ? r.getCountry().getName() : null, null)
        );
    }
}