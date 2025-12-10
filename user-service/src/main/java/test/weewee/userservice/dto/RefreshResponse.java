package test.weewee.userservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshResponse {
    private UserResponse user;
    private String accessToken;
}