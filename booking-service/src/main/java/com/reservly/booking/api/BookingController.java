package com.reservly.booking.api;

import com.reservly.booking.dto.booking.BookingResponse;
import com.reservly.booking.dto.booking.CreateBookingRequest;
import com.reservly.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@AllArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createBooking(@RequestBody @Valid CreateBookingRequest request) {
        BookingResponse bookingResponse = bookingService.create(request);

        log.info("Created booking with id={}", bookingResponse.id());

        return bookingResponse;
    }

    @DeleteMapping("/{id}")
    public BookingResponse cancel(@PathVariable Long id) {

        log.info("Cancelled booking with id = {}", id);

        return bookingService.cancel(id);
    }

    @GetMapping("/my")
    public Page<BookingResponse> getMy(@PageableDefault(size = 20) Pageable pageable) {
        log.info("Get my pageable list");

        return bookingService.getMy(pageable);
    }

    @GetMapping("/{id}")
    public BookingResponse getById(@PathVariable Long id) {
        log.info("Get by id = {}", id);

        return bookingService.getById(id);
    }
}
