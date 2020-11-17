package engine.repository;

import engine.Accomplishment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccomplishmentRepository extends CrudRepository<Accomplishment, String> {

    <C extends Accomplishment> C save(C completion);

    Page<Accomplishment> findByUserEmail(String email, Pageable pageable);
}
