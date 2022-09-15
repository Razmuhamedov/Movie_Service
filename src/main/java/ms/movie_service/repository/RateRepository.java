package ms.movie_service.repository;

import ms.movie_service.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RateRepository extends JpaRepository<Rate, Integer>, JpaSpecificationExecutor<Rate> {
    List<Rate> findAllByUserId(Integer userId);

    List<Rate> findAllByMovieId(Integer movieId);


    @Query("select avg(score) from Rate where movieId =:id and status=true")
    Double selectAvgByMovieId(@Param("id") Integer id);

    @Query("select r from Rate r where r.userId =:userId and r.movieId =:movieId and r.status=true")
    Optional<Rate> findByUserIdAndMovieId(@Param("userId") Integer userId, @Param("movieId") Integer movieId);

}
