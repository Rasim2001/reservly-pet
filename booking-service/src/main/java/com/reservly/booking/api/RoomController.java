package com.reservly.booking.api;

import com.reservly.booking.dto.CreateRoomRequest;
import com.reservly.booking.dto.RoomResponse;
import com.reservly.booking.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomResponse createRoom(
            @Valid
            @RequestBody
            CreateRoomRequest roomRequest) {

        RoomResponse response = roomService.create(roomRequest);

        log.info("Created room with id={}", response.id());

        return response;
    }

    @GetMapping("/{id}")
    public RoomResponse getById(@PathVariable Long id) {
        RoomResponse response = roomService.getById(id);

        log.info("Get by id={}", id);

        return response;
    }
}
