package com.kellymariejones.activitiesforwellness.data;

import com.kellymariejones.activitiesforwellness.models.Sample;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SampleRepository extends CrudRepository<Sample, Integer> {

    List<Sample> findAllByDimensionId(Integer dimension_id);

}
