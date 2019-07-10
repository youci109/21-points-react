package gds.health.web.rest;

import gds.health.service.WeigthService;
import gds.health.web.rest.errors.BadRequestAlertException;
import gds.health.service.dto.WeigthDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
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
 * REST controller for managing {@link gds.health.domain.Weigth}.
 */
@RestController
@RequestMapping("/api")
public class WeigthResource {

    private final Logger log = LoggerFactory.getLogger(WeigthResource.class);

    private static final String ENTITY_NAME = "weigth";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WeigthService weigthService;

    public WeigthResource(WeigthService weigthService) {
        this.weigthService = weigthService;
    }

    /**
     * {@code POST  /weigths} : Create a new weigth.
     *
     * @param weigthDTO the weigthDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new weigthDTO, or with status {@code 400 (Bad Request)} if the weigth has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/weigths")
    public ResponseEntity<WeigthDTO> createWeigth(@Valid @RequestBody WeigthDTO weigthDTO) throws URISyntaxException {
        log.debug("REST request to save Weigth : {}", weigthDTO);
        if (weigthDTO.getId() != null) {
            throw new BadRequestAlertException("A new weigth cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WeigthDTO result = weigthService.save(weigthDTO);
        return ResponseEntity.created(new URI("/api/weigths/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /weigths} : Updates an existing weigth.
     *
     * @param weigthDTO the weigthDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weigthDTO,
     * or with status {@code 400 (Bad Request)} if the weigthDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the weigthDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/weigths")
    public ResponseEntity<WeigthDTO> updateWeigth(@Valid @RequestBody WeigthDTO weigthDTO) throws URISyntaxException {
        log.debug("REST request to update Weigth : {}", weigthDTO);
        if (weigthDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WeigthDTO result = weigthService.save(weigthDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, weigthDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /weigths} : get all the weigths.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of weigths in body.
     */
    @GetMapping("/weigths")
    public ResponseEntity<List<WeigthDTO>> getAllWeigths(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Weigths");
        Page<WeigthDTO> page = weigthService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /weigths/:id} : get the "id" weigth.
     *
     * @param id the id of the weigthDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the weigthDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/weigths/{id}")
    public ResponseEntity<WeigthDTO> getWeigth(@PathVariable Long id) {
        log.debug("REST request to get Weigth : {}", id);
        Optional<WeigthDTO> weigthDTO = weigthService.findOne(id);
        return ResponseUtil.wrapOrNotFound(weigthDTO);
    }

    /**
     * {@code DELETE  /weigths/:id} : delete the "id" weigth.
     *
     * @param id the id of the weigthDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/weigths/{id}")
    public ResponseEntity<Void> deleteWeigth(@PathVariable Long id) {
        log.debug("REST request to delete Weigth : {}", id);
        weigthService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/weigths?query=:query} : search for the weigth corresponding
     * to the query.
     *
     * @param query the query of the weigth search.
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the result of the search.
     */
    @GetMapping("/_search/weigths")
    public ResponseEntity<List<WeigthDTO>> searchWeigths(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of Weigths for query {}", query);
        Page<WeigthDTO> page = weigthService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
