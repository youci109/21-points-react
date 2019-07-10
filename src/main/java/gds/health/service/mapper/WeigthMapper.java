package gds.health.service.mapper;

import gds.health.domain.*;
import gds.health.service.dto.WeigthDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Weigth} and its DTO {@link WeigthDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface WeigthMapper extends EntityMapper<WeigthDTO, Weigth> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    WeigthDTO toDto(Weigth weigth);

    @Mapping(source = "userId", target = "user")
    Weigth toEntity(WeigthDTO weigthDTO);

    default Weigth fromId(Long id) {
        if (id == null) {
            return null;
        }
        Weigth weigth = new Weigth();
        weigth.setId(id);
        return weigth;
    }
}
