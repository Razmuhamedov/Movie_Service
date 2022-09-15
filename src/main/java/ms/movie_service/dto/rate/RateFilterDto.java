package ms.movie_service.dto.rate;

import lombok.Getter;
import lombok.Setter;
import ms.movie_service.dto.FilterDto;

@Getter
@Setter
public class RateFilterDto extends FilterDto {
    private Double minScore;
    private Double maxScore;
    private String userName;
    private String movieName;
}
