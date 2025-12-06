package com.project.HotelManagementSystem.scheduler;

import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import com.project.HotelManagementSystem.dto.searchFilter.customer.CustomerSearchField;
import com.project.HotelManagementSystem.dto.searchFilter.customer.CustomerSearchFilter;
import com.project.HotelManagementSystem.dto.searchFilter.customer.CustomerSearchQuery;
import com.project.HotelManagementSystem.service.excelExport.CustomerExportProcess;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyReportScheduler {
    private final CustomerExportProcess customerExportProcess;

    @Async("schedulerExecutor")
    @Scheduled(fixedRate = 150000000)
    public void customerReport(){
//        log.info("Report Export Customer Report {}", LocalDateTime.now());
//        CustomerSearchQuery customerSearchQuery = new CustomerSearchQuery();
//        customerSearchQuery.setPageNumber(0);
//        customerSearchQuery.setPageSize(6);
//        customerSearchQuery.setSortBy("createdAt");
//        customerSearchQuery.setSortDirection(SortDirection.DESC);
//        customerSearchQuery.setSortDirection(SortDirection.DESC);
//        customerSearchQuery.setFilterList(List.of(
//                new CustomerSearchFilter(CustomerSearchField.NAME, MatchType.CONTAINS, ""),
//                new CustomerSearchFilter(CustomerSearchField.PHONE, MatchType.CONTAINS, ""),
//                new CustomerSearchFilter(CustomerSearchField.NATIONALITY, MatchType.CONTAINS, ""),
//                new CustomerSearchFilter(CustomerSearchField.DATE_OF_BIRTH, MatchType.EXACT,""),
//                new CustomerSearchFilter(CustomerSearchField.VIP_STATUS, MatchType.EXACT, ""),
//                new CustomerSearchFilter(CustomerSearchField.STATUS, MatchType.EXACT, "")
//        ));
//        customerExportProcess.generateExportFileAndSendToAdmin(customerSearchQuery);
    }
}
