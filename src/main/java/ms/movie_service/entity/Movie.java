package ms.movie_service.entity;

import lombok.Data;
import ms.movie_service.type.Country;
import ms.movie_service.type.MovieType;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data

@Entity
@Table(name = ("movies"))
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private Country country;

    @Column(name = ("released_at"))
    private LocalDate releasedAd;

    @Column(name = ("type"))
    private MovieType movieType;

    @ManyToOne
    @JoinColumn(name = ("creator_id"), insertable = false, updatable = false)
    private User user;
    @Column(name = ("creator_id"))
    private Integer userId;
    private Boolean status;

    @Column(name = ("rate"))
    private Double rate;

    @Column(name = ("created_at"))
    private LocalDateTime createdAt;
    @Column(name = ("updated_at"))
    private LocalDateTime updatedAt;
    @Column(name = ("deleted_at"))
    private LocalDateTime deletedAt;

}
