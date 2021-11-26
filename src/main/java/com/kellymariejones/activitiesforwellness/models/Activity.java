package com.kellymariejones.activitiesforwellness.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Activity extends AbstractEntity{

    private String description;

    // JPA annotation to set up many-to-one relationship
    // between Activity and Dimension
    @ManyToOne // relate one dimension to an event
    private Dimension dimension;

    public Activity () {}

    //note to self: might need to put in constructor this line:
    // @Size(min=1, max=400, message="Activity description is required.")
    public Activity(String description, Dimension dimension) {
        this.description = description;
        this.dimension = dimension;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    @Override
    public String toString() {
        return description;
    }


}
