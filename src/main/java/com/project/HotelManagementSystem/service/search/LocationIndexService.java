package com.project.HotelManagementSystem.service.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.project.HotelManagementSystem.config.AppConstants;
import com.project.HotelManagementSystem.dto.booking.LocationSearchDocument;
import com.project.HotelManagementSystem.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationIndexService {

    private final ElasticsearchClient client;

    public void indexHotel(Hotel hotel) throws Exception {
        Address a = hotel.getAddress();
        City c = a.getCity();
        Region r = c.getRegion();
        Country co = r.getCountry();

        LocationSearchDocument doc = new LocationSearchDocument();
        doc.setId("HOTEL_" + hotel.getId());
        doc.setType("HOTEL");
        doc.setName(hotel.getName());
        doc.setHotelId(hotel.getId());
        doc.setCityId(c.getId());
        doc.setRegionId(r.getId());
        doc.setCountryId(co.getId());
        doc.setRegionName(r.getName());
        doc.setCountryName(co.getName());

        client.index(i -> i
                .index(AppConstants.LOCATION_INDEX)
                .id(doc.getId())
                .document(doc)
        );
    }

    public void indexCity(City city) throws Exception {
        Region r = city.getRegion();
        Country co = r.getCountry();

        LocationSearchDocument doc = new LocationSearchDocument();
        doc.setId("CITY_" + city.getId());
        doc.setType("CITY");
        doc.setName(city.getName());
        doc.setCityId(city.getId());
        doc.setRegionId(r.getId());
        doc.setCountryId(co.getId());
        doc.setRegionName(r.getName());
        doc.setCountryName(co.getName());

        client.index(i -> i
                .index(AppConstants.LOCATION_INDEX)
                .id(doc.getId())
                .document(doc)
        );
    }

    public void indexRegion(Region region) throws Exception {
        Country co = region.getCountry();

        LocationSearchDocument doc = new LocationSearchDocument();
        doc.setId("REGION_" + region.getId());
        doc.setType("REGION");
        doc.setName(region.getName());
        doc.setRegionId(region.getId());
        doc.setCountryId(co.getId());
        doc.setCountryName(co.getName());

        client.index(i -> i
                .index(AppConstants.LOCATION_INDEX)
                .id(doc.getId())
                .document(doc)
        );
    }

    public void indexCountry(Country country) throws Exception {
        LocationSearchDocument doc = new LocationSearchDocument();
        doc.setId("COUNTRY_" + country.getId());
        doc.setType("COUNTRY");
        doc.setName(country.getName());
        doc.setCountryId(country.getId());

        client.index(i -> i
                .index(AppConstants.LOCATION_INDEX)
                .id(doc.getId())
                .document(doc)
        );
    }
}
