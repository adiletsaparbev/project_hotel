package com.example.it_proger.servise;

import com.example.it_proger.models.BookingStatus;
import com.example.it_proger.models.Image;
import com.example.it_proger.models.Room;
import com.example.it_proger.repo.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;


    public void saveRoom(Room room, MultipartFile file1,
                         MultipartFile file2, MultipartFile file3) throws IOException {
        // Проверка на существование комнаты с таким же номером
        if (roomRepository.findOptionalByRoomNumber(room.getRoomNumber()).isPresent()) {
            throw new IllegalArgumentException("Room with this number already exists.");
        }

        Image image1;
        Image image2;
        Image image3;

        if (file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            room.addImageRoom(image1);
        }
        if (file2.getSize() != 0) {
            image2 = toImageEntity(file2);
            room.addImageRoom(image2);
        }
        if (file3.getSize() != 0) {
            image3 = toImageEntity(file3);
            room.addImageRoom(image3);
        }

        log.info("Saving new Room. Number: {}; Type: {}", room.getRoomNumber(), room.getRoomType());
        Room roomFromOb = roomRepository.save(room);
        roomFromOb.setPreviewImageId(roomFromOb.getImages().get(0).getId());
        roomRepository.save(room);
    }

    private Image toImageEntity(MultipartFile file) throws IOException{
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    public List<Room> getAvailableRooms() {
        return roomRepository.findByAvailabilityTrue();
    }

    public List<String> getRoomTypes() {
        return roomRepository.findDistinctRoomType();
    }





}
