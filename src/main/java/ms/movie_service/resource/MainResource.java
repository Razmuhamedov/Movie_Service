package ms.movie_service.resource;

import ms.movie_service.service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainResource {
    private final MovieService movieService;

    public MainResource(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("movies", movieService.getAllMovies(0, 15));
        return "home";
    }
}
