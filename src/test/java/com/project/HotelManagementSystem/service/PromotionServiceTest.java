package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.promotion.PromotionCreateDTO;
import com.project.HotelManagementSystem.dto.promotion.PromotionDTO;
import com.project.HotelManagementSystem.dto.promotion.PromotionResponse;
import com.project.HotelManagementSystem.dto.promotion.PromotionUpdateDTO;
import com.project.HotelManagementSystem.entity.Promotion;
import com.project.HotelManagementSystem.entity.constants.DiscountType;
import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.PromotionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PromotionServiceTest {

    @Mock
    private PromotionRepository promotionRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private PromotionService promotionService;

    @Test
    void testCreatePromotion_Success(){
        PromotionCreateDTO promotionCreateDTO = new PromotionCreateDTO(1L, "ABC123", DiscountType.FIXED_AMOUNT, 10.0, LocalDate.now(), LocalDate.now().plusDays(10), 100, 10, 0);

        Promotion promotion = new Promotion();;
        promotion.setCode("ABC123");
        promotion.setDiscountAmount(10.0);
        promotion.setStartDate(promotionCreateDTO.getStartDate());
        promotion.setEndDate(promotionCreateDTO.getEndDate());
        promotion.setPointAmount(100);
        promotion.setUsageLimit(10);
        promotion.setTimesUsed(0);

        Promotion savedPromotion = new Promotion();
        savedPromotion.setCode("ABC123");
        savedPromotion.setDiscountAmount(10.0);
        savedPromotion.setStartDate(promotionCreateDTO.getStartDate());
        savedPromotion.setEndDate(promotionCreateDTO.getEndDate());
        savedPromotion.setPointAmount(100);
        savedPromotion.setUsageLimit(10);
        savedPromotion.setTimesUsed(0);

        PromotionCreateDTO savedPromotionDTO = new PromotionCreateDTO(1L, "ABC123", DiscountType.FIXED_AMOUNT, 10.0, LocalDate.now(), LocalDate.now().plusDays(10), 100, 10, 0);

        when(promotionRepository.findByCode(promotionCreateDTO.getCode())).thenReturn(Optional.empty());
        when(modelMapper.map(promotionCreateDTO, Promotion.class)).thenReturn(promotion);
        when(promotionRepository.save(promotion)).thenReturn(savedPromotion);
        when(modelMapper.map(savedPromotion, PromotionCreateDTO.class)).thenReturn(savedPromotionDTO);

        PromotionCreateDTO result = promotionService.createPromotion(promotionCreateDTO);

        assertNotNull(result);
        assertEquals(promotionCreateDTO.getCode(), result.getCode());
    }

    @Test
    void testCreatePromotion_Duplicate(){
        PromotionCreateDTO promotionCreateDTO = new PromotionCreateDTO();
        promotionCreateDTO.setCode("ABC123");

        Promotion existing = new Promotion();
        existing.setCode("ABC123");

        when(promotionRepository.findByCode("ABC123")).thenReturn(Optional.of(existing));

        Assertions.assertThrows(DuplicateException.class, () -> promotionService.createPromotion(promotionCreateDTO));
    }

    @Test
    void testUpdatePromotion_Success(){
        PromotionUpdateDTO promotionUpdateDTO = new PromotionUpdateDTO();
        promotionUpdateDTO.setId(1L);
        promotionUpdateDTO.setCode("ABC123");
        promotionUpdateDTO.setDiscountAmount(15.0);
        promotionUpdateDTO.setStartDate(LocalDate.now());
        promotionUpdateDTO.setEndDate(LocalDate.now().plusDays(5));
        promotionUpdateDTO.setPointAmount(200);
        promotionUpdateDTO.setUsageLimit(5);
        promotionUpdateDTO.setTimesUsed(1);

        Promotion mappedPromotion = new Promotion();
        mappedPromotion.setId(1L);
        mappedPromotion.setCode("ABC123");
        mappedPromotion.setDiscountAmount(15.0);
        mappedPromotion.setStartDate(LocalDate.now());
        mappedPromotion.setEndDate(LocalDate.now().plusDays(5));
        mappedPromotion.setPointAmount(200);
        mappedPromotion.setUsageLimit(5);
        mappedPromotion.setTimesUsed(1);

        Promotion existing = new Promotion();
        existing.setId(1L);
        existing.setCode("ABC123");
        existing.setDiscountAmount(15.0);
        existing.setStartDate(LocalDate.now());
        existing.setEndDate(LocalDate.now().plusDays(5));
        existing.setPointAmount(200);
        existing.setUsageLimit(5);
        existing.setTimesUsed(1);

        Promotion savedPromotion = new Promotion();
        savedPromotion.setId(1L);
        savedPromotion.setCode("ABC123");
        savedPromotion.setDiscountAmount(15.0);
        savedPromotion.setStartDate(LocalDate.now());
        savedPromotion.setEndDate(LocalDate.now().plusDays(5));
        savedPromotion.setPointAmount(200);
        savedPromotion.setUsageLimit(5);
        savedPromotion.setTimesUsed(1);

        when(promotionRepository.findByCode("ABC123")).thenReturn(Optional.empty());
        when(modelMapper.map(promotionUpdateDTO,Promotion.class)).thenReturn(mappedPromotion);
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(promotionRepository.save(existing)).thenReturn(savedPromotion);
        when(modelMapper.map(savedPromotion,PromotionUpdateDTO.class)).thenReturn(promotionUpdateDTO);

        PromotionUpdateDTO result = promotionService.updatePromotion(1L,promotionUpdateDTO);

        assertNotNull(result);
        assertEquals(promotionUpdateDTO.getCode(), result.getCode());
    }

    @Test
    void testUpdatePromotion_Duplicate(){
        PromotionUpdateDTO promotionUpdateDTO = new PromotionUpdateDTO();
        promotionUpdateDTO.setId(1L);
        promotionUpdateDTO.setCode("ABC123");

        Promotion existing = new Promotion();
        existing.setId(2L);
        existing.setCode("ABC123");

        when(promotionRepository.findByCode("ABC123")).thenReturn(Optional.of(existing));
        Assertions.assertThrows(DuplicateException.class, () -> promotionService.updatePromotion(1L,promotionUpdateDTO));
    }

    @Test
    void testUpdatePromotion_NotFound(){
        PromotionUpdateDTO promotionUpdateDTO = new PromotionUpdateDTO();
        promotionUpdateDTO.setId(1L);
        promotionUpdateDTO.setCode("ABC123");

        when(promotionRepository.findByCode("ABC123")).thenReturn(Optional.empty());
        when(promotionRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,() -> promotionService.updatePromotion(1L,promotionUpdateDTO));

    }

    @Test
    void testDeletePromotion_Success(){
        Promotion existing = new Promotion();
        existing.setCode("ABC123");
        existing.setDiscountType(DiscountType.FIXED_AMOUNT);
        existing.setDiscountAmount(15.0);
        existing.setStartDate(LocalDate.now());
        existing.setEndDate(LocalDate.now().plusDays(5));
        existing.setPointAmount(200);
        existing.setUsageLimit(5);
        existing.setTimesUsed(1);

        when(promotionRepository.findById(1L)).thenReturn(Optional.of(existing));

        promotionService.deletePromotion(1L);

        verify(promotionRepository).delete(existing);
    }

    @Test
    void testDeletePromotion_NotFound(){
        when(promotionRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,() -> promotionService.deletePromotion(1L));
    }

    @Test
    void testFindPromotionById_Success(){
        Promotion existing = new Promotion();
        existing.setId(1L);
        existing.setCode("ABC123");

        PromotionUpdateDTO promotionUpdateDTO = new PromotionUpdateDTO();
        promotionUpdateDTO.setId(1L);
        promotionUpdateDTO.setCode("ABC123");

        when(promotionRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(modelMapper.map(existing,PromotionUpdateDTO.class)).thenReturn(promotionUpdateDTO);

        PromotionUpdateDTO result = promotionService.findPromotionById(1L);

        assertNotNull(result);
        assertEquals(promotionUpdateDTO.getCode(), result.getCode());
    }

    @Test
    void testFindAllPromotions_Success(){
        List<Promotion> promotions = Arrays.asList(new Promotion(), new Promotion());

        when(promotionRepository.findAll()).thenReturn(promotions);
        when(modelMapper.map(any(Promotion.class),eq(PromotionDTO.class))).thenReturn(new PromotionDTO());

        List<PromotionDTO> result = promotionService.findAllPromotions();
        assertNotNull(result);
        assertEquals(promotions.size(), result.size());
    }

    @Test
    void testFindAllPromotionsWithPagination_Success(){
        List<Promotion> promotions = Arrays.asList(new Promotion(), new Promotion());
        Page<Promotion> page = new PageImpl<>(promotions);

        when(promotionRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(modelMapper.map(any(Promotion.class),eq(PromotionDTO.class))).thenReturn(new PromotionDTO());

        PromotionResponse response = promotionService.findAllPromotionsWithPagination(0,10,"id","asc");
        assertNotNull(response);
        assertEquals(promotions.size(), response.getPromotions().size());
    }
}
