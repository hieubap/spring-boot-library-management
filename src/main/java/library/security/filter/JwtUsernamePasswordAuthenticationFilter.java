package library.security.filter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import library.jpa.responceEntity.EntityResponse;
import library.security.Dao.UsernameAndPasswordDao;
import library.security.configuration.jwt_config.JwtPropertiesConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtPropertiesConfiguration jwtConfigProperties;
    private final SecretKey secretKey;

    public JwtUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtPropertiesConfiguration jwtConfigProperties, SecretKey secretKey) {
        this.authenticationManager = authenticationManager;
        this.jwtConfigProperties = jwtConfigProperties;
        this.secretKey = secretKey;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // tạo dao để map dữ liệu vào từ request
            UsernameAndPasswordDao accountDAO = new ObjectMapper().readValue(request.getInputStream(), UsernameAndPasswordDao.class);

            Authentication authentication;

            if (accountDAO == null) {
                authentication = new UsernamePasswordAuthenticationToken("","");
            }

            // tạo token để authenticatinManager xác thực
            else
                authentication = new UsernamePasswordAuthenticationToken(
                        accountDAO.getUsername(),
                        accountDAO.getPassword()
                );

            // authenticationManager xác thực sẽ trả về một Authenticaion với authenticate
            // là true hoặc false tùy vào xác thực thành công không
            Authentication authenticate = authenticationManager.authenticate(authentication);

            return authenticate;
        } catch (JsonParseException e){
            System.out.println("error .");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // create token and response client
        String token = jwtConfigProperties.getTokenPrefix() + Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
                .signWith(secretKey)
                .compact();

        response.addHeader(jwtConfigProperties.getAuthorizationHeader(),token);

        // return response successful login
        response.setContentType("json");
        EntityResponse<Object> entityResponse = new EntityResponse(HttpStatus.OK, "Login Successful. Has a Token response for you !!!", token);

        String entityResponce = new ObjectMapper().writeValueAsString(entityResponse);

        response.getWriter().write(entityResponce);
        response.getWriter().flush();
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // return response error

        response.setContentType("json;charset=ISO-8859-1");


        EntityResponse<Object> entityResponse = new EntityResponse(HttpStatus.NOT_ACCEPTABLE, "username or password is not correct", null);

        String entityresponce = new ObjectMapper().writeValueAsString(entityResponse);

        response.getWriter().write(entityresponce);
        response.getWriter().flush();
    }
}
