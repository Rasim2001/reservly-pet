package com.reservly.booking.service;

import com.reservly.booking.domain.RoomEntity;
import com.reservly.booking.domain.RoomMapper;
import com.reservly.booking.domain.RoomStatus;
import com.reservly.booking.dto.CreateRoomRequest;
import com.reservly.booking.dto.RoomResponse;
import com.reservly.booking.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper mapper;

    @Transactional
    public RoomResponse create(CreateRoomRequest request) {

        RoomEntity entity = mapper.toEntity(request);
        entity.setStatus(RoomStatus.ACTIVE);
        entity.setCreatedAt(Instant.now());

        return mapper.toResponse(roomRepository.save(entity));
    }

    public RoomResponse getById(Long id) {

        Optional<RoomEntity> roomEntityOptional = roomRepository.findById(id);

        RoomEntity entity =  roomEntityOptional.orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id='%s' not found".formatted(id)));

        return mapper.toResponse(entity);
    }
}
