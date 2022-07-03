package io.lana.libman.core.user.dto;

import io.lana.libman.support.validate.StrongPassword;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class ResetPasswordDto {
    private String token;

    @StrongPassword
    @NotBlank
    private String password;

    @NotBlank
    private String retypePassword;
}
