package com.reservly.booking.service;

import com.reservly.booking.domain.CurrentUser;
import com.reservly.booking.domain.booking.BookingEntity;
import com.reservly.booking.domain.booking.BookingMapper;
import com.reservly.booking.domain.booking.BookingStatus;
import com.reservly.booking.domain.room.RoomEntity;
import com.reservly.booking.dto.booking.BookingResponse;
import com.reservly.booking.dto.booking.CreateBookingRequest;
import com.reservly.booking.event.BookingCreatedEvent;
import com.reservly.booking.event.BookingEventPublisher;
import com.reservly.booking.repository.BookingRepository;
import com.reservly.common.BadRequestException;
import com.reservly.common.ConflictException;
import com.reservly.common.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookingService {

    private final BookingMapper mapper;
    private final BookingRepository bookingRepository;
    private final RoomService roomService;
    private final CurrentUser currentUser;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public BookingResponse create(CreateBookingRequest createBookingRequest) {

        RoomEntity roomEntity = roomService.getEntityByIdForUpdate(createBookingRequest.roomId());

        if (!createBookingRequest.startTime().isBefore(createBookingRequest.endTime()))
            throw new BadRequestException("startTime must be before endTime");

        if (bookingRepository.existsOverlapping(
                createBookingRequest.roomId(),
                createBookingRequest.startTime(),
                createBookingRequest.endTime(),
                List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED))) {
            throw new ConflictException("Room is already booked for the requested time interval");
        }

        BookingEntity bookingEntity = mapper.toEntity(createBookingRequest);

        bookingEntity.setUserId(currentUser.getCurrentUserId());
        bookingEntity.setRoom(roomEntity);
        bookingEntity.setStatus(BookingStatus.PENDING);
        bookingEntity.setCreatedAt(Instant.now());

        BookingEntity saved = bookingRepository.save(bookingEntity);

        BookingCreatedEvent bookingEvent = BookingCreatedEvent.builder()
                .bookingId(saved.getId())
                .roomId(roomEntity.getId())
                .userId(currentUser.getCurrentUserId())
                .startTime(saved.getStartTime())
                .endTime(saved.getEndTime())
                .createdAt(saved.getCreatedAt())
                .build();

        eventPublisher.publishEvent(bookingEvent);

        return mapper.toResponse(saved);
    }

    public BookingResponse getById(Long id){
        return mapper.toResponse(getBookingEntity(bookingRepository.findById(id), id));
    }

    public Page<BookingResponse> getMy(Pageable pageable) {
        Page<BookingEntity> list = bookingRepository.findAllByUserId(currentUser.getCurrentUserId(), pageable);

        return list.map(mapper::toResponse);
    }

    @Transactional
    public BookingResponse cancel(Long id) {
        BookingEntity bookingEntity = getBookingEntity(bookingRepository.findById(id), id);

        if(bookingEntity.getStatus().equals(BookingStatus.COMPLETED) || bookingEntity.getStatus().equals(BookingStatus.CANCELLED))
            throw new ConflictException("You can't cancel booking with status %s".formatted(bookingEntity.getStatus()));

        bookingEntity.setStatus(BookingStatus.CANCELLED);

        return mapper.toResponse(bookingEntity);
    }

    private BookingEntity getBookingEntity(Optional<BookingEntity> found, Long id) {
        BookingEntity bookingEntity = found
                .orElseThrow(() -> new NotFoundException("Booking with id = '%s' not found".formatted(id)));

        if(!bookingEntity.getUserId().equals(currentUser.getCurrentUserId()))
            throw new NotFoundException("Booking with id = '%s' not found".formatted(id));

        return bookingEntity;
    }
}
