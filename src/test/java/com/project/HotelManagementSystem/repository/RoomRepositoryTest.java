package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.Room;
import com.project.HotelManagementSystem.entity.constants.HotelType;
import com.project.HotelManagementSystem.entity.constants.RoomType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    private Hotel hotel;

    @BeforeEach
    public void setup() {
        hotel = new Hotel();
        hotel.setName("Hotel");
        hotel.setPhoneNumber("2345235");
        hotel.setEmail("hotel33@gmail.com");
        hotel.setDescription("Hotel Description");
        hotel.setRating(1.3);
        hotel.setHotelType(HotelType.APARTMENT);
        Hotel savedHotel = hotelRepository.save(hotel);
    }

    public Room createRoom(String description) {
        Room room = new Room();
        room.setDescription(description);
        room.setPrice(122);
        room.setAvailable(true);
        room.setRoomType(RoomType.DELUXE);
        room.setMaxCapacity(2);
        room.setHotel(hotel);
        return room;
    }

    @Test
    public void saveRoom_ReturnSavedRoom(){
        Room room = createRoom("Room 1");
        Room savedRoom = roomRepository.save(room);

        Assertions.assertThat(savedRoom.getId()).isNotNull();
        Assertions.assertThat(savedRoom.getId()).isGreaterThan(0);
        Assertions.assertThat(savedRoom.getRoomType()).isEqualTo(room.getRoomType());
    }

    @Test
    public void updateRoom_ReturnUpdatedRoom(){
        Room room = createRoom("Room 1");
        Room savedRoom = roomRepository.save(room);

        savedRoom.setPrice(200);
        savedRoom.setDescription("Updated Description");
        Room updatedRoom = roomRepository.save(savedRoom);

        Assertions.assertThat(updatedRoom.getPrice()).isEqualTo(200);
        Assertions.assertThat(updatedRoom.getDescription()).isEqualTo("Updated Description");
        Assertions.assertThat(updatedRoom).isNotNull();
    }

    @Test
    public void deleteRoomById_ReturnRoomIsEmpty(){
        Room room = createRoom("Room 1");
        Room savedRoom = roomRepository.save(room);

        roomRepository.deleteById(savedRoom.getId());

        Optional<Room> optionalRoom = roomRepository.findById(savedRoom.getId());

        Assertions.assertThat(optionalRoom).isNotPresent();
    }

    @Test
    public void findAllRooms_ReturnAllRooms(){
        Room room1 = createRoom("Room 1");
        Room room2 = createRoom("Room 2");
        roomRepository.save(room1);
        roomRepository.save(room2);

        List<Room> rooms = roomRepository.findAll();

        Assertions.assertThat(rooms).isNotNull();
        Assertions.assertThat(rooms.size()).isEqualTo(2);
    }

    @Test
    public void findRoomById_ReturnRoom(){
        Room room = createRoom("Room 1");
        Room savedRoom = roomRepository.save(room);

        Optional<Room> found = roomRepository.findById(savedRoom.getId());

        Assertions.assertThat(found).isPresent();
        Assertions.assertThat(found.get().getPrice()).isEqualTo(room.getPrice());

    }

    @Test
    public void findAllWithPagination_ReturnPageRooms(){
        Room room1 = createRoom("Room 1");
        Room room2 = createRoom("Room 2");
        Room room3 = createRoom("Room 3");

        roomRepository.save(room1);
        roomRepository.save(room2);
        roomRepository.save(room3);

        Sort sortByAndSortOrder = Sort.by("id").ascending();
        Page<Room> page = roomRepository.findAll(PageRequest.of(0, 2, sortByAndSortOrder));

        Assertions.assertThat(page).isNotNull();
        Assertions.assertThat(page.getTotalElements()).isEqualTo(3);
        Assertions.assertThat(page.getContent().size()).isEqualTo(2);
    }
}
