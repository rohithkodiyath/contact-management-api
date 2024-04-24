package edu.concordia.auth.filter;

import edu.concordia.auth.user.entity.AppUser;
import edu.concordia.auth.user.services.UserService;
import edu.concordia.auth.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {



    @Autowired
    private  JwtTokenUtil jwtTokenUtil;

    @Autowired
    private  UserService userService;

    @Value("${app.auth.header}")
    private  String header;

    @Value("${app.auth.prefix}")
    private  String prefix;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

            System.out.println("JwtTokenFilter.doFilterInternal");
            // Get authorization header and validate
            final String auth_header = request.getHeader(header);
            if (auth_header == null || StringUtils.isEmpty(auth_header) || !auth_header.startsWith(prefix + " ")) {
                System.out.println("JwtTokenFilter.doFilterInternal: no token found");
                chain.doFilter(request, response);
                return;
            }
            // Get jwt token and validate
            final String token = auth_header.split(" ")[1].trim();

            if (!jwtTokenUtil.validateToken(token)) {
                chain.doFilter(request, response);
                return;
            }
            //Get user identity and set it on the spring security context
            AppUser userDetails = userService
                    .loadUserByUsername(jwtTokenUtil.extractUsername(token));

            UsernamePasswordAuthenticationToken
                    authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null,
                    userDetails == null ? List.of() : userDetails.getAuthorities()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);

    }

}