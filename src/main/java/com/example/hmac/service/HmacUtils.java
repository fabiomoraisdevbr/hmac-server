package com.example.hmac.service;

import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Service
public class HmacUtils {

   @Value("${security.hmac.algorithm}")
   private  String algorithm;

    @Value("${security.hmac.secret}")
    private String secret;

    public  String sign(String payload) {
        var key = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                algorithm
        );

        return Hashing.hmacSha512(key)
                .hashString(payload, StandardCharsets.UTF_8)
                .toString();
    }
}
