package gds.health.repository;

import gds.health.domain.Weigth;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Weigth entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WeigthRepository extends JpaRepository<Weigth, Long> {

    @Query("select weigth from Weigth weigth where weigth.user.login = ?#{principal.name} order by weight.timestamp desc")
    Page<Weigth> findByUserIsCurrentUser(Pageable pageable);

    Page<Weigth> findAllByOrderByTimestampDesc(Pageable pageable);

}
