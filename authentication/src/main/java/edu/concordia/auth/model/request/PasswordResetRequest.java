package edu.concordia.auth.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest {

    @Email
    @NotEmpty
    private String emailAddress;

    @NotEmpty
    private String password;

    @NotEmpty
    private String conformPassword;


}
