package vn.isofh.security.configuration.jwt_config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtSecretKeyConfiguration {
    private final JwtPropertiesConfiguration jwtConfigProperties;

    @Autowired
    public JwtSecretKeyConfiguration(JwtPropertiesConfiguration jwtConfigProperties) {
        this.jwtConfigProperties = jwtConfigProperties;
    }

    @Bean
    public SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtConfigProperties.getSecretKey().getBytes());
    }
}
