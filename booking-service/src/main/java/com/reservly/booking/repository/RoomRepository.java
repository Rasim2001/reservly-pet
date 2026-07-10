package com.reservly.booking.repository;

import com.reservly.booking.domain.room.RoomEntity;
import com.reservly.booking.domain.room.RoomStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    Page<RoomEntity> findAllByStatus(RoomStatus status, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from RoomEntity r where r.id = :id")
    Optional<RoomEntity> findByIdForUpdate(@Param("id") Long id);

}
