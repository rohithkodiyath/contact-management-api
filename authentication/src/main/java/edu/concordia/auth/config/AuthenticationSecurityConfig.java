package edu.concordia.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.concordia.auth.filter.JwtTokenFilter;
import edu.concordia.auth.user.services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity
@Configuration
public class AuthenticationSecurityConfig {

    @Autowired
    private  AuthenticationConfiguration authConfiguration;

    @Autowired
    @Qualifier("delegatedAuthenticationEntryPoint")
    private AuthenticationEntryPoint authEntryPoint;

    @Value("${app.cors.allowed-origins}")
    private String allowedCorsOrigins;

    @PostConstruct
    public void init(){
        System.out.println("AuthenticationSecurityConfig.init");
    }

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http = http.cors(cors -> {
            cors.configurationSource(corsConfigureSource());
            //cors.disable();
        }).csrf(csrf -> {
            csrf.disable();
        }).sessionManagement(session -> {
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });
        http = http.exceptionHandling(exception -> {
            exception.authenticationEntryPoint((request, response, ex) -> {
                Map<String, Object> data = new HashMap<>();
                data.put("message", "Authentication Failed: " + ex.getMessage());
                data.put("timestamp", new Date());
                data.put("status", HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                ex.printStackTrace();
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(data);
                    response.getOutputStream().println(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });

        http = http.authorizeHttpRequests(authorize -> {
            authorize.requestMatchers("/v1/public/**").permitAll()
                    .requestMatchers("/v1/auth/**").permitAll()
                    .requestMatchers("/swagger-ui.html").permitAll()
                    .requestMatchers("/webjars/**").permitAll()
                    .requestMatchers("/swagger-resources/**").permitAll()
                    .requestMatchers("/v2/api-docs").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .anyRequest().authenticated();
            //authorize.anyRequest().authenticated();
        });
        http = http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigureSource() {


        CorsConfigurer corsConfigurer = new CorsConfigurer();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedCorsOrigins.split(","))); // Specify your allowed origins here
        //configuration.setAllowedOrigins(Arrays.asList("")); // Specify your allowed origins here
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Specify your allowed methods here
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // Specify your allowed headers here
        configuration.setAllowCredentials(true); // If you want to send cookies or use credentials
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply CORS configuration to all paths
        return source;
    }


}
