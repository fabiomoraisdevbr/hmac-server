package com.example.hmac.service;

import com.example.hmac.dto.MessageResquestDTO;
import com.example.hmac.dto.WeatherStationMessage;
import com.example.hmac.repository.HmacRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HmacOauthService {

    @Autowired
    private HmacUtils hmacUtils;

    @Autowired
    private HashCacheService cache;


    public Boolean oauth(WeatherStationMessage message, Long timestamp, String hash) {
        if(isMessageOverdue(timestamp) || cache.exists(hash)) {
            System.out.println("Mensagem antiga ou já recebida");
            return false;
        }

        var content = createContent(message.temperature(), message.humidity(), message.pressure(), timestamp);

        var serverHash = hmacUtils.sign(content);

        var isOauth = serverHash.equals(hash);

        if(isOauth) {
            cache.save(hash);
            System.out.println("Mensagem recebida com sucesso");
            return true;
        }
        System.out.println("Menasgem com hash invalido");
        return false;
    }

    public MessageResquestDTO generateOauth(WeatherStationMessage message) {
        var currentTime = System.currentTimeMillis();

        var content = createContent(message.temperature(), message.humidity(), message.pressure(), currentTime);
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

    private String createContent(String temp, String humi, String pres, Long time) {

        return  String.join(
                ":",
                temp,
                humi,
                pres,
                time.toString()
        );



    }


}
