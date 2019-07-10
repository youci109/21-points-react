package gds.health.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link WeigthSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class WeigthSearchRepositoryMockConfiguration {

    @MockBean
    private WeigthSearchRepository mockWeigthSearchRepository;

}
