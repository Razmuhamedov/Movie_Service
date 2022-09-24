package ms.movie_service.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Integer id;
    private String name;
    private String email;
    private Integer age;
}
