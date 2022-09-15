package ms.movie_service.service;

import ms.movie_service.dto.rate.CreateRateDto;
import ms.movie_service.dto.rate.RateDto;
import ms.movie_service.dto.rate.RateFilterDto;
import ms.movie_service.entity.Movie;
import ms.movie_service.entity.Rate;
import ms.movie_service.exception.BadRequest;
import ms.movie_service.repository.MovieRepository;
import ms.movie_service.repository.RateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RateService {

    private final RateRepository rateRepository;
    private final UserService userService;
    private final MovieService movieService;
    private final MovieRepository movieRepository;

    public RateService(RateRepository rateRepository, UserService userService, MovieService movieService, MovieRepository movieRepository) {
        this.rateRepository = rateRepository;
        this.userService = userService;
        this.movieService = movieService;
        this.movieRepository = movieRepository;
    }

    public RateDto createRate(CreateRateDto dto){
        Rate rate = new Rate();
        Optional<Rate> optional = rateRepository.findByUserIdAndMovieId(dto.getUserId(), dto.getMovieId());
        optional.ifPresent(value -> deleteRate(value.getId()));
        rate.setScore(dto.getScore());
        rate.setUserId(dto.getUserId());
        rate.setMovieId(dto.getMovieId());
        rate.setCreatedAt(LocalDateTime.now());
        rate.setStatus(true);
        rateRepository.save(rate);
        Movie movie = movieService.getEntity(dto.getMovieId());
        movie.setRate(rateMovie(dto.getMovieId()));
        movieRepository.save(movie);
        return convertToDto(rate, new RateDto());
    }

    public List<RateDto> getAll(){
        List<Rate> rates = rateRepository.findAll();
        return getAllBy(rates);
    }

    public List<RateDto> getByUserId(Integer userId) {
        List<Rate> rates = rateRepository.findAllByUserId(userId);
        return getAllBy(rates);
    }

    public List<RateDto> getByMovieId(Integer movieId){
        List<Rate> rates = rateRepository.findAllByMovieId(movieId);
        return getAllBy(rates);
    }

    private List<RateDto> getAllBy(List<Rate> rates){
        if(rates.isEmpty()){
            throw new BadRequest("No ratings yet!");
        }
        List<RateDto> rateList = new ArrayList<>();
        for (Rate rate: rates) {
            rateList.add(convertToDto(rate, new RateDto()));
        }
        return rateList;
    }

    private Double rateMovie(Integer movieId){
        return rateRepository.selectAvgByMovieId(movieId);
    }

    public Rate getEntity(Integer rateId){
        Optional<Rate> optional = rateRepository.findById(rateId);
        if(optional.isEmpty()) throw new BadRequest("Rating not found!");
        return optional.get();
    }

    public RateDto getRate(Integer rateId){
        Rate rate = getEntity(rateId);
        if(!rate.getStatus()) throw new BadRequest("Rating has been deleted!");
        return convertToDto(rate, new RateDto());
    }

    public void deleteRate(Integer rateId){
        Rate rate = getEntity(rateId);
        rate.setStatus(false);
        rate.setDeletedAt(LocalDateTime.now());
    }

    public String deleteEntity(Integer rateId){
        Rate rate = getEntity(rateId);
        rateRepository.delete(rate);
        return "Rating deleted!";
    }
    public List<RateDto> filter(RateFilterDto dto){
        String sortBy = "createdAt";
        if(dto.getSortBy() != null){
            sortBy = dto.getSortBy();
        }
        PageRequest pageRequest = PageRequest.of(dto.getPage(), dto.getSize(), dto.getDirection(), sortBy);
        List<Predicate> predicates = new ArrayList<>();
        Specification<Rate> specification = ((root, query, criteriaBuilder) ->{
            if(dto.getMovieName() != null){
                predicates.add(criteriaBuilder.like(root.get("movie"), "%" + dto.getMovieName() + "%"));
            }
            if(dto.getUserName() != null){
                predicates.add(criteriaBuilder.like(root.get("user"), "%" + dto.getUserName() + "%"));
            }
            if(dto.getMinScore() != null && dto.getMaxScore() != null){
                predicates.add(criteriaBuilder.between(root.get("score"), dto.getMinScore(), dto.getMaxScore()));
            }
            if(dto.getMinScore() != null && dto.getMaxScore() == null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("score"), dto.getMinScore()));
            }
            if(dto.getMinScore() == null && dto.getMaxScore() != null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("score"), dto.getMaxScore()));
            }
            if (dto.getMinCreatedDate() != null && dto.getMaxCreatedDate() != null) {
                predicates.add(criteriaBuilder.between(root.get("createdAt"), dto.getMinCreatedDate(), dto.getMaxCreatedDate()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        } );
        Page<Rate> page = rateRepository.findAll(specification, pageRequest);

        return page.stream().map(rate -> convertToDto(rate, new RateDto())).collect(Collectors.toList());

    }

    private RateDto convertToDto(Rate rate, RateDto dto) {
        dto.setId(rate.getId());
        dto.setScore(rate.getScore());
        dto.setUser(userService.getUser(rate.getUserId()));
        dto.setMovie(movieService.getMovie(rate.getMovieId()));
        return dto;
    }

    public Long count() {
        return rateRepository.count();
    }
}
