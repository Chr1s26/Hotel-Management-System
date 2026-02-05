package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Address;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.PropertyDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {
    @Query("SELECT h FROM Hotel h WHERE LOWER(h.name) = LOWER(:name) AND h.address.latitude = :latitude AND h.address.longitude = :longitude")
    Optional<Hotel> findHotelByNameAndCoordinates(String name, double latitude, double longitude);

    @Query("SELECT h FROM Hotel h WHERE LOWER(h.name) = LOWER(:name) AND h.address.latitude = :latitude AND h.address.longitude = :longitude AND h.id <> :id")
    Optional<Hotel> findHotelByNameAndCoordinatesAndIdNot(String name, double latitude, double longitude, Long id);

    boolean existsByAddress(Address address);
    boolean existsByPropertyDescription(PropertyDescription propertyDescription);

    Optional<Hotel> findHotelByAddressAndIdNot(Address address,Long id);
    boolean existsByAddressAndIdNot(Address address, Long id);
    boolean existsByPropertyDescriptionAndIdNot(PropertyDescription propertyDescription, Long id);

    @Query("""
        SELECT DISTINCT h FROM Hotel h
        JOIN h.address a
        JOIN a.city c
        JOIN c.region r
        JOIN r.country co
        WHERE (
            :keyword IS NULL OR :keyword = '' OR
            LOWER(h.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(co.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    """)
    List<Hotel> searchByKeyword(String keyword);

    @Query("""
    SELECT DISTINCT h FROM Hotel h
    JOIN h.address a
    JOIN a.city c
    WHERE c.id = :cityId
""")
    List<Hotel> findByCityId(Long cityId);

    @Query("""
    SELECT DISTINCT h FROM Hotel h
    JOIN h.address a
    JOIN a.city c
    JOIN c.region r
    WHERE r.id = :regionId
""")
    List<Hotel> findByRegionId(Long regionId);

    @Query("""
    SELECT DISTINCT h FROM Hotel h
    JOIN h.address a
    JOIN a.city c
    JOIN c.region r
    JOIN r.country co
    WHERE co.id = :countryId
""")
    List<Hotel> findByCountryId(Long countryId);

    @Query("""
    SELECT DISTINCT h FROM Hotel h
    WHERE h.id = :hotelId
       OR h.address.city.id = (
            SELECT a.city.id FROM Hotel h2
            JOIN h2.address a
            WHERE h2.id = :hotelId
       )
""")
    List<Hotel> findHotelAndNearby(Long hotelId);

    @Query("""
    SELECT DISTINCT h FROM Hotel h
    JOIN h.address a
    JOIN a.city c
    JOIN c.region r
    WHERE r.id = (
        SELECT c2.region.id FROM Hotel h2
        JOIN h2.address a2
        JOIN a2.city c2
        WHERE h2.id IN :hotelIds
    )
""")
    List<Hotel> findHotelsInSameRegion(List<Long> hotelIds);

    @Query("""
    select h from Hotel h
    left join fetch h.policies
    where h.id = :hotelId
""")
    Optional<Hotel> findByIdWithPolicies(Long hotelId);

}
