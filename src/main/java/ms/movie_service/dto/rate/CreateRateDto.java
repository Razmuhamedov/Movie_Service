package ms.movie_service.dto.rate;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CreateRateDto {
    @NotNull
    @Positive(message = "Only positive numbers")
    @Size(max = 10, message = "Max score - 10")
    private Integer score;
    @NotNull(message = "User not be null")
    private Integer userId;
    @NotNull(message = "Movie not be null")
    private Integer movieId;
}
