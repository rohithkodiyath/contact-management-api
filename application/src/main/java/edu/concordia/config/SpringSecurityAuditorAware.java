package edu.concordia.config;

import edu.concordia.auth.user.entity.AppUser;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;


public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        System.out.println("SpringSecurityAuditorAware.getCurrentAuditor() called");
        // Your logic to fetch the current user
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication"+authentication);
        System.out.println("authentication.isAuthenticated"+authentication.isAuthenticated());

        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof AppUser)){
            return Optional.empty();
        }
        String uuid = ((AppUser)authentication.getPrincipal()).getUuid();
        System.out.println("SpringSecurityAuditorAware.getCurrentAuditor() called "+uuid);
        // Assuming the username is what you want to store as the auditor
        return Optional.ofNullable(uuid);
    }
}
