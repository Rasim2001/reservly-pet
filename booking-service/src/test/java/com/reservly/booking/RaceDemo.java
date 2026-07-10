package com.reservly.booking;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CountDownLatch;

public class RaceDemo {
    public static void main(String[] args) throws Exception {
        String json = """
                { "roomId": 5, "startTime": "2026-07-20T04:00:00Z", "endTime": "2026-07-20T05:00:00Z" }
                """;

        CountDownLatch start = new CountDownLatch(1);   // "стартовый пистолет"

        Runnable task = () -> {
            try {
                start.await();                          // ждать выстрела
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8082/api/bookings"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.statusCode() + " -> " + response.body());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start();
        t2.start();       // оба потока стоят на await()

        start.countDown(); // выстрел — оба стартуют одновременно

        t1.join();         // дождаться обоих
        t2.join();
    }
}
