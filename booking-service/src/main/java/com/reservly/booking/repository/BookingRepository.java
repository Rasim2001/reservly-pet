package com.reservly.booking.repository;


import com.reservly.booking.domain.booking.BookingEntity;
import com.reservly.booking.domain.booking.BookingStatus;
import com.reservly.booking.dto.booking.BookingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    @Query("""
                select count(b) > 0 from BookingEntity b
                where b.room.id = :roomId
                and b.startTime < :endTime and b.endTime > :startTime
                and b.status in :statuses
            """)
    boolean existsOverlapping(@Param("roomId") Long roomId,
                              @Param("startTime") Instant startTime,
                              @Param("endTime") Instant endTime,
                              @Param("statuses") List<BookingStatus> statuses);


    Page<BookingEntity> findAllByUserId(Long userId, Pageable pageable);
}
