package ms.movie_service.dto.movie;

import lombok.Getter;
import lombok.Setter;
import ms.movie_service.dto.FilterDto;

@Getter
@Setter
public class MovieFilterDto extends FilterDto {
    private String name;
    private String description;
    private Integer creatorId;
    private Double rateMax;
    private Double rateMin;
}
