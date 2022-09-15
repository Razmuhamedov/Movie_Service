package ms.movie_service.dto.user;

import lombok.Getter;
import lombok.Setter;
import ms.movie_service.dto.FilterDto;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserFilterDto extends FilterDto {
    private String name;
    private String email;
    private Integer minAge;
    private Integer maxAge;



}
