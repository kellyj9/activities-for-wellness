package com.kellymariejones.activitiesforwellness.data;

import com.kellymariejones.activitiesforwellness.models.Activity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository
        extends CrudRepository<Activity, Integer> {

        List<Activity> findByDimensionIdAndUserId(
                Integer dimension_id, Integer user_id);

        List<Activity> findByUserId(Integer user_id);
}
