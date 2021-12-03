package com.kellymariejones.activitiesforwellness.data;

import com.kellymariejones.activitiesforwellness.models.Sample;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SampleRepository extends CrudRepository<Sample, Integer> {

    List<Sample> findByDimensionId(Integer dimension_id);

}
