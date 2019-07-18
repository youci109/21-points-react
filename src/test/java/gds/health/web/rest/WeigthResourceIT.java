package gds.health.web.rest;

import gds.health.TwentyOnePointsReactApp;
import gds.health.config.TestSecurityConfiguration;
import gds.health.domain.Weigth;
import gds.health.domain.User;
import gds.health.repository.WeigthRepository;
import gds.health.repository.search.WeigthSearchRepository;
import gds.health.service.WeigthService;
import gds.health.service.dto.WeigthDTO;
import gds.health.service.mapper.WeigthMapper;
import gds.health.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static gds.health.web.rest.TestUtil.sameInstant;
import static gds.health.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link WeigthResource} REST controller.
 */
@SpringBootTest(classes = {TwentyOnePointsReactApp.class, TestSecurityConfiguration.class})
public class WeigthResourceIT {

    private static final ZonedDateTime DEFAULT_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Double DEFAULT_WEIGHT = 1D;
    private static final Double UPDATED_WEIGHT = 2D;

    @Autowired
    private WeigthRepository weigthRepository;

    @Autowired
    private WeigthMapper weigthMapper;

    @Autowired
    private WeigthService weigthService;

    /**
     * This repository is mocked in the gds.health.repository.search test package.
     *
     * @see gds.health.repository.search.WeigthSearchRepositoryMockConfiguration
     */
    @Autowired
    private WeigthSearchRepository mockWeigthSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restWeigthMockMvc;

