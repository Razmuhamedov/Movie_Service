package ms.movie_service.service;

import ms.movie_service.dto.library.LibraryDto;
import ms.movie_service.dto.movie.MovieDto;
import ms.movie_service.entity.Library;
import ms.movie_service.exception.BadRequest;
import ms.movie_service.repository.LibraryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final UserService userService;
    private final MovieService movieService;
    public LibraryService(LibraryRepository libraryRepository, UserService userService, MovieService movieService) {
        this.libraryRepository = libraryRepository;
        this.userService = userService;
        this.movieService = movieService;
    }

    public LibraryDto watchMovie(Integer userId, Integer movieId) {
        userService.getEntity(userId);
        movieService.getEntity(movieId);
        Library library = new Library();
        library.setUserId(userId);
        library.setMovieId(movieId);
        library.setCreatedAt(LocalDateTime.now());
        libraryRepository.save(library);
        return convertToDto(library, new LibraryDto());
    }

    public LibraryDto convertToDto(Library library, LibraryDto dto){
        dto.setUser(userService.getUser(library.getUserId()));
        dto.setMovie(movieService.getMovie(library.getMovieId()));
        return dto;
    }


    public List<MovieDto> history(Integer page, Integer size, Integer userId) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Library> pages = libraryRepository.findAll(pageRequest);
        List<MovieDto> movieList = new ArrayList<>();
        for (Library library: pages) {
            if(library.getUserId().equals(userId)){
                movieList.add(movieService.convertToDto(library.getMovie(), new MovieDto()));
            }
        }
        if(movieList.isEmpty()){
            throw new BadRequest("Library is empty!");
        }
        return movieList;

    }

    public String deleteHistory(Integer userId, Integer movieId) {
        List<Library> libraryList = libraryRepository.findAllByUserId(userId);
        if(libraryList.isEmpty()) throw new BadRequest("Library is empty!");
        for(Library library: libraryList){
            if(library.getMovieId().equals(movieId)){
                libraryRepository.delete(library);
            }
        }
        return "Movie deleted!";
    }
}
