package engine.repository;

import engine.entity.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends CrudRepository<Quiz, Integer> {

    Optional<Quiz> findById(Integer id);

    Page<Quiz> findAll(Pageable pageable);

//    <T extends Quiz> T save(T question);
}