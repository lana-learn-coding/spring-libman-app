package io.lana.libman.core.user.dto;

import io.lana.libman.core.user.User;
import io.lana.libman.support.data.Gender;
import io.lana.libman.support.validate.DateBeforeNow;
import io.lana.libman.support.validate.PhoneNumber;
import io.lana.libman.support.validate.Unique;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Unique(value = "phone", message = "The phone was already used", entity = User.class)
@NoArgsConstructor
public class ProfileUpdateDto {
    @NotBlank
    private String id;

    @NotBlank
    private String firstName;

    private String lastName;

    @PhoneNumber
    @NotBlank
    private String phone;

    private String address;

    @NotNull
    private Gender gender = Gender.UNSPECIFIED;

    @DateBeforeNow
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    public ProfileUpdateDto(User user) {
        BeanUtils.copyProperties(user, this);
    }
}
