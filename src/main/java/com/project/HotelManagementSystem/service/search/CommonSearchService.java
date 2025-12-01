package com.project.HotelManagementSystem.service.search;

import com.project.HotelManagementSystem.dto.searchFilter.SearchQuery;
import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import com.project.HotelManagementSystem.dto.searchFilter.country.CountrySearchFilter;
import com.project.HotelManagementSystem.dto.searchFilter.country.CountrySearchQuery;
import com.project.HotelManagementSystem.entity.Country;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.specification.CountrySpecification;
import com.project.HotelManagementSystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CommonSearchService {
    public final AuthService authService;

    public <E, F> Page<E> searchByQuery(
            JpaSpecificationExecutor<E> repository,
            Function<F, Specification<E>> specFunction,
            SearchQuery<F> query
    ) {
        int page = (query.getPageNumber() == null || query.getPageNumber() < 0) ? 0 : query.getPageNumber();
        int size = (query.getPageSize() == null || query.getPageSize() < 1) ? 10 : query.getPageSize();
        String sortBy = (query.getSortBy() == null || query.getSortBy().isBlank()) ? "createdAt" : query.getSortBy();
        Sort.Direction dir = (query.getSortDirection() == null || query.getSortDirection() == SortDirection.DESC)
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));

        Specification<E> spec = Specification.where(null);
        if (query.getFilterList() != null) {
            for (F filter : query.getFilterList()) {
                Specification<E> s = specFunction.apply(filter);
                if (s != null) spec = (spec == null) ? Specification.where(s) : spec.and(s);
            }
        }
        spec = createdByUserFilter(spec);
        return repository.findAll(spec, pageable);
    }

    public <E, F> List<E> searchByQueryAll(
            JpaSpecificationExecutor<E> repository,
            Function<F, Specification<E>> specFunction,
            SearchQuery<F> query
    ) {
        Specification<E> spec = buildSpecification(query, specFunction);
        spec = createdByUserFilter(spec);
        return repository.findAll(spec);
    }

    private <E> Specification<E> createdByUserFilter(Specification<E> spec){
        User currentUser = authService.getCurrentUser();
        Long currentUserId = currentUser.getId();
        boolean isAdmin = authService.getCurrentUserRole().equalsIgnoreCase("ROLE_ADMIN");
        if(!isAdmin){
            return spec;
        }
        Specification<E> createdBySpec = (root, query, cb) -> cb.equal(root.get("createdBy").get("id"), currentUserId);
        if(spec == null){
            return Specification.where(createdBySpec);
        }
        return spec.and(createdBySpec);
    }

    private <E, F> Specification<E> buildSpecification(
            SearchQuery<F> query,
            Function<F, Specification<E>> specFunction
    ) {
        Specification<E> spec = Specification.where(null);

        if (query.getFilterList() != null) {
            for (F filter : query.getFilterList()) {
                Specification<E> s = specFunction.apply(filter);
                if (s != null) spec = (spec == null) ? Specification.where(s) : spec.and(s);
            }
        }

        return spec;
    }
}