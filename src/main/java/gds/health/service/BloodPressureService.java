package gds.health.service;

import gds.health.domain.BloodPressure;
import gds.health.repository.BloodPressureRepository;
import gds.health.repository.search.BloodPressureSearchRepository;
import gds.health.service.dto.BloodPressureDTO;
import gds.health.service.mapper.BloodPressureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link BloodPressure}.
 */
@Service
@Transactional
public class BloodPressureService {

    private final Logger log = LoggerFactory.getLogger(BloodPressureService.class);

    private final BloodPressureRepository bloodPressureRepository;

    private final BloodPressureMapper bloodPressureMapper;

    private final BloodPressureSearchRepository bloodPressureSearchRepository;

    public BloodPressureService(BloodPressureRepository bloodPressureRepository, BloodPressureMapper bloodPressureMapper, BloodPressureSearchRepository bloodPressureSearchRepository) {
        this.bloodPressureRepository = bloodPressureRepository;
        this.bloodPressureMapper = bloodPressureMapper;
        this.bloodPressureSearchRepository = bloodPressureSearchRepository;
    }

    /**
     * Save a bloodPressure.
     *
     * @param bloodPressureDTO the entity to save.
     * @return the persisted entity.
     */
    public BloodPressureDTO save(BloodPressureDTO bloodPressureDTO) {
        log.debug("Request to save BloodPressure : {}", bloodPressureDTO);
        BloodPressure bloodPressure = bloodPressureMapper.toEntity(bloodPressureDTO);
        bloodPressure = bloodPressureRepository.save(bloodPressure);
        BloodPressureDTO result = bloodPressureMapper.toDto(bloodPressure);
        bloodPressureSearchRepository.save(bloodPressure);
        return result;
    }

    /**
     * Get all the bloodPressures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BloodPressureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BloodPressures");
        return bloodPressureRepository.findAll(pageable)
            .map(bloodPressureMapper::toDto);
    }


    /**
     * Get one bloodPressure by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BloodPressureDTO> findOne(Long id) {
        log.debug("Request to get BloodPressure : {}", id);
        return bloodPressureRepository.findById(id)
            .map(bloodPressureMapper::toDto);
    }

    /**
     * Delete the bloodPressure by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BloodPressure : {}", id);
        bloodPressureRepository.deleteById(id);
        bloodPressureSearchRepository.deleteById(id);
    }

    /**
     * Search for the bloodPressure corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BloodPressureDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BloodPressures for query {}", query);
        return bloodPressureSearchRepository.search(queryStringQuery(query), pageable)
            .map(bloodPressureMapper::toDto);
    }
}
