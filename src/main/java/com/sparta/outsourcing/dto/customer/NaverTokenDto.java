package com.sparta.outsourcing.dto.customer;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverTokenDto {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private int expires_in;

    public NaverTokenDto(String json) {
        JsonElement element = JsonParser.parseString(json);
        this.access_token = element.getAsJsonObject().get("access_token").getAsString();
        this.refresh_token = element.getAsJsonObject().get("refresh_token").getAsString();
        this.token_type = element.getAsJsonObject().get("token_type").getAsString();
        this.expires_in = element.getAsJsonObject().get("expires_in").getAsInt();
    }
}