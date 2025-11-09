package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.dto.hotel.HotelCreateDTO;
import com.project.HotelManagementSystem.dto.hotel.HotelDTO;
import com.project.HotelManagementSystem.dto.hotel.HotelUpdateDTO;
import com.project.HotelManagementSystem.dto.searchFilter.MatchType;
import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import com.project.HotelManagementSystem.dto.searchFilter.hotel.HotelSearchField;
import com.project.HotelManagementSystem.dto.searchFilter.hotel.HotelSearchFilter;
import com.project.HotelManagementSystem.dto.searchFilter.hotel.HotelSearchQuery;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.constants.HotelMediaType;
import com.project.HotelManagementSystem.service.*;
import com.project.HotelManagementSystem.service.excelExport.HotelExportProcess;
import com.project.HotelManagementSystem.service.search.HotelSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/hotels")
public class HotelController {

    private final HotelService hotelService;
    private final AddressService addressService;
    private final PropertyDescriptionService propertyDescriptionService;
    private final PromotionService promotionService;
    private final PolicyService policyService;
    private final HotelSearchService hotelSearchService;
    private final HotelExportProcess hotelExportProcess;
    private final FileService fileService;

    @ModelAttribute("query")
    public HotelSearchQuery initQuery() {
        HotelSearchQuery query = new HotelSearchQuery();
        query.setPageNumber(0);
        query.setPageSize(6);
        query.setSortBy("createdAt");
        query.setSortDirection(SortDirection.DESC);
        query.setFilterList(List.of(
                new HotelSearchFilter(HotelSearchField.NAME, MatchType.CONTAINS,""),
                new HotelSearchFilter(HotelSearchField.PHONE, MatchType.CONTAINS, ""),
                new HotelSearchFilter(HotelSearchField.EMAIL, MatchType.CONTAINS, ""),
                new HotelSearchFilter(HotelSearchField.STATUS, MatchType.EXACT, ""),
                new HotelSearchFilter(HotelSearchField.RATING, MatchType.EXACT, ""),
                new HotelSearchFilter(HotelSearchField.HOTEL_TYPE, MatchType.EXACT, "")
        ));
        return query;
    }

    @GetMapping
    public String getAllHotels(Model model, @ModelAttribute("query") HotelSearchQuery query) {
        Page<Hotel> page = this.hotelSearchService.searchByQuery(query);
        model.addAttribute("hotels", page.getContent());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalElements",page.getTotalElements());
        return "hotels/listing";
    }

    @PostMapping
    public String searchHotels(Model model, @ModelAttribute("query") HotelSearchQuery query) {
        Page<Hotel> hotels = this.hotelSearchService.searchByQuery(query);
        model.addAttribute("hotels", hotels.getContent());
        model.addAttribute("totalPages",hotels.getTotalPages());
        model.addAttribute("totalElements",hotels.getTotalElements());
        return "hotels/listing";
    }

    @GetMapping("/new")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showCreateForm(Model model) {
        model.addAttribute("hotel", new HotelCreateDTO());
        model.addAttribute("addresses", this.addressService.findAllAddress());
        model.addAttribute("propertyDescriptions", this.propertyDescriptionService.findAllPropertyDescriptions());
        model.addAttribute("promotions", this.promotionService.findAllPromotions());
        model.addAttribute("policies", this.policyService.findAllPolicies());
        return "hotels/create";
    }

    @PostMapping("/create")
    public String createHotel(@Valid @ModelAttribute("hotel") HotelCreateDTO hotelCreateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("addresses", this.addressService.findAllAddress());
            model.addAttribute("propertyDescriptions", this.propertyDescriptionService.findAllPropertyDescriptions());
            model.addAttribute("promotions", this.promotionService.findAllPromotions());
            model.addAttribute("policies", this.policyService.findAllPolicies());
            return "hotels/create";
        }
        this.hotelService.createHotel(hotelCreateDTO);
        return "redirect:/hotels";
    }

    @GetMapping("/edit/{id}")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String showEditForm(@PathVariable Long id,Model model) {
        model.addAttribute("hotel", this.hotelService.findHotelById(id));
        model.addAttribute("addresses", this.addressService.findAllAddress());
        model.addAttribute("propertyDescriptions", this.propertyDescriptionService.findAllPropertyDescriptions());
        model.addAttribute("promotions", this.promotionService.findAllPromotions());
        model.addAttribute("policies", this.policyService.findAllPolicies());
        return "hotels/edit";
    }

    @PostMapping("/update/{id}")
    public String updateHotel(@PathVariable Long id,@Valid @ModelAttribute("hotel") HotelUpdateDTO hotelUpdateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("addresses", this.addressService.findAllAddress());
            model.addAttribute("propertyDescriptions", this.propertyDescriptionService.findAllPropertyDescriptions());
            model.addAttribute("promotions", this.promotionService.findAllPromotions());
            model.addAttribute("policies", this.policyService.findAllPolicies());
            return "hotels/edit";
        }
        this.hotelService.updateHotel(id, hotelUpdateDTO);
        return "redirect:/hotels";
    }

    @GetMapping("/delete/{id}")
    @ActiveRole({"ADMIN", "EDITOR"})
    public String deleteHotel(@PathVariable Long id) {
        this.hotelService.deleteHotel(id);
        return "redirect:/hotels";
    }

    @PostMapping("/export/excel")
    public String exportExcelToS3(Model model, @ModelAttribute("query") HotelSearchQuery query) {
        hotelExportProcess.generateExportFile(query);
        return "redirect:/hotels";
    }

    @GetMapping("/view/{id}")
    public String showView(@PathVariable Long id, Model model) {
        HotelDTO hotel = this.hotelService.findById(id);
        model.addAttribute("hotel", hotel);
        model.addAttribute("address", hotel.getAddress());
        model.addAttribute("profilePhotos", hotelService.getHotelPhotos(id, HotelMediaType.PROFILE));
        model.addAttribute("coverPhotos", hotelService.getHotelPhotos(id, HotelMediaType.COVER_PHOTO));
        model.addAttribute("servicePhotos", hotelService.getHotelPhotos(id, HotelMediaType.SERVICE));
        model.addAttribute("otherPhotos", hotelService.getHotelPhotos(id, HotelMediaType.OTHER));
        return "hotels/view";
    }

    @PostMapping("/photos/delete/{id}")
    public String deletePhotos(@PathVariable Long id,
                               @RequestParam(value="selectedPhotos",required = false) List<Long> photoIds) {

        if (photoIds == null || photoIds.isEmpty()) {
            return "redirect:/hotels/view/" + id + "?error=NoPhotosSelected";
        }

        for (Long photoId : photoIds) {
            hotelService.deleteHotelAttachmentAndFiles(photoId);
        }

        return "redirect:/hotels/view/" + id + "?success=PhotosDeleted";
    }

    @PostMapping("/photos/add/{id}")
    public String addPhotos(@PathVariable Long id,
                            @RequestParam("files")List<MultipartFile> files,
                            @RequestParam("mediaType") HotelMediaType mediaType) {
        if(files == null || files.isEmpty()) {
            return "redirect:/hotels/view/" + id + "?error=NoPhotosSelected";
        }
        hotelService.addAttachment(files, id, mediaType);
        return "redirect:/hotels/view/" + id + "?success=PhotosAdded";
    }


}
