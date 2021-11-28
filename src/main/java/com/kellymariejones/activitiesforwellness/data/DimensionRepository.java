package com.kellymariejones.activitiesforwellness.data;

import com.kellymariejones.activitiesforwellness.models.Dimension;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DimensionRepository
        extends CrudRepository<Dimension, Integer> {
}
