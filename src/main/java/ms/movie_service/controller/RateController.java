package ms.movie_service.controller;

import ms.movie_service.dto.rate.CreateRateDto;
import ms.movie_service.dto.rate.RateDto;
import ms.movie_service.dto.rate.RateFilterDto;
import ms.movie_service.entity.Rate;
import ms.movie_service.service.RateService;
import ms.movie_service.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rate")
public class RateController {
    private final RateService rateService;

    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    @PostMapping("/createRate")
    public ResponseEntity<?> createRate(@RequestBody CreateRateDto dto){
        String result = rateService.createRate(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getRateById/{id}")
    public ResponseEntity<?> getRate(@PathVariable("id") Integer id){
        RateDto result = rateService.getRate(id);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/deleteRate/{id}")
    public ResponseEntity<?> deleteRate(@PathVariable("id") Integer id){
        String result = rateService.deleteEntity(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getByMovieId/{id}")
    public ResponseEntity<?> getAllByMovieId(@PathVariable("id") Integer movieId){
        List<RateDto> result = rateService.getByMovieId(movieId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getByUserId/{id}")
    public ResponseEntity<?> getAllByUserId(@PathVariable("id") Integer userId){
        List<RateDto> result = rateService.getByUserId(userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getMyRates")
    public ResponseEntity<?> getMyRates(){
        List<RateDto> result = rateService.getByUserId(SecurityUtil.getUserId());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(){
        List<RateDto> result = rateService.getAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/count")
    public ResponseEntity<?> count(){
        Long result = rateService.count();
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/filter")
    public ResponseEntity<?> filter(@RequestBody RateFilterDto dto){
        List<RateDto> result = rateService.filter(dto);
        return ResponseEntity.ok(result);
    }
}
