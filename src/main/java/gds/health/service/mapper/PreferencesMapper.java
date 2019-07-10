package gds.health.service.mapper;

import gds.health.domain.*;
import gds.health.service.dto.PreferencesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Preferences} and its DTO {@link PreferencesDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PreferencesMapper extends EntityMapper<PreferencesDTO, Preferences> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    PreferencesDTO toDto(Preferences preferences);

    @Mapping(source = "userId", target = "user")
    Preferences toEntity(PreferencesDTO preferencesDTO);

    default Preferences fromId(Long id) {
        if (id == null) {
            return null;
        }
        Preferences preferences = new Preferences();
        preferences.setId(id);
        return preferences;
    }
}
