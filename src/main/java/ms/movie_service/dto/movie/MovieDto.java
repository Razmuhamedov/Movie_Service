package ms.movie_service.dto.movie;

import lombok.Getter;
import lombok.Setter;
import ms.movie_service.dto.user.UserDto;

@Getter
@Setter
public class MovieDto {
    private Integer id;
    private String name;
    private String description;
    private UserDto user;
    private Double rate;
}
