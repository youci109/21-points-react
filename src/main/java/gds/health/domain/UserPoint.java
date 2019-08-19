package gds.health.domain;

/**
 * 此接口中可以定义多于和接收参数有关的get Methond
 */
public interface UserPoint {
    String getUserId();
    String getUserName();
    String getEmail();
    String getClassName();
    Integer getUPoints();
}