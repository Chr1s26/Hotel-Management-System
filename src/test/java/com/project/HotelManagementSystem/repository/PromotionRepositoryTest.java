package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Promotion;
import com.project.HotelManagementSystem.entity.constants.DiscountType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PromotionRepositoryTest {

    @Autowired
    private PromotionRepository promotionRepository;

    private Promotion createPromotion(String code) {
        Promotion promotion = new Promotion();
        promotion.setCode(code);
        promotion.setDiscountType(DiscountType.FIXED_AMOUNT);
        promotion.setDiscountAmount(50);
        promotion.setStartDate(LocalDate.now());
        promotion.setEndDate(LocalDate.now().plusDays(10));
        promotion.setPointAmount(100);
        promotion.setUsageLimit(5);
        promotion.setTimesUsed(0);
        return promotion;
    }

    @Test
    public void savePromotion_ReturnSavedPromotion() {
        Promotion promo = createPromotion("PROMO50");
        Promotion saved = promotionRepository.save(promo);

        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void updatePromotion_ReturnUpdatedPromotion() {
        Promotion promo = createPromotion("PROMO50");
        promotionRepository.save(promo);

        promo.setCode("PROMO100");
        promo.setDiscountAmount(100);

        Promotion updated = promotionRepository.save(promo);

        Assertions.assertThat(updated.getCode()).isEqualTo("PROMO100");
        Assertions.assertThat(updated.getDiscountAmount()).isEqualTo(100);
    }

    @Test
    public void deletePromotionById_ReturnEmptyOptional() {
        Promotion promo = createPromotion("DELETE_ME");
        promotionRepository.save(promo);

        promotionRepository.deleteById(promo.getId());
        Optional<Promotion> result = promotionRepository.findById(promo.getId());

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void findAllPromotions_ReturnListOfPromotions() {
        promotionRepository.save(createPromotion("P1"));
        promotionRepository.save(createPromotion("P2"));

        List<Promotion> promotions = promotionRepository.findAll();

        Assertions.assertThat(promotions).isNotNull();
        Assertions.assertThat(promotions.size()).isEqualTo(2);
    }

    @Test
    public void findPromotionById_ReturnPromotion() {
        Promotion promo = createPromotion("FIND_ME");
        promotionRepository.save(promo);

        Optional<Promotion> found = promotionRepository.findById(promo.getId());

        Assertions.assertThat(found).isPresent();
        Assertions.assertThat(found.get().getCode()).isEqualTo("FIND_ME");
    }

    @Test
    public void findAllWithPagination_ReturnPagedResult() {
        promotionRepository.save(createPromotion("PAGE1"));
        promotionRepository.save(createPromotion("PAGE2"));
        promotionRepository.save(createPromotion("PAGE3"));

        Page<Promotion> page = promotionRepository.findAll(PageRequest.of(0, 2));

        Assertions.assertThat(page.getContent().size()).isEqualTo(2);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(3);
    }
}
