package edu.concordia.auth.user.entity;

import edu.concordia.auth.user.model.UserModel;
import edu.concordia.shared.entitiy.CoreEntity;
import edu.concordia.shared.model.CoreModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.annotation.Documented;
import java.util.Collection;
import java.util.Collections;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AppUser extends CoreEntity implements UserDetails {

    @Id
    private String id;

    @Field("firstName")
    private String firstName;

    @Field("lastName")
    private String lastName;

    @Field("emailAddress")
    private String emailAddress;

    @Field("password")
    private String password;

    @Field("role")
    private String role = null;

    @Field("isAccountLocked")
    private Boolean isAccountLocked = false;

    @Field("isAccountActive")
    private Boolean isAccountActive = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.emailAddress;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.isAccountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isAccountActive;
    }

    @Override
    public UserModel toModel() {
        return UserModel.builder()
                .firstName(this.firstName)
                .lastName(this.lastName)
                .emailAddress(this.emailAddress)
                .build();
    }
}
