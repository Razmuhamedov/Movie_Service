package ms.movie_service.controller;

import ms.movie_service.dto.movie.CreateMovieDto;
import ms.movie_service.dto.movie.MovieDto;
import ms.movie_service.dto.movie.MovieFilterDto;
import ms.movie_service.dto.movie.MovieListResponse;
import ms.movie_service.service.MovieService;
import ms.movie_service.type.MovieType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PatchMapping("/filter")
    public ResponseEntity<?> filterMovie(@RequestBody @Valid MovieFilterDto dto){
        List<MovieDto> result = movieService.filter(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAllMovies")
    public ResponseEntity<?> getAllMovies(@RequestParam("page") Integer page,
                                          @RequestParam("size") Integer size){
        MovieListResponse movieListResponse = new MovieListResponse();
        movieListResponse.setDtoList(movieService.getAllMovies(page, size));
        movieListResponse.setCount(movieService.getMoviesCount());
        return ResponseEntity.ok(movieListResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovie(@PathVariable("id") Integer id){
        MovieDto result = movieService.getMovie(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAllByType")
    public ResponseEntity<?> getAllByType(@RequestParam("page") Integer page,
                                          @RequestParam("size") Integer size,
                                          @RequestParam("type")MovieType movieType){
        MovieListResponse movieListResponse = new MovieListResponse();
        movieListResponse.setDtoList(movieService.getAllByType(page, size, movieType));
        movieListResponse.setCount(movieService.getMoviesCountByType(movieType));
        return ResponseEntity.ok(movieListResponse);
    }

    @PostMapping("/secured")
    public ResponseEntity<?> createMovie(@RequestBody @Valid CreateMovieDto dto){
        MovieDto result = movieService.createMovie(dto);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/secured/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable("id") Integer id,
                                        @RequestBody @Valid CreateMovieDto dto){
        MovieDto result = movieService.updateMovie(id, dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/secured/delete/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable("id") Integer id){
        String result = movieService.softDelete(id);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/secured/deleteEntity/{id}")
    public ResponseEntity<?> deleteEntity(@PathVariable("id") Integer id){
        String result = movieService.deleteEntity(id);
        return ResponseEntity.ok(result);
    }
}
