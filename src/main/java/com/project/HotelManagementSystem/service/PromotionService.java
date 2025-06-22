package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.promotion.PromotionCreateDTO;
import com.project.HotelManagementSystem.dto.promotion.PromotionDTO;
import com.project.HotelManagementSystem.dto.promotion.PromotionResponse;
import com.project.HotelManagementSystem.dto.promotion.PromotionUpdateDTO;
import com.project.HotelManagementSystem.entity.Promotion;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PromotionCreateDTO createPromotion(PromotionCreateDTO promotionCreateDTO) {
        Promotion promotion = modelMapper.map(promotionCreateDTO, Promotion.class);
        Promotion savedPromotion = this.promotionRepository.save(promotion);
        return modelMapper.map(savedPromotion,PromotionCreateDTO.class);
    }

    public PromotionUpdateDTO updatePromotion(Long id, PromotionUpdateDTO promotionUpdateDTO) {
        Promotion promotion = modelMapper.map(promotionUpdateDTO, Promotion.class);
        Optional<Promotion> promotionOp = promotionRepository.findById(id);
        if(promotionOp.isPresent()) {
            Promotion updatedPromotion = promotionOp.get();
            updatedPromotion.setCode(promotion.getCode());
            updatedPromotion.setDiscountType(promotion.getDiscountType());
            updatedPromotion.setDiscountAmount(promotion.getDiscountAmount());
            if(promotion.getStartDate() != null) {
                updatedPromotion.setStartDate(promotion.getStartDate());
            }
            if(promotion.getEndDate() != null) {
                updatedPromotion.setEndDate(promotion.getEndDate());
            }
            updatedPromotion.setPointAmount(promotion.getPointAmount());
            updatedPromotion.setUsageLimit(promotion.getUsageLimit());
            updatedPromotion.setTimesUsed(promotion.getTimesUsed());
            Promotion savedPromotion = this.promotionRepository.save(updatedPromotion);
            return modelMapper.map(savedPromotion,PromotionUpdateDTO.class);
        }
        return null;
    }

    public void deletePromotion(Long id) {
        Optional<Promotion> promotionOp = promotionRepository.findById(id);
        if(promotionOp.isPresent()) {
            promotionRepository.deleteById(id);
        }
    }

    public PromotionDTO findPromotionById(Long id) {
        Promotion promotion = promotionRepository.findById(id).get();
        return modelMapper.map(promotion,PromotionDTO.class);
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
