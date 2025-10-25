package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.city.*;
import com.project.HotelManagementSystem.entity.City;
import com.project.HotelManagementSystem.entity.Region;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.entity.specification.CitySpecification;
import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.CityRepository;
import com.project.HotelManagementSystem.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;
    private final RegionRepository regionRepository;
    private final ModelMapper modelMapper;
    private final AuthService authService;

    public CityCreateDTO createCity(CityCreateDTO cityCreateDTO) {
        Optional<City> cityOp = this.cityRepository.findByNameIgnoreCase(cityCreateDTO.getName());
        if(cityOp.isPresent()) {
            throw new DuplicateException("Another City with name " + cityCreateDTO.getName() + " already exists");
        }
        City city = modelMapper.map(cityCreateDTO, City.class);
        city.setStatus(StatusType.ACTIVE);
        city.setCreatedAt(LocalDateTime.now());
        city.setCreatedBy(authService.getCurrentUser());
        City savedCity = cityRepository.save(city);
        return modelMapper.map(savedCity,CityCreateDTO.class);
    }

    public CityUpdateDTO updateCity(Long id, CityUpdateDTO cityUpdateDTO) {
        Optional<City> cityOptional = this.cityRepository.findByNameIgnoreCaseAndIdNot(cityUpdateDTO.getName(),cityUpdateDTO.getId());
        if(cityOptional.isPresent() && !cityOptional.get().getId().equals(id)) {
            throw new DuplicateException("Another City with name " + cityUpdateDTO.getName() + " already exists");
        }
        City cityOp = cityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("City","id",id));
        City city = modelMapper.map(cityUpdateDTO, City.class);
        cityOp.setName(city.getName());
        cityOp.setRegion(city.getRegion());
        cityOp.setUpdatedAt(LocalDateTime.now());
        cityOp.setUpdatedBy(authService.getCurrentUser());
        cityOp.setStatus(StatusType.ACTIVE);
        City savedCity = cityRepository.save(cityOp);
        return modelMapper.map(savedCity,CityUpdateDTO.class);
    }

    public void deleteCity(Long id) {
        City city = cityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("City", "id", id));
        cityRepository.delete(city);
    }

    public CityDTO findCityById(Long id) {
        City city = cityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("City", "id", id));
        return modelMapper.map(city,CityDTO.class);
    }

    public CityResponse findAllCitiesWithPagination(Integer pageNumber,Integer pageSize,String sortBy, String sortOrder){
        Sort sortByAndSortOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndSortOrder);
        Page<City> cityPage = cityRepository.findAll(pageable);
        List<City> cities = cityPage.getContent();
        List<CityDTO> cityDTOList = cities.stream().map(c -> modelMapper.map(c, CityDTO.class)).toList();
        CityResponse cityResponse = new CityResponse();
        cityResponse.setCities(cityDTOList);
        cityResponse.setPageNumber(pageNumber);
        cityResponse.setPageSize(pageSize);
        cityResponse.setTotalPages(cityPage.getTotalPages());
        cityResponse.setTotalElements(cityPage.getTotalElements());
        cityResponse.setLastPage(cityPage.isLast());
        return cityResponse;
    }

    public List<CityDTO> findAllCities() {
        List<City> cities = cityRepository.findAll();
        List<CityDTO> cityDTOS =  cities.stream().map(c -> modelMapper.map(c,CityDTO.class)).toList();
        return cityDTOS;
    }

    public CityResponse search(CitySearchCriteria criteria) {
        Sort sortByAndOrder = criteria.getSortOrder().equalsIgnoreCase("asc") ? Sort.by(criteria.getSortBy()).ascending() : Sort.by(criteria.getSortBy()).descending();
        Pageable pageable = PageRequest.of(criteria.getPageNumber(),criteria.getPageSize(),sortByAndOrder);

        Specification<City> spec = Specification.where(CitySpecification.findByName(criteria.getName()))
                .and(CitySpecification.findByRegion(criteria.getRegionName()));


        Page<City> cityPage = cityRepository.findAll(spec,pageable);
        List<City> cities = cityPage.getContent();
        List<CityDTO> cityDTOList =  cities.stream().map(c -> modelMapper.map(c,CityDTO.class)).toList();
        CityResponse cityResponse = new CityResponse();
        cityResponse.setCities(cityDTOList);
        cityResponse.setPageNumber(cityPage.getNumber());
        cityResponse.setPageSize(cityPage.getSize());
        cityResponse.setTotalPages(cityPage.getTotalPages());
        cityResponse.setTotalElements(cityPage.getTotalElements());
        cityResponse.setLastPage(cityPage.isLast());
        return cityResponse;
    }

    public ByteArrayInputStream exportCitiesToExcel() throws IOException {
        List<CityDTO> cities = findAllCities();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Cities");

        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Name", "Region"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }

        int rowNum = 1;
        for (CityDTO city : cities) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(city.getId());
            row.createCell(1).setCellValue(city.getName());
            row.createCell(2).setCellValue(city.getRegion() != null ? city.getRegion().getName() : "-");
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    public void importCitiesFromExcel(MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();

            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String cityName = row.getCell(1).getStringCellValue();
                String regionName = row.getCell(2) != null ? row.getCell(2).getStringCellValue() : null;

                Optional<Region> regionOp = regionRepository.findByNameIgnoreCase(regionName);


                if (cityRepository.findByNameIgnoreCase(cityName).isPresent()) continue;
                if (regionOp.isEmpty()) continue;

                City city = new City();
                city.setName(cityName);
                city.setRegion(regionOp.get());
                city.setStatus(StatusType.ACTIVE);
                city.setCreatedAt(LocalDateTime.now());
                city.setCreatedBy(authService.getCurrentUser());

                cityRepository.save(city);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to import Excel file: " + e.getMessage());
        }
    }


//    protected <T> void uniqueOrThrow(Optional<T> existing, String entity, String field, Object value) {
//        if (existing.isPresent()) throw new DuplicateException("%s with %s '%s' already exists"
//                .formatted(entity, field, String.valueOf(value)));
//    }
//    validator.uniqueOrThrow(
//            regionRepository.findByNameIgnoreCase(dto.getName()),
//            "Region", "name", dto.getName()
//            );

}