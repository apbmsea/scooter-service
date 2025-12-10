package test.weewee.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class CookieService {

    @Value("${app.cookie.secure:true}")
    private boolean cookieSecure;

    public String createCookie(String name, String value, Duration maxAge) {
        log.debug("Creating cookie: {} (secure: {})", name, cookieSecure);

        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(maxAge)
                .sameSite("None")
                .build();

        log.debug("Cookie created: path=/, secure={}, httpOnly=true, sameSite=None, maxAge={}",
                cookieSecure, maxAge.getSeconds());
        return cookie.toString();
    }

    public String deleteCookie(String name) {
        log.debug("Deleting cookie: {}", name);

        ResponseCookie cookie = ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();

        return cookie.toString();
    }
}