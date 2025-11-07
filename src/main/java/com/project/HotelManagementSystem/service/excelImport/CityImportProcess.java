package com.project.HotelManagementSystem.service.excelImport;

import com.project.HotelManagementSystem.entity.City;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.repository.CityRepository;
import com.project.HotelManagementSystem.repository.RegionRepository;
import com.project.HotelManagementSystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CityImportProcess extends CommonImportProcess<City> {

    private final RegionRepository regionRepository;
    private final CityRepository cityRepository;
    private final AuthService authService;

    @Override
    public City mapRow(Row row) {
        String cityName = row.getCell(1).getStringCellValue();
        String regionName = row.getCell(2).getStringCellValue();

        var region = regionRepository.findByNameIgnoreCase(regionName).orElse(null);
        if (region == null) return null;

        if(cityRepository.findByNameIgnoreCase(cityName).isPresent()) return null;

        City city = new City();
        city.setName(cityName);
        city.setRegion(region);
        city.setStatus(StatusType.ACTIVE);
        city.setCreatedAt(LocalDateTime.now());
        city.setCreatedBy(authService.getCurrentUser());

        return city;
    }

    @Override
    public void saveEntity(City entity) {
        cityRepository.save(entity);
    }
}
