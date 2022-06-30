package io.lana.libman.core.user.dto;

import io.lana.libman.support.validate.StrongPassword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChangePasswordDto {
    @NotBlank
    private String oldPassword;

    @StrongPassword
    @NotBlank
    private String newPassword;

    @NotBlank
    private String retypeNewPassword;
}
