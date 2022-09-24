package ms.movie_service.dto.movie;

import lombok.Getter;
import lombok.Setter;
import ms.movie_service.dto.user.UserDto;
import ms.movie_service.type.Country;
import ms.movie_service.type.MovieType;

import java.time.LocalDate;

@Getter
@Setter
public class MovieDto {
    private Integer id;
    private String name;
    private String description;
    private UserDto user;
    private Double rate;
    private MovieType movieType;
    private LocalDate releasedAt;
    private Country country;
}
