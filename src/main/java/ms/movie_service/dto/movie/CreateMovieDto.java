package ms.movie_service.dto.movie;


import lombok.Getter;
import lombok.Setter;
import ms.movie_service.type.Country;
import ms.movie_service.type.MovieType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Getter
@Setter

public class CreateMovieDto {
    @NotBlank(message = "Name can not be empty or null")
    private String name;
    @NotEmpty(message = "Description cannot be empty")
    private String description;
    private Integer creatorId;
    @NotBlank(message = "Description cannot be empty or null")
    private MovieType movieType;
    @NotBlank(message = "Released Date cannot be empty or null")
    private LocalDate releasedAt;
    private Country country;
    @NotBlank(message = "Input url")
    private String url;
}
