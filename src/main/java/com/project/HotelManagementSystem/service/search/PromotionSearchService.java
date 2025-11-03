package com.project.HotelManagementSystem.service.search;

import com.project.HotelManagementSystem.dto.searchFilter.promotion.PromotionSearchQuery;
import com.project.HotelManagementSystem.entity.Promotion;
import com.project.HotelManagementSystem.entity.specification.PromotionSpecification;
import com.project.HotelManagementSystem.repository.PromotionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PromotionSearchService {
    private final CommonSearchService commonSearchService;
    private final PromotionRepository promotionRepository;

    public Page<Promotion> searchByQuery(PromotionSearchQuery query){
        return commonSearchService.searchByQuery(promotionRepository, PromotionSpecification::fromFilter, query);
    }

    public List<Promotion> searchByQueryAll(PromotionSearchQuery query){
        return commonSearchService.searchByQueryAll(promotionRepository, PromotionSpecification::fromFilter, query);
    }
}
