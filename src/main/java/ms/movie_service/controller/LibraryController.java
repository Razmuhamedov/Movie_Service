package ms.movie_service.controller;

import ms.movie_service.dto.library.LibraryDto;
import ms.movie_service.dto.movie.MovieDto;
import ms.movie_service.service.LibraryService;
import ms.movie_service.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/library")
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/watch/{id}")
    public ResponseEntity<?> watchMovie(@PathVariable("id") Integer movieId){
        LibraryDto result = libraryService.watchMovie(SecurityUtil.getUserId(), movieId);
        return ResponseEntity.ok(result);
    }
    /*public ResponseEntity<?> watchMovie(@RequestParam("user") Integer userId,
                                        @RequestParam("movie") Integer movieId){
        LibraryDto result = libraryService.watchMovie(userId, movieId);
        return ResponseEntity.ok(result);
    }*/

    @GetMapping("/myLibrary")
    public ResponseEntity<?> myLibrary(@RequestParam("page") Integer page,
                                     @RequestParam("size") Integer size){
        List<MovieDto> result = libraryService.history(page, size, SecurityUtil.getUserId());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/secured/getLibraryByUser/{id}")
    public ResponseEntity<?> getLibraryByUserId(@PathVariable("id") Integer userId,
                                                @RequestParam("page") Integer page,
                                                @RequestParam("size") Integer size){
        List<MovieDto> result = libraryService.history(page, size, userId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/deleteMovieFromLibrary/{id}")
    public ResponseEntity<?> deleteHistory(@PathVariable("id") Integer movieId){
        String result = libraryService.deleteHistory(SecurityUtil.getUserId(), movieId);
        return ResponseEntity.ok(result);
    }




}
