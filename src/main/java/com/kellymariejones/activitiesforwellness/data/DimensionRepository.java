package com.kellymariejones.activitiesforwellness.data;

import com.kellymariejones.activitiesforwellness.models.Dimension;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/*
 Spring is able to create a class on the fly that will create a class in the
 application's memory and then it will make sure that class can be autowired
 into the eventRepository field in EventController
 (Java has the ability to create classes on the fly in memory)
 we end up with an object that allows our code to store and retrieve object
 instances from a database of a specific type; in this case- Event object instances
*/

// Repository annotation tells Spring Boot that this is a
// class that Spring Boot should manage
@Repository
public interface DimensionRepository
        extends CrudRepository<Dimension, Integer> {
}
