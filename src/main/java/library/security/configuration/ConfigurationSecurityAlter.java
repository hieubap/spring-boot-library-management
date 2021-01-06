package library.security.configuration;

import library.security.configuration.jwt_config.JwtPropertiesConfiguration;
import library.security.filter.JwtUsernamePasswordAuthenticationFilter;
import library.security.filter.TokenVerifierFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableCircuitBreaker
public class ConfigurationSecurityAlter extends WebSecurityConfigurerAdapter {
    private static final boolean isJwtToken = true;
    private static final boolean isRemember_Me = true;
    private static final boolean isForm_Login = false;

    private final UserDetailsService userDetailsService;
    private final SecretKey secretKey;
    private final JwtPropertiesConfiguration jwtConfigProperties;

    @Autowired
    public ConfigurationSecurityAlter(UserDetailsService userDetailsService, SecretKey secretKey, JwtPropertiesConfiguration jwtConfigProperties) {
        this.userDetailsService = userDetailsService;
        this.secretKey = secretKey;
        this.jwtConfigProperties = jwtConfigProperties;
//        System.out.println(":::"+filterChainProxy.getFilterChains().get(0).getFilters().get(0).getClass().getName());
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("!!! Authentication with Storage Mechanism is UserDetailService !!!");
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // set stateless with token
        if (isJwtToken) {
            http
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .addFilter(new JwtUsernamePasswordAuthenticationFilter(authenticationManager(), jwtConfigProperties, secretKey))
                    .addFilterAfter(new TokenVerifierFilter(jwtConfigProperties, secretKey), JwtUsernamePasswordAuthenticationFilter.class);
        }
        if (isForm_Login) {
            http
                    .formLogin()
                    .defaultSuccessUrl("/paths/list", true);
        }
        if (isRemember_Me) {
            http
                    .rememberMe()
                    .rememberMeCookieName("remember-me")
                    .alwaysRemember(true);
        }
        // disable csrf
        http
                .csrf().disable()
                .logout()
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .and()
                .httpBasic();
    }

}
