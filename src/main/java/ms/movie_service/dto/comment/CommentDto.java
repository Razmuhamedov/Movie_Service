package ms.movie_service.dto.comment;

import lombok.Getter;
import lombok.Setter;
import ms.movie_service.dto.movie.MovieDto;
import ms.movie_service.dto.user.UserDto;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    private Integer id;
    private MovieDto movie;
    private UserDto user;
    private String content;
    private LocalDateTime createdAt;
}
