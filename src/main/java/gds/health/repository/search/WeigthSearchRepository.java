package gds.health.repository.search;

import gds.health.domain.Weigth;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Weigth} entity.
 */
public interface WeigthSearchRepository extends ElasticsearchRepository<Weigth, Long> {
}
