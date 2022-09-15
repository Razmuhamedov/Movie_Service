package ms.movie_service.dto.movie;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter

public class CreateMovieDto {
    @NotBlank(message = "Name can not be empty or null")
    private String name;
    @NotEmpty(message = "Description cannot be empty")
    private String description;
    private Integer creatorId;
}
