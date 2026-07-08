package com.reservly.booking.service;

import com.reservly.booking.domain.RoomEntity;
import com.reservly.booking.domain.RoomMapper;
import com.reservly.booking.domain.RoomStatus;
import com.reservly.booking.dto.CreateRoomRequest;
import com.reservly.booking.dto.RoomResponse;
import com.reservly.booking.dto.UpdateRoomRequest;
import com.reservly.booking.repository.RoomRepository;
import com.reservly.common.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

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
        return mapper.toResponse(getEntityById(id));
    }

    @Transactional
    public RoomResponse updateRoom(Long id, UpdateRoomRequest updateRoomRequest) {

        RoomEntity findRoomEntity = getEntityById(id);

        findRoomEntity.setName(updateRoomRequest.name());
        findRoomEntity.setCapacity(updateRoomRequest.capacity());
        findRoomEntity.setType(updateRoomRequest.type());

        return mapper.toResponse(findRoomEntity);
    }

    @Transactional
    public RoomResponse deleteRoom(Long id) {
        RoomEntity forDeleteEntity = getEntityById(id);

        forDeleteEntity.setStatus(RoomStatus.HIDDEN);

        return mapper.toResponse(forDeleteEntity);
    }

    public Page<RoomResponse> list(Pageable pageable) {
        Page<RoomEntity> allByStatus = roomRepository.findAllByStatus(RoomStatus.ACTIVE, pageable);

        return allByStatus.map(mapper::toResponse);
    }

    private RoomEntity getEntityById(Long id) {
        RoomEntity roomEntity = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Room with id=%s not found".formatted(id)));

        if (roomEntity.getStatus() == RoomStatus.HIDDEN) {
            throw new NotFoundException("Room with id=%s not found".formatted(id));
        }

        return roomEntity;
    }
}
