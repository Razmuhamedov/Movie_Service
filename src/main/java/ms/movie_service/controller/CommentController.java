package ms.movie_service.controller;

import ms.movie_service.dto.comment.CommentDto;
import ms.movie_service.dto.comment.CommentFilterDto;
import ms.movie_service.dto.comment.CreateCommentDto;
import ms.movie_service.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateCommentDto dto){
        CommentDto result = commentService.create(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/secured/getByMovieId/{id}")
    public ResponseEntity<?> getByMovieId(@PathVariable("id") Integer movieId){
        List<CommentDto> result = commentService.getByMovieId(movieId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/secured/getByUserId/{id}")
    public ResponseEntity<?> getByUserId(@PathVariable("id") Integer userId){
        List<CommentDto> result = commentService.getByUserId(userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/secured/getAllComments")
    public ResponseEntity<?> getAllComments(){
        List<CommentDto> result = commentService.getAllComments();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer commentId){
        CommentDto result = commentService.getById(commentId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filter(@RequestBody CommentFilterDto dto){
        List<CommentDto> result = commentService.filter(dto);
        return ResponseEntity.ok(result);
    }
    //todo: delete comment
}
