package ms.movie_service.dto.movie;

import lombok.Getter;
import lombok.Setter;
import ms.movie_service.dto.FilterDto;
import ms.movie_service.type.Country;

import java.time.LocalDate;

@Getter
@Setter
public class MovieFilterDto extends FilterDto {
    private String name;
    private String description;
    private Integer creatorId;
    private Double rateMax;
    private Double rateMin;
    private Country country;
    private LocalDate minReleasedAt;
    private LocalDate maxReleasedAt;
}
