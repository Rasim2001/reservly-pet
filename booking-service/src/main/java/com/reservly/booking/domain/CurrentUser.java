package com.reservly.booking.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class CurrentUser {

    public Long getCurrentUserId() {
        return 1L;
    }
}
