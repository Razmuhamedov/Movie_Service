package ms.movie_service.repository;

import ms.movie_service.entity.Movie;
import ms.movie_service.type.MovieType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Integer>, JpaSpecificationExecutor<Movie> {

    @Query(value = "select * from Movie where deletedAt is null", nativeQuery = true)
    Page<Movie> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<Movie> findByIdAndDeletedAtIsNull(Integer id);

    @Query("select count(id) from Movie where deletedAt is null")
    Long countMovie();
    @Query("select count(id) from Movie where movieType =: movieType and deletedAt is null")
    Long countByType(@Param("movieType") MovieType movieType);
}
