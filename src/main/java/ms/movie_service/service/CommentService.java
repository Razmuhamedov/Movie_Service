package ms.movie_service.service;

import ms.movie_service.dto.comment.CommentDto;
import ms.movie_service.dto.comment.CommentFilterDto;
import ms.movie_service.dto.comment.CreateCommentDto;
import ms.movie_service.dto.movie.MovieDto;
import ms.movie_service.dto.user.UserDto;
import ms.movie_service.entity.Comment;
import ms.movie_service.exception.BadRequest;
import ms.movie_service.repository.CommentRepository;
import ms.movie_service.util.SecurityUtil;
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
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final MovieService movieService;

    public CommentService(CommentRepository commentRepository, UserService userService, MovieService movieService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.movieService = movieService;
    }

    public String create(CreateCommentDto dto){
        Comment comment = new Comment();
        comment.setUserId(SecurityUtil.getUserId());
        comment.setMovieId(dto.getMovieId());
        comment.setContent(dto.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);
        return "Your comment is accepted";
    }

    public CommentDto convertToDto(Comment comment, CommentDto dto){
        dto.setId(comment.getId());
        dto.setUser(userService.convertToDto(comment.getUser(), new UserDto()));
        dto.setMovie(movieService.convertToDto(comment.getMovie(), new MovieDto()));
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }

    public Comment getEntity(Integer id){
        Optional<Comment> optional = commentRepository.findById(id);
        if(optional.isEmpty()){
            throw new BadRequest("Comment not found!");
        }
        return optional.get();
    }

    private List<CommentDto> getComments(List<Comment>  commentList){
        if(commentList.isEmpty()) {
            throw new BadRequest("Comments not found!");
        }
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment c:commentList) {
            commentDtoList.add(convertToDto(c, new CommentDto()));
        }
        return commentDtoList;
    }

    public List<CommentDto> getByMovieId(Integer movieId) {
        List<Comment> commentList = commentRepository.findAllByMovieId(movieId);
        return getComments(commentList);
    }

    public List<CommentDto> getByUserId(Integer userId) {
        List<Comment> commentList = commentRepository.findAllByUserId(userId);
        return getComments(commentList);
    }

    public List<CommentDto> getAllComments() {
        List<Comment> commentList = commentRepository.findAll();
        return getComments(commentList);
    }

    public CommentDto getById(Integer commentId) {
        return convertToDto(getEntity(commentId), new CommentDto());
    }

    public List<CommentDto> filter(CommentFilterDto dto){
        String sortBy = "createdAt";
        if(dto.getSortBy() != null){
            sortBy = dto.getSortBy();
        }
        PageRequest request = PageRequest.of(dto.getPage(), dto.getSize(), dto.getDirection(), sortBy);
        List<Predicate> predicates = new ArrayList<>();
        Specification<Comment> specification = ((root, query, criteriaBuilder) -> {
            if(dto.getUser() != null){
                predicates.add(criteriaBuilder.like(root.get("user"), "%" + dto.getUser() + "%"));
            }
            if (dto.getMovie() != null){
                predicates.add(criteriaBuilder.like(root.get("movie"), "%" + dto.getMovie() + "%"));
            }
            if(dto.getContent() != null){
                predicates.add(criteriaBuilder.like(root.get("content"), "%" + dto.getContent() + "%"));
            }
            if(dto.getMinCreatedDate() != null && dto.getMaxCreatedDate() != null){
                predicates.add(criteriaBuilder.between(root.get("createdAt"), dto.getMinCreatedDate(), dto.getMaxCreatedDate()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        Page<Comment> page = commentRepository.findAll(specification, request);
        return page.stream().map(comment -> convertToDto(comment, new CommentDto())).collect(Collectors.toList());

    }
}
