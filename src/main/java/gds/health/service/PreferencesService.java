package gds.health.service;

import gds.health.domain.Preferences;
import gds.health.domain.User;
import gds.health.repository.PreferencesRepository;
import gds.health.repository.UserRepository;
import gds.health.repository.search.PreferencesSearchRepository;
import gds.health.security.AuthoritiesConstants;
import gds.health.security.SecurityUtils;
import gds.health.service.dto.PreferencesDTO;
import gds.health.service.mapper.PreferencesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Preferences}.
 */
@Service
@Transactional
public class PreferencesService {

    private final Logger log = LoggerFactory.getLogger(PreferencesService.class);

    private final PreferencesRepository preferencesRepository;

    private final PreferencesMapper preferencesMapper;

    private final PreferencesSearchRepository preferencesSearchRepository;

    private final UserRepository userRepository;

    public PreferencesService(PreferencesRepository preferencesRepository, PreferencesMapper preferencesMapper, PreferencesSearchRepository preferencesSearchRepository, UserRepository userRepository) {
        this.preferencesRepository = preferencesRepository;
        this.preferencesMapper = preferencesMapper;
        this.preferencesSearchRepository = preferencesSearchRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a preferences.
     *
     * @param preferencesDTO the entity to save.
     * @return the persisted entity.
     */
    public PreferencesDTO save(PreferencesDTO preferencesDTO) {
        log.debug("Request to save Preferences : {}", preferencesDTO);
        Preferences preferences = preferencesMapper.toEntity(preferencesDTO);
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            log.debug("No user passed in, using current user: {}", SecurityUtils.getCurrentUserLogin());
            preferences.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get());
        }
        preferences = preferencesRepository.save(preferences);
        PreferencesDTO result = preferencesMapper.toDto(preferences);
        preferencesSearchRepository.save(preferences);
        return result;
    }

    /**
     * Get all the preferences.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PreferencesDTO> findAll() {
        log.debug("Request to get all Preferences");
        List<PreferencesDTO> preferencesDTO = new ArrayList<>();
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            preferencesDTO = preferencesRepository.findAll().stream()
                .map(preferencesMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        } else {
            PreferencesDTO userPreferencesDTO = getUserPreferences();
            // don't return default value of 10 points in this method
            if (userPreferencesDTO.getId() != null) {
                preferencesDTO.add(userPreferencesDTO);
            }
        }
        return preferencesDTO;
    }


    /**
     * Get one preferences by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PreferencesDTO> findOne(Long id) {
        log.debug("Request to get Preferences : {}", id);
        return preferencesRepository.findById(id)
            .map(preferencesMapper::toDto);
    }

    /**
     * Delete the preferences by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Preferences : {}", id);
        preferencesRepository.deleteById(id);
        preferencesSearchRepository.deleteById(id);
    }

    /**
     * Search for the preferences corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PreferencesDTO> search(String query) {
        log.debug("Request to search Preferences for query {}", query);
        return StreamSupport
            .stream(preferencesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(preferencesMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     *  获取用户设置
     * @return 用户设置
     */
    public PreferencesDTO getUserPreferences() {
        String username = SecurityUtils.getCurrentUserLogin().get();
        log.debug("Request to get User Preferences : {}",username);
        Optional<Preferences> preferences = preferencesRepository.findOneByUserLogin(username);

        if(preferences.isPresent()){
           return preferences.map(preferencesMapper::toDto).get();
        } else {
            PreferencesDTO defaultPreferences = new PreferencesDTO();
            defaultPreferences.setWeeklyGoal(10); // default
            return defaultPreferences;
        }
    }
}
