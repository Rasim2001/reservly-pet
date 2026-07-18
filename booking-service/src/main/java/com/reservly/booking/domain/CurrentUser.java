package com.reservly.booking.domain;

import com.reservly.common.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentUser {

    public static final String X_USER_ID = "X-User-Id";

    private final HttpServletRequest request;

    public Long getCurrentUserId() {
        String header = request.getHeader(X_USER_ID);
        if (header == null) {
            throw new UnauthorizedException("Missing X-User-Id header");
        }
        return Long.parseLong(header);
    }
}
