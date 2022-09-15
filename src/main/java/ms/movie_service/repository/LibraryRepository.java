package ms.movie_service.repository;

import ms.movie_service.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibraryRepository extends JpaRepository<Library, Integer> {
    List<Library> findAllByUserId(Integer id);
}
