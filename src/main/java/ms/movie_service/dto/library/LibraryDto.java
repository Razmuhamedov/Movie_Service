package ms.movie_service.dto.library;

import lombok.Getter;
import lombok.Setter;
import ms.movie_service.dto.movie.MovieDto;
import ms.movie_service.dto.user.UserDto;

@Getter
@Setter
public class LibraryDto {
    private UserDto user;
    private MovieDto movie;
}
