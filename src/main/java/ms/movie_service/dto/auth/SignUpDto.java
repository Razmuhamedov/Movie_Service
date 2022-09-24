package ms.movie_service.dto.auth;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Getter
@Setter
public class SignUpDto {

    @Length(min = 2, message = "Minimum length for name 2")
    @NotBlank(message = "Name cannot be null and empty")
    private String name;
    @Size(min = 8, message = "Minimum size for password 8")
    @NotBlank(message = "Password cannot be null and empty")
    private String password;
    @NotNull(message = "")
    @Min(value = 12)
    private Integer age;
    @Email(message = "This is not email type")
    @NotBlank(message = "Email cannot be null and empty")
    private String email;
}
