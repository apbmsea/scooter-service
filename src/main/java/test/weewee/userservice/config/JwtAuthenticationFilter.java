package test.weewee.userservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import test.weewee.userservice.security.JwtUtil;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Пропускаем JWT проверку для эндпоинтов аутентификации (кроме logout)
        String path = request.getServletPath();
        if (path.startsWith("/auth/") && !path.equals("/auth/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("No Bearer token found in request, continuing filter chain");

            // ВОЗВРАЩАЕМ ОШИБКУ ВМЕСТО 403
            if (path.startsWith("/users/")) {
                sendErrorResponse(response, "Токен отсутствует. Пожалуйста, авторизуйтесь.");
                return; // ← ДОБАВИЛ return
            }

            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = authHeader.substring(7);

            // АГРЕССИВНАЯ ОЧИСТКА ТОКЕНА
            jwt = cleanToken(jwt);
            logger.debug("Processing JWT token for authentication (cleaned): {}...",
                    jwt.substring(0, Math.min(30, jwt.length())));

            if (!jwtUtil.validateToken(jwt)) {
                logger.warn("Invalid JWT token");

                // ВОЗВРАЩАЕМ ОШИБКУ ВМЕСТО 403
                if (path.startsWith("/users/")) {
                    sendErrorResponse(response, "Недействительный токен. Пожалуйста, авторизуйтесь заново.");
                    return; // ← ДОБАВИЛ return
                }

                filterChain.doFilter(request, response);
                return;
            }

            String userEmail = jwtUtil.getEmailFromToken(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                logger.debug("Loading user details for email: {}", userEmail);
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                logger.info("JWT token validated successfully for user: {}", userEmail);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.debug("Authentication set in SecurityContext for user: {}", userEmail);
            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            logger.error("JWT authentication failed: {}", exception.getMessage(), exception);

            // ВОЗВРАЩАЕМ ОШИБКУ ВМЕСТО 403
            if (path.startsWith("/users/")) {
                sendErrorResponse(response, "Ошибка аутентификации: " + exception.getMessage());
                return; // ← ДОБАВИЛ return
            }

            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }

    /**
     * Отправляет JSON ошибку вместо 403 Forbidden
     */
    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String errorJson = String.format(
                "{\"message\":\"%s\",\"errors\":{\"auth\":\"%s\"}}",
                "Ошибка аутентификации",
                errorMessage
        );
        response.getWriter().write(errorJson);
        response.getWriter().flush();
    }

    /**
     * Агрессивная очистка токена от любых лишних символов
     */
    private String cleanToken(String token) {
        if (token == null) {
            return null;
        }

        String original = token;

        // 1. Убираем ВСЕ кавычки (двойные и одинарные)
        String cleaned = token.replaceAll("[\"']", "");

        // 2. Убираем пробелы
        cleaned = cleaned.trim();

        // 3. Убираем слово "Bearer" если оно есть повторно
        cleaned = cleaned.replaceAll("(?i)bearer", "").trim();

        // 4. Убираем любые не-JWT символы в начале/конце
        cleaned = cleaned.replaceAll("^[^A-Za-z0-9]+|[^A-Za-z0-9]+$", "");

        logger.debug("=== TOKEN CLEANING DEBUG ===");
        logger.debug("Original: '{}'", original);
        logger.debug("Cleaned:  '{}'", cleaned);
        logger.debug("Length: {} -> {}", original.length(), cleaned.length());
        logger.debug("Starts with eyJ: {}", cleaned.startsWith("eyJ"));

        return cleaned;
    }
}