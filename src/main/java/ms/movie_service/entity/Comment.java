package ms.movie_service.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = ("comments"))
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String content;

    @ManyToOne
    @JoinColumn(name = ("user_id"), insertable = false, updatable = false)
    private User user;
    @Column(name = ("user_id"))
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = ("movie_id"), insertable = false, updatable = false)
    private Movie movie;
    @Column(name = ("movie_id"))
    private Integer movieId;

    @Column(name = ("created_at"))
    private LocalDateTime createdAt;
    @Column(name = ("deleted_at"))
    private LocalDateTime deletedAt;
}
