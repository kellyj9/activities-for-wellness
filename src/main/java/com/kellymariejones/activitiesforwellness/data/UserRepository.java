package com.kellymariejones.activitiesforwellness.data;

import com.kellymariejones.activitiesforwellness.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByUsername(String username);

}