    private Weigth weigth;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WeigthResource weigthResource = new WeigthResource(weigthService);
        this.restWeigthMockMvc = MockMvcBuilders.standaloneSetup(weigthResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Weigth createEntity(EntityManager em) {
        Weigth weigth = new Weigth()
            .timestamp(DEFAULT_TIMESTAMP)
            .weight(DEFAULT_WEIGHT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        weigth.setUser(user);
        return weigth;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Weigth createUpdatedEntity(EntityManager em) {
        Weigth weigth = new Weigth()
            .timestamp(UPDATED_TIMESTAMP)
            .weight(UPDATED_WEIGHT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        weigth.setUser(user);
        return weigth;
    }

    @BeforeEach
    public void initTest() {
        weigth = createEntity(em);
    }

    @Test
    @Transactional
    public void createWeigth() throws Exception {
        int databaseSizeBeforeCreate = weigthRepository.findAll().size();

        // Create security-aware mockMvc
        restWeigthMockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();

        // Create the Weigth
        WeigthDTO weigthDTO = weigthMapper.toDto(weigth);
        restWeigthMockMvc.perform(post("/api/weigths")
            .with(user("user"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weigthDTO)))
            .andExpect(status().isCreated());

        // Validate the Weigth in the database
        List<Weigth> weigthList = weigthRepository.findAll();
        assertThat(weigthList).hasSize(databaseSizeBeforeCreate + 1);
        Weigth testWeigth = weigthList.get(weigthList.size() - 1);
        assertThat(testWeigth.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testWeigth.getWeight()).isEqualTo(DEFAULT_WEIGHT);

        // Validate the Weigth in Elasticsearch
        verify(mockWeigthSearchRepository, times(1)).save(testWeigth);
    }

    @Test
    @Transactional
    public void createWeigthWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = weigthRepository.findAll().size();

        // Create the Weigth with an existing ID
        weigth.setId(1L);
        WeigthDTO weigthDTO = weigthMapper.toDto(weigth);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWeigthMockMvc.perform(post("/api/weigths")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weigthDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Weigth in the database
        List<Weigth> weigthList = weigthRepository.findAll();
        assertThat(weigthList).hasSize(databaseSizeBeforeCreate);

        // Validate the Weigth in Elasticsearch
        verify(mockWeigthSearchRepository, times(0)).save(weigth);
    }


    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = weigthRepository.findAll().size();
        // set the field null
        weigth.setTimestamp(null);

        // Create the Weigth, which fails.
        WeigthDTO weigthDTO = weigthMapper.toDto(weigth);

        restWeigthMockMvc.perform(post("/api/weigths")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weigthDTO)))
            .andExpect(status().isBadRequest());

        List<Weigth> weigthList = weigthRepository.findAll();
        assertThat(weigthList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = weigthRepository.findAll().size();
        // set the field null
        weigth.setWeight(null);

        // Create the Weigth, which fails.
        WeigthDTO weigthDTO = weigthMapper.toDto(weigth);

        restWeigthMockMvc.perform(post("/api/weigths")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weigthDTO)))
            .andExpect(status().isBadRequest());

        List<Weigth> weigthList = weigthRepository.findAll();
        assertThat(weigthList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWeigths() throws Exception {
        // Initialize the database
        weigthRepository.saveAndFlush(weigth);

        // Create security-aware mockMvc
        restWeigthMockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();

        // Get all the weigthList
        restWeigthMockMvc.perform(get("/api/weigths?sort=id,desc")
            .with(user("admin").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weigth.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(sameInstant(DEFAULT_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getWeigth() throws Exception {
        // Initialize the database
        weigthRepository.saveAndFlush(weigth);

        // Get the weigth
        restWeigthMockMvc.perform(get("/api/weigths/{id}", weigth.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(weigth.getId().intValue()))
            .andExpect(jsonPath("$.timestamp").value(sameInstant(DEFAULT_TIMESTAMP)))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWeigth() throws Exception {
        // Get the weigth
        restWeigthMockMvc.perform(get("/api/weigths/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWeigth() throws Exception {
        // Initialize the database
        weigthRepository.saveAndFlush(weigth);

        int databaseSizeBeforeUpdate = weigthRepository.findAll().size();

        // Create security-aware mockMvc
        restWeigthMockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();

        // Update the weigth
        Weigth updatedWeigth = weigthRepository.findById(weigth.getId()).get();
        // Disconnect from session so that the updates on updatedWeigth are not directly saved in db
        em.detach(updatedWeigth);
        updatedWeigth
            .timestamp(UPDATED_TIMESTAMP)
            .weight(UPDATED_WEIGHT);
        WeigthDTO weigthDTO = weigthMapper.toDto(updatedWeigth);

        restWeigthMockMvc.perform(put("/api/weigths")
            .with(user("user"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weigthDTO)))
            .andExpect(status().isOk());

        // Validate the Weigth in the database
        List<Weigth> weigthList = weigthRepository.findAll();
        assertThat(weigthList).hasSize(databaseSizeBeforeUpdate);
        Weigth testWeigth = weigthList.get(weigthList.size() - 1);
        assertThat(testWeigth.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testWeigth.getWeight()).isEqualTo(UPDATED_WEIGHT);

        // Validate the Weigth in Elasticsearch
        verify(mockWeigthSearchRepository, times(1)).save(testWeigth);
    }

    @Test
    @Transactional
    public void updateNonExistingWeigth() throws Exception {
        int databaseSizeBeforeUpdate = weigthRepository.findAll().size();

        // Create the Weigth
        WeigthDTO weigthDTO = weigthMapper.toDto(weigth);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeigthMockMvc.perform(put("/api/weigths")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weigthDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Weigth in the database
        List<Weigth> weigthList = weigthRepository.findAll();
        assertThat(weigthList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Weigth in Elasticsearch
        verify(mockWeigthSearchRepository, times(0)).save(weigth);
    }

    @Test
    @Transactional
    public void deleteWeigth() throws Exception {
        // Initialize the database
        weigthRepository.saveAndFlush(weigth);

        int databaseSizeBeforeDelete = weigthRepository.findAll().size();

        // Delete the weigth
        restWeigthMockMvc.perform(delete("/api/weigths/{id}", weigth.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Weigth> weigthList = weigthRepository.findAll();
        assertThat(weigthList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Weigth in Elasticsearch
        verify(mockWeigthSearchRepository, times(1)).deleteById(weigth.getId());
    }

    @Test
    @Transactional
    public void searchWeigth() throws Exception {
        // Initialize the database
        weigthRepository.saveAndFlush(weigth);
        when(mockWeigthSearchRepository.search(queryStringQuery("id:" + weigth.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(weigth), PageRequest.of(0, 1), 1));
        // Search the weigth
        restWeigthMockMvc.perform(get("/api/_search/weigths?query=id:" + weigth.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weigth.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(sameInstant(DEFAULT_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Weigth.class);
        Weigth weigth1 = new Weigth();
        weigth1.setId(1L);
        Weigth weigth2 = new Weigth();
        weigth2.setId(weigth1.getId());
        assertThat(weigth1).isEqualTo(weigth2);
        weigth2.setId(2L);
        assertThat(weigth1).isNotEqualTo(weigth2);
        weigth1.setId(null);
        assertThat(weigth1).isNotEqualTo(weigth2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WeigthDTO.class);
        WeigthDTO weigthDTO1 = new WeigthDTO();
        weigthDTO1.setId(1L);
        WeigthDTO weigthDTO2 = new WeigthDTO();
        assertThat(weigthDTO1).isNotEqualTo(weigthDTO2);
        weigthDTO2.setId(weigthDTO1.getId());
        assertThat(weigthDTO1).isEqualTo(weigthDTO2);
        weigthDTO2.setId(2L);
        assertThat(weigthDTO1).isNotEqualTo(weigthDTO2);
        weigthDTO1.setId(null);
        assertThat(weigthDTO1).isNotEqualTo(weigthDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(weigthMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(weigthMapper.fromId(null)).isNull();
    }
}
