package gds.health.repository;

import gds.health.domain.BloodPressure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the BloodPressure entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BloodPressureRepository extends JpaRepository<BloodPressure, Long> {


    @Query("select blood_pressure from BloodPressure blood_pressure where blood_pressure.user.login = ?#{principal.name} order by blood_pressure.timestamp desc")
    Page<BloodPressure> findByUserIsCurrentUser(Pageable pageable);

    Page<BloodPressure> findAllByOrderByTimestampDesc(Pageable pageable);


}
