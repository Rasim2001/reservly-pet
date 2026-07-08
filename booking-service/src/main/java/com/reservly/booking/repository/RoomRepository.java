package com.reservly.booking.repository;

import com.reservly.booking.domain.RoomEntity;
import com.reservly.booking.domain.RoomStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    Page<RoomEntity> findAllByStatus(RoomStatus status, Pageable pageable);

}
