package gds.health.repository;

import gds.health.domain.Points;
import gds.health.domain.User;
import gds.health.domain.UserPoint;
import gds.health.service.dto.PointsDTO;
import gds.health.service.dto.UserDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data  repository for the Points entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PointsRepository extends JpaRepository<Points, Long> {

    Page<Points> findAllByOrderByDateDesc(Pageable pageable);

    @Query("select points from Points points where points.user.login = ?#{principal.name} order by points.date desc")
    Page<Points> findByUserIsCurrentUser(Pageable pageable);

    @Modifying
    List<Points> findAllByDateBetweenAndUserLogin(LocalDate firstDate, LocalDate secondDate, String user);

    // 使用定义接口方式实现原生sql 的数据获取
    @Query(value="select u.id userId, u.login userName, sum(p.meals + p.alcohol + p.excercise) uPoints  from Points p, jhi_user u where p.user_id=u.login " +
        " and p.date >= :firstDate  and p.date <= :secondDate group by u.id order by uPoints desc", nativeQuery=true)
    List<UserPoint> findUserPointRanKBetween(@Param("firstDate") LocalDate firstDate, @Param("secondDate") LocalDate secondDate);

    // 使用domain定义的对象方式实现原生sql 的数据获取
    // @Query(value="select p.id id, p.date date, u.login login,p.meals meals,p.notes notes,p.user_id , p.alcohol alcohol, p.excercise excercise"+  
    //             " from Points p, jhi_user u where p.user_id=u.login group by p.id",nativeQuery=true)
    // List<Points> findUserPoint();

}
