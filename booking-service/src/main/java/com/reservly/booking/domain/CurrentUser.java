package com.reservly.booking.domain;

import com.reservly.common.SecurityHeaders;
import com.reservly.common.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentUser {

    private final HttpServletRequest request;

    public Long getCurrentUserId() {
        String header = request.getHeader(SecurityHeaders.USER_ID);
        if (header == null) {
            throw new UnauthorizedException("Missing X-User-Id header");
        }
        return Long.parseLong(header);
    }
}
