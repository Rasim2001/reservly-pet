package com.reservly.booking.api;

import com.reservly.booking.dto.room.CreateRoomRequest;
import com.reservly.booking.dto.room.RoomResponse;
import com.reservly.booking.dto.room.UpdateRoomRequest;
import com.reservly.booking.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping
    public Page<RoomResponse> getList(@PageableDefault(size = 20) Pageable pageable) {
        log.info("Get list");

        return roomService.list(pageable);
    }

    @PutMapping("/{id}")
    public RoomResponse updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoomRequest roomRequest) {

        log.info("Update room with id={}", id);

        return roomService.updateRoom(id, roomRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@PathVariable Long id) {
        log.info("delete room with id={}", id);

        roomService.deleteRoom(id);
    }
}
