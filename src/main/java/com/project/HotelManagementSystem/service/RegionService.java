package com.project.HotelManagementSystem.service;


import com.project.HotelManagementSystem.entity.Region;
import com.project.HotelManagementSystem.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;

    public Region createRegion(Region region) {
        return regionRepository.save(region);
    }

    public Region updateRegion(Long id,Region region) {
        Optional<Region> regionOp = regionRepository.findById(id);
        if(regionOp.isPresent()){
            Region updatedRegion = regionOp.get();
            updatedRegion.setName(region.getName());
            updatedRegion.setCities(region.getCities());
            updatedRegion.setCountry(region.getCountry());
            regionRepository.save(updatedRegion);
            return updatedRegion;
        }
        return null;
    }

    public void deleteRegion(Long id) {
        Optional<Region> regionOp = regionRepository.findById(id);
        if(regionOp.isPresent()){
            regionRepository.deleteById(id);
        }
    }

    public Optional<Region> findRegionById(Long id) {
        Optional<Region> regionOp = regionRepository.findById(id);
        return regionOp;
    }

    public List<Region> findAllRegion() {
        return regionRepository.findAll();
    }
}