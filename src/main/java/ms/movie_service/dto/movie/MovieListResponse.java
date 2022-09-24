package ms.movie_service.dto.movie;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MovieListResponse {
    private List<MovieDto> dtoList;
    private Long count;
}
