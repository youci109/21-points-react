package gds.health.web.rest;

import gds.health.service.PreferencesService;
import gds.health.web.rest.errors.BadRequestAlertException;
import gds.health.service.dto.PreferencesDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link gds.health.domain.Preferences}.
 */
@RestController
@RequestMapping("/api")
public class PreferencesResource {

    private final Logger log = LoggerFactory.getLogger(PreferencesResource.class);

    private static final String ENTITY_NAME = "preferences";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PreferencesService preferencesService;

    public PreferencesResource(PreferencesService preferencesService) {
        this.preferencesService = preferencesService;
    }

    /**
     * {@code POST  /preferences} : Create a new preferences.
     *
     * @param preferencesDTO the preferencesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new preferencesDTO, or with status {@code 400 (Bad Request)} if the preferences has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/preferences")
    public ResponseEntity<PreferencesDTO> createPreferences(@Valid @RequestBody PreferencesDTO preferencesDTO) throws URISyntaxException {
        log.debug("REST request to save Preferences : {}", preferencesDTO);
        if (preferencesDTO.getId() != null) {
            throw new BadRequestAlertException("A new preferences cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PreferencesDTO result = preferencesService.save(preferencesDTO);
        return ResponseEntity.created(new URI("/api/preferences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /preferences} : Updates an existing preferences.
     *
     * @param preferencesDTO the preferencesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated preferencesDTO,
     * or with status {@code 400 (Bad Request)} if the preferencesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the preferencesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/preferences")
    public ResponseEntity<PreferencesDTO> updatePreferences(@Valid @RequestBody PreferencesDTO preferencesDTO) throws URISyntaxException {
        log.debug("REST request to update Preferences : {}", preferencesDTO);
        if (preferencesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PreferencesDTO result = preferencesService.save(preferencesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, preferencesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /preferences} : get all the preferences.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of preferences in body.
     */
    @GetMapping("/preferences")
    public List<PreferencesDTO> getAllPreferences() {
        log.debug("REST request to get all Preferences");
        return preferencesService.findAll();
    }

    /**
     * {@code GET  /preferences/:id} : get the "id" preferences.
     *
     * @param id the id of the preferencesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the preferencesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/preferences/{id}")
    public ResponseEntity<PreferencesDTO> getPreferences(@PathVariable Long id) {
        log.debug("REST request to get Preferences : {}", id);
        Optional<PreferencesDTO> preferencesDTO = preferencesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(preferencesDTO);
    }

    /**
     * {@code DELETE  /preferences/:id} : delete the "id" preferences.
     *
     * @param id the id of the preferencesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/preferences/{id}")
    public ResponseEntity<Void> deletePreferences(@PathVariable Long id) {
        log.debug("REST request to delete Preferences : {}", id);
        preferencesService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/preferences?query=:query} : search for the preferences corresponding
     * to the query.
     *
     * @param query the query of the preferences search.
     * @return the result of the search.
     */
    @GetMapping("/_search/preferences")
    public List<PreferencesDTO> searchPreferences(@RequestParam String query) {
        log.debug("REST request to search Preferences for query {}", query);
        return preferencesService.search(query);
    }

}
