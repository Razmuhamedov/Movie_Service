package ms.movie_service.dto.rate;

import lombok.Getter;
import lombok.Setter;
import ms.movie_service.dto.movie.MovieDto;
import ms.movie_service.dto.user.UserDto;

@Getter
@Setter
public class RateDto {
    private Integer id;
    private Double score;
    private UserDto user;
    private MovieDto movie;
}
