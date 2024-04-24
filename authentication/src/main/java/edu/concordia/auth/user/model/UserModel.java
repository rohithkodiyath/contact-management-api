package edu.concordia.auth.user.model;

import edu.concordia.auth.user.entity.AppUser;
import edu.concordia.shared.model.CoreModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserModel  extends CoreModel<AppUser> {


    private String id;

    @NotBlank(message = "First Name is mandatory")
    private String firstName;

    @NotBlank(message = "last Name is mandatory")
    private String lastName;

    @NotBlank(message = "Email address is mandatory")
    @Email(message = "Invalid Email format")
    private String emailAddress;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least 8 characters, one uppercase, one lowercase, one special character and one number")
    @NotBlank(message = "Password is mandatory")
    private String password;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least 8 characters, one uppercase, one lowercase and one number")
    @NotBlank(message = "Confirm Password is mandatory")
    private String confirmPassword;


    @Override
    public AppUser toEntity() {

        return AppUser.builder()
                .firstName(this.firstName)
                .lastName(this.lastName)
                .emailAddress(this.emailAddress)
                .password(this.password)
                .build();
    }
}
