package edu.concordia.auth.user.services;

import edu.concordia.auth.exception.InvalidOtpException;
import edu.concordia.auth.exception.OtpExpiredException;
import edu.concordia.auth.exception.UsernameTakenException;
import edu.concordia.auth.user.entity.AppUser;
import edu.concordia.auth.user.entity.PasswordResetEntry;
import edu.concordia.auth.user.model.UserModel;
import edu.concordia.auth.user.repository.PasswordResetEntryRepository;
import edu.concordia.auth.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;

@Service
public class PasswordResetService  {

    @Autowired
    private PasswordResetEntryRepository repository;

    @Value("${app.auth.reset-password-expiry-seconds}")
    private long RESET_PASSWORD_EXPIRY_SECONDS;

    private void removePasswordResetEntryByUser(AppUser user) {
        repository.deleteAllByUser(user);
    }

    public PasswordResetEntry findPasswordResetEntryByUser(AppUser user) {
        return repository.findByUser(user);
    }

    public PasswordResetEntry createPasswordResetEntry(AppUser user) {
        this.removePasswordResetEntryByUser(user);
        PasswordResetEntry entry = PasswordResetEntry.builder()
                .user(user)
                .token(String.valueOf(new Random().nextInt(999999)))
                .build();
        return repository.save(entry);
    }

    public Boolean validatePasswordResetEntry(AppUser user, String otp)
            throws OtpExpiredException, InvalidOtpException{

        PasswordResetEntry entry = repository.findByUser(user);
        if (entry == null) {
            return false;
        }
        if (entry.getCreatedAt().plusSeconds(RESET_PASSWORD_EXPIRY_SECONDS).isAfter(Instant.now())) {
            throw new OtpExpiredException("OTP has expired");
        }
        if (!entry.getToken().equals(otp)) {
            throw new InvalidOtpException("Invalid OTP");
        }
        return true;
    }

}
