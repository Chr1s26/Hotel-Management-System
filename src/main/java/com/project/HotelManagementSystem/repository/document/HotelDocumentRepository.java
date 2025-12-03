package com.project.HotelManagementSystem.repository.document;

import com.project.HotelManagementSystem.entity.document.Hotel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HotelDocumentRepository extends MongoRepository<Hotel, String> {
}
