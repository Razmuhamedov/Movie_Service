package ms.movie_service.dto.comment;

import lombok.Getter;
import lombok.Setter;
import ms.movie_service.dto.FilterDto;

@Getter
@Setter
public class CommentFilterDto extends FilterDto {
    private String user;
    private String movie;
    private String content;
}
