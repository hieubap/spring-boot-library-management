package library.security.filter;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import library.security.configuration.jwt_config.JwtPropertiesConfiguration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtTokenVerifierFilter extends OncePerRequestFilter {
    private final JwtPropertiesConfiguration jwtConfigProperties;
    private final SecretKey secretKey;

    public JwtTokenVerifierFilter(JwtPropertiesConfiguration jwtConfigProperties, SecretKey secretKey) {
        this.jwtConfigProperties = jwtConfigProperties;
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // lấy chuỗi token từ httpservletrequest với header theo configProperties
        String authorizationHeader = httpServletRequest.getHeader(jwtConfigProperties.getAuthorizationHeader());

        // nếu chuỗi này không có hoặc rông hoặc không bắt đầu với prefix theo configProperties thì thực hiện filter tiếp theo
        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfigProperties.getTokenPrefix())) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        // bỏ phần prefix đầu chuỗi token và lấy chuỗi hoàn chỉnh
        String token = authorizationHeader.replace(jwtConfigProperties.getTokenPrefix(), "");

        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            Claims body = claimsJws.getBody();
            String username = body.getSubject();

            var authority = (List<Map<String, String>>) body.get("authorities");

            Set<SimpleGrantedAuthority> grantetAuth = authority.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    grantetAuth
            );


            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException e) {
            throw new IllegalStateException(String.format("token %s cannot truest", token));
        }
        // thực hiện filter tiếp theo
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
