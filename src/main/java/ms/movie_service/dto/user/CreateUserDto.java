package ms.movie_service.dto.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class CreateUserDto {
    @NotBlank(message = "Name can not be empty or null")
    private String name;
    @NotBlank(message = "Email cannot be empty or null")
    @Email
    private String email;
    @Positive
    @Min(value = 16)
    private Integer age;
}
