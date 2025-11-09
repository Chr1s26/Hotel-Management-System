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
        if (promotionOp.isPresent()) {
            throw new DuplicateException("promotion",promotionCreateDTO,"code","promotions/create","A promotion with this code already exists");
        }
        Promotion promotion = modelMapper.map(promotionCreateDTO, Promotion.class);
        promotion.setStatus(StatusType.ACTIVE);
        promotion.setCreatedAt(LocalDateTime.now());
        promotion.setCreatedBy(authService.getCurrentUser());
        Promotion savedPromotion = this.promotionRepository.save(promotion);
        return modelMapper.map(savedPromotion,PromotionCreateDTO.class);
    }

    public PromotionUpdateDTO updatePromotion(Long id, PromotionUpdateDTO promotionUpdateDTO) {
        Optional<Promotion> promotionOptional = promotionRepository.findByCodeAndIdNot(promotionUpdateDTO.getCode(),promotionUpdateDTO.getId());
        if (promotionOptional.isPresent() && !promotionOptional.get().getId().equals(id)) {
            throw new DuplicateException("promotion",promotionUpdateDTO,"code","promotions/edit","A promotion with this code already exists");
        }
        Promotion promotion = modelMapper.map(promotionUpdateDTO, Promotion.class);
        Promotion promotionOp = promotionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("promotion",promotionUpdateDTO,"id","promotions/edit","A promotion with this id cannot be found"));
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
        Optional<Promotion> promotionOp = promotionRepository.findById(id);
        if(promotionOp.isEmpty()){
            throw new ResourceNotFoundException("promotion",promotionOp,"id","promotions","A promotion with this id cannot be found");
        }
        promotionRepository.delete(promotionOp.get());
    }

    public PromotionUpdateDTO findPromotionById(Long id) {
        Optional<Promotion> promotionOp = promotionRepository.findById(id);
        if(promotionOp.isEmpty()){
            throw new ResourceNotFoundException("promotion",promotionOp,"id","promotions","A promotion with this id cannot be found");
        }
        return modelMapper.map(promotionOp,PromotionUpdateDTO.class);
    }

    public PromotionDTO findById(Long id) {
        Optional<Promotion> promotionOp = promotionRepository.findById(id);
        if(promotionOp.isEmpty()){
            throw new ResourceNotFoundException("promotion",promotionOp,"id","promotions","A promotion with this id cannot be found");
        }
        return toDTO(promotionOp.get());
    }

    private PromotionDTO toDTO(Promotion promotion) {
        PromotionDTO promotionDTO = new PromotionDTO();
        promotionDTO.setId(promotion.getId());
        promotionDTO.setCode(promotion.getCode());
        promotionDTO.setDiscountType(promotion.getDiscountType());
        promotionDTO.setDiscountAmount(promotion.getDiscountAmount());
        promotionDTO.setStartDate(promotion.getStartDate());
        promotionDTO.setEndDate(promotion.getEndDate());
        promotionDTO.setPointAmount(promotion.getPointAmount());
        promotionDTO.setUsageLimit(promotion.getUsageLimit());
        promotionDTO.setTimesUsed(promotion.getTimesUsed());
        promotionDTO.setCreatedBy(promotion.getCreatedBy());
        promotionDTO.setUpdatedBy(promotion.getUpdatedBy());
        promotionDTO.setUpdatedAt(promotion.getUpdatedAt());
        promotionDTO.setUpdatedBy(promotion.getUpdatedBy());
        promotionDTO.setStatus(promotion.getStatus());
        return promotionDTO;
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
