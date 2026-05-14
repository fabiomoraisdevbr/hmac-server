package com.example.hmac.controller;

import com.example.hmac.dto.MessageResquestDTO;
import com.example.hmac.dto.WeatherStationMessage;
import com.example.hmac.service.HmacOauthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/message")
public class HmacOauthController {

    @Autowired
    private HmacOauthService hmacOauthService;

    @PostMapping
    public Boolean hmacOauth(@Valid @RequestBody MessageResquestDTO request) {

        var authenticated = hmacOauthService.oauth(request.message(), request.timestamp(), request.hash());

        if (!authenticated) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED
            );
        }
        return true;

    }

    @GetMapping
    public MessageResquestDTO generateHmac(@Valid @RequestBody WeatherStationMessage message) {


        return hmacOauthService.generateOauth(message);


    }

}
