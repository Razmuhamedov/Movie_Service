package ms.movie_service.service;

import ms.movie_service.dto.movie.CreateMovieDto;
import ms.movie_service.dto.movie.MovieDto;
import ms.movie_service.dto.movie.MovieFilterDto;
import ms.movie_service.dto.user.UserDto;
import ms.movie_service.entity.Movie;
import ms.movie_service.exception.BadRequest;
import ms.movie_service.repository.MovieRepository;
import ms.movie_service.type.MovieType;
import ms.movie_service.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final UserService userService;

    public MovieService(MovieRepository movieRepository, UserService userService) {
        this.movieRepository = movieRepository;
        this.userService = userService;
    }

    public MovieDto createMovie(CreateMovieDto dto) {
        Movie movie = new Movie();
        movie.setName(dto.getName());
        movie.setDescription(dto.getDescription());
        movie.setUserId(SecurityUtil.getUserId());
        movie.setMovieType(dto.getMovieType());
        movie.setCreatedAt(LocalDateTime.now());
        movie.setReleasedAd(dto.getReleasedAt());
        movie.setStatus(true);
        movie.setUrl(dto.getUrl());
        movie.setRate(0.0);
        movieRepository.save(movie);
        return convertToDto(movie, new MovieDto());
    }

    public Movie getEntity(Integer movieId){
        Optional<Movie> optional = movieRepository.findByIdAndDeletedAtIsNull(movieId);
        if(optional.isEmpty()) throw new BadRequest("Movie not found!");
        return optional.get();
    }

    public MovieDto convertToDto(Movie movie, MovieDto dto) {
        dto.setId(movie.getId());
        dto.setName(movie.getName());
        dto.setDescription(movie.getDescription());
        dto.setRate(movie.getRate());
        dto.setMovieType(movie.getMovieType());
        dto.setReleasedAt(movie.getReleasedAd());
        dto.setUrl(movie.getUrl());
        dto.setUser(userService.convertToDto(userService.getEntity(movie.getUserId()), new UserDto()));
        return dto;
    }

    public MovieDto getMovie(Integer movieId) {
        Movie movie = getEntity(movieId);
        if(!movie.getStatus()){
            throw new BadRequest("Movie has been deleted!");
        }
        return convertToDto(movie, new MovieDto());
    }

    public MovieDto updateMovie(Integer id, CreateMovieDto dto) {
        Movie movie = getEntity(id);
        movie.setName(dto.getName());
        movie.setDescription(dto.getDescription());
        movie.setUserId(dto.getCreatorId());
        movie.setUpdatedAt(LocalDateTime.now());
        movie.setMovieType(dto.getMovieType());
        movie.setReleasedAd(dto.getReleasedAt());
        movie.setUrl(dto.getUrl());
        movieRepository.save(movie);
        return convertToDto(movie, new MovieDto());
    }

    public String softDelete(Integer id) {
        Movie movie = getEntity(id);
        movie.setDeletedAt(LocalDateTime.now());
        movie.setStatus(false);
        movieRepository.save(movie);
        return "Movie deleted!";
    }

    public String deleteEntity(Integer id) {
        Movie movie = getEntity(id);
        movie.setDeletedAt(LocalDateTime.now());
        movieRepository.delete(movie);
        return "Movie deleted!";
    }

    public List<MovieDto> filter(MovieFilterDto dto) {
        String sortBy = "createdAt";
        if(dto.getSortBy() != null){
            sortBy = dto.getSortBy();
        }
        PageRequest pageRequest = PageRequest.of(dto.getPage(), dto.getSize(), dto.getDirection(), sortBy);
        List<Predicate> predicates = new ArrayList<>();
        Specification<Movie> specification = ((root, query, criteriaBuilder) ->{
            if(dto.getName() != null){
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + dto.getName() + "%"));
            }
            if(dto.getDescription() != null){
                predicates.add(criteriaBuilder.like(root.get("description"), "%" + dto.getDescription() + "%"));
            }
            if(dto.getCreatorId() != null){
                predicates.add(criteriaBuilder.equal(root.get("creatorId"), dto.getCreatorId()));
            }
            if (dto.getMinCreatedDate() != null && dto.getMaxCreatedDate() != null) {
                predicates.add(criteriaBuilder.between(root.get("createdAt"), dto.getMinCreatedDate(), dto.getMaxCreatedDate()));
            }
            if(dto.getCountry() != null){
                predicates.add(criteriaBuilder.equal(root.get("country"), dto.getCountry()));
            }
            if(dto.getMinReleasedAt() != null && dto.getMaxReleasedAt() != null){
                predicates.add(criteriaBuilder.between(root.get("releasedAt"), dto.getMinReleasedAt(), dto.getMaxReleasedAt()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        } );
        Page<Movie> page = movieRepository.findAll(specification, pageRequest);

        return page.stream().map(movie -> convertToDto(movie, new MovieDto())).collect(Collectors.toList());
    }

    //todo: stream
    public List<MovieDto> getAllMovies(Integer page, Integer size) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Movie> pages = movieRepository.findAll(pageRequest);
        List<MovieDto> movieList = new ArrayList<>();
        for(Movie movie:pages){
            if(movie.getDeletedAt() == null){
                movieList.add(convertToDto(movie, new MovieDto()));
            }
        }
        return movieList;
    }

    public List<MovieDto> getAllByType(Integer page, Integer size, MovieType movieType){
        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> pages = movieRepository.findAll(pageable);
        List<MovieDto> dtoList = new ArrayList<>();
        for(Movie movie: pages) {
            if (movie.getMovieType().equals(movieType)) {
                dtoList.add(convertToDto(movie, new MovieDto()));
            }
        }
        return dtoList;
    }

    public Long getMoviesCount(){
        return movieRepository.countMovie();
    }

    public Long getMoviesCountByType(MovieType movieType) {
        return movieRepository.countByType(movieType);
    }
}
