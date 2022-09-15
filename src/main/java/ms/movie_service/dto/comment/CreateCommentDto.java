package ms.movie_service.dto.comment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateCommentDto {
    private Integer userId;
    private Integer movieId;
    @NotBlank(message = "Content can not be null or empty!")
    private String content;
}
