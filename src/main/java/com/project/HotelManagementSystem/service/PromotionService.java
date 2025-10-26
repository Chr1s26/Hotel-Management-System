package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.promotion.PromotionCreateDTO;
import com.project.HotelManagementSystem.dto.promotion.PromotionDTO;
import com.project.HotelManagementSystem.dto.promotion.PromotionResponse;
import com.project.HotelManagementSystem.dto.promotion.PromotionUpdateDTO;
import com.project.HotelManagementSystem.entity.Promotion;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final AuthService authService;
    private final ModelMapper modelMapper;

    public PromotionCreateDTO createPromotion(PromotionCreateDTO promotionCreateDTO) {
        Optional<Promotion> promotionOp = promotionRepository.findByCode(promotionCreateDTO.getCode());
//        if (promotionOp.isPresent()) {
//            throw new DuplicateException("Promotion with code " + promotionCreateDTO.getCode() + " already exists");
//        }
        Promotion promotion = modelMapper.map(promotionCreateDTO, Promotion.class);
        promotion.setStatus(StatusType.ACTIVE);
        promotion.setCreatedAt(LocalDateTime.now());
        promotion.setCreatedBy(authService.getCurrentUser());
        Promotion savedPromotion = this.promotionRepository.save(promotion);
        return modelMapper.map(savedPromotion,PromotionCreateDTO.class);
    }

    public PromotionUpdateDTO updatePromotion(Long id, PromotionUpdateDTO promotionUpdateDTO) {
        Optional<Promotion> promotionOptional = promotionRepository.findByCodeAndIdNot(promotionUpdateDTO.getCode(),promotionUpdateDTO.getId());
//        if (promotionOptional.isPresent() && !promotionOptional.get().getId().equals(id)) {
//            throw new DuplicateException("Promotion with code " + promotionUpdateDTO.getCode() + " already exists");
//        }
        Promotion promotion = modelMapper.map(promotionUpdateDTO, Promotion.class);
        Promotion promotionOp = promotionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Promotion","id",id));
        promotionOp.setCode(promotion.getCode());
        promotionOp.setDiscountType(promotion.getDiscountType());
        promotionOp.setDiscountAmount(promotion.getDiscountAmount());
        if(promotion.getStartDate() != null) {
            promotionOp.setStartDate(promotion.getStartDate());
        }if(promotion.getEndDate() != null) {
            promotionOp.setEndDate(promotion.getEndDate());
        }
        promotionOp.setPointAmount(promotion.getPointAmount());
        promotionOp.setUsageLimit(promotion.getUsageLimit());
        promotionOp.setTimesUsed(promotion.getTimesUsed());
        promotionOp.setUpdatedAt(LocalDateTime.now());
        promotionOp.setUpdatedBy(authService.getCurrentUser());
        promotionOp.setStatus(StatusType.ACTIVE);
        Promotion savedPromotion = this.promotionRepository.save(promotionOp);
        return modelMapper.map(savedPromotion,PromotionUpdateDTO.class);

    }

    public void deletePromotion(Long id) {
        Promotion promotion = promotionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Promotion", "id", id));
        promotionRepository.delete(promotion);
    }

    public PromotionUpdateDTO findPromotionById(Long id) {
        Promotion promotion = promotionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Promotion", "id", id));
        return modelMapper.map(promotion,PromotionUpdateDTO.class);
    }

    public PromotionResponse findAllPromotionsWithPagination(Integer pageNumber, Integer pageSize, String sortBy,String sortOrder) {
        Sort sortByAndSortOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndSortOrder);
        Page<Promotion> page = promotionRepository.findAll(pageable);
        List<PromotionDTO> promotionDTOList = page.getContent().stream().map(promotion -> modelMapper.map(promotion, PromotionDTO.class)).toList();
        PromotionResponse promotionResponse = new PromotionResponse();
        promotionResponse.setPromotions(promotionDTOList);
        promotionResponse.setPageNumber(page.getNumber());
        promotionResponse.setPageSize(page.getSize());
        promotionResponse.setTotalPages(page.getTotalPages());
        promotionResponse.setTotalElements(page.getTotalElements());
        promotionResponse.setLastPage(page.isLast());
        return promotionResponse;
     }

    public List<PromotionDTO> findAllPromotions() {
        List<Promotion>  promotions = promotionRepository.findAll();
        List<PromotionDTO> promotionDTOs = promotions.stream().map(promotion -> modelMapper.map(promotion, PromotionDTO.class)).collect(Collectors.toList());
        return promotionDTOs;
    }
}
