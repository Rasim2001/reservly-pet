package com.reservly.booking;

import com.reservly.booking.domain.room.RoomType;
import com.reservly.booking.dto.room.CreateRoomRequest;
import com.reservly.booking.dto.room.RoomResponse;
import com.reservly.booking.repository.BookingRepository;
import com.reservly.booking.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookingRaceIT {

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @LocalServerPort
    private int port;

    @Autowired
    private RoomService roomService;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void onlyOneBookingWinsTheRace() throws Exception {

        CreateRoomRequest roomRequest = new CreateRoomRequest("TestRoom", RoomType.COWORKING, 16);

        RoomResponse roomResponse = roomService.create(roomRequest);

        CountDownLatch countDownLatch = new CountDownLatch(1);

        String json = """
                { "roomId": %d, "startTime": "2026-07-20T02:00:00Z", "endTime": "2026-07-20T03:00:00Z" }
                """.formatted(roomResponse.id());

        List<Integer> responseList = Collections.synchronizedList(new ArrayList<>());

        Runnable task = () -> {
            try {
                countDownLatch.await();

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:" + port + "/api/bookings"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                int responseCode = response.statusCode();
                responseList.add(responseCode);

            } catch (Exception e) {
                e.printStackTrace();
            }
        };


        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start();
        t2.start();

        countDownLatch.countDown();

        t1.join();
        t2.join();

        assertThat(responseList).containsExactlyInAnyOrder(201, 409);
        assertThat(bookingRepository.count()).isEqualTo(1);
    }
}
