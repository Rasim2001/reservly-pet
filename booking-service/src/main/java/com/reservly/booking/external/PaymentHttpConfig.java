package com.reservly.booking.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

@Configuration
public class PaymentHttpConfig {

    @Value("${payment-service.base-url}")
    private String paymentServiceBaseUrl;

    @Bean
    RestClient paymentRestClient(RestClient.Builder builder) {

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(2));
        factory.setReadTimeout(Duration.ofSeconds(5));

        return builder
                .baseUrl(paymentServiceBaseUrl)
                .build();
    }

    @Bean
    PaymentHttpClient paymentHttpClient(RestClient restClient) {
        return HttpServiceProxyFactory.builder()
                .exchangeAdapter(RestClientAdapter.create(restClient))
                .build()
                .createClient(PaymentHttpClient.class);
    }
}
