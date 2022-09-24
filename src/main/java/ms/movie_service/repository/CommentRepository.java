package ms.movie_service.repository;

import ms.movie_service.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer>, JpaSpecificationExecutor<Comment> {
    List<Comment> findAllByMovieId(Integer movieId);
    List<Comment> findAllByUserId(Integer userId);
}
