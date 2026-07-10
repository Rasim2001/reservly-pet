package com.reservly.booking.service;

import com.reservly.booking.domain.room.RoomEntity;
import com.reservly.booking.domain.room.RoomMapper;
import com.reservly.booking.domain.room.RoomStatus;
import com.reservly.booking.dto.room.CreateRoomRequest;
import com.reservly.booking.dto.room.RoomResponse;
import com.reservly.booking.dto.room.UpdateRoomRequest;
import com.reservly.booking.repository.RoomRepository;
import com.reservly.common.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void deleteRoom(Long id) {
        RoomEntity forDeleteEntity = getEntityById(id);

        forDeleteEntity.setStatus(RoomStatus.HIDDEN);
    }

    public Page<RoomResponse> list(Pageable pageable) {
        Page<RoomEntity> allByStatus = roomRepository.findAllByStatus(RoomStatus.ACTIVE, pageable);

        return allByStatus.map(mapper::toResponse);
    }

    public RoomEntity getEntityByIdForUpdate(Long roomId) {
        return requireActive(roomRepository.findByIdForUpdate(roomId), roomId);
    }

    private RoomEntity getEntityById(Long id) {
        return requireActive(roomRepository.findById(id), id);
    }

    private RoomEntity requireActive(Optional<RoomEntity> found, Long id) {
        RoomEntity entity = found.orElseThrow(
                () -> new NotFoundException("Room with id=%s not found".formatted(id)));

        if (entity.getStatus() == RoomStatus.HIDDEN) {
            throw new NotFoundException("Room with id=%s not found".formatted(id));
        }
        return entity;
    }
}
