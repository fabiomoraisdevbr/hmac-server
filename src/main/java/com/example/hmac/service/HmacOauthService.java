package com.example.hmac.service;

import com.example.hmac.dto.MessageResquestDTO;
import com.example.hmac.dto.WeatherStationMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HmacOauthService {

    @Autowired
    private HmacUtils hmacUtils;


    public Boolean oauth(WeatherStationMessage message, Long timestamp, String hash) {
        if(isMessageOverdue(timestamp)) {
            return false;
        }

        var content = message.temperature() + message.humidity() + message.pressure() + timestamp;

        var serverHash = hmacUtils.sign(content);

        return serverHash.equals(hash);
    }

    public MessageResquestDTO generateOauth(WeatherStationMessage message) {
        var currentTime = System.currentTimeMillis();

        var content = message.temperature() + message.humidity() + message.pressure() + currentTime;
        var hash = hmacUtils.sign(content);

        return MessageResquestDTO.builder()
                .message(message)
                .timestamp(currentTime)
                .hash(hash)
                .build();
    }

    private Boolean isMessageOverdue(Long timestamp) {
        var currentTime = System.currentTimeMillis();
        var timeDifference = currentTime - timestamp;
        var fiveMinutesInMillis = 15 * 60 * 1000;

        return timeDifference > fiveMinutesInMillis;
    }


}
