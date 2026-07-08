package com.reservly.booking.repository;

import com.reservly.booking.domain.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<RoomEntity,Long> {
}
