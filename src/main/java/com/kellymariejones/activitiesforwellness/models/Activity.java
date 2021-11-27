package com.kellymariejones.activitiesforwellness.models;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class Activity extends AbstractEntity{

    @Size(min=1, max=400, message="Please enter up to 400 characters.")
    private String description;

    // JPA annotation ManyToOne used to set up many-to-one relationship
    // between Activity and Dimension, which is why we have a property named
    // “dimension” in the Activity class
    @ManyToOne // relate one dimension to an activity
    //@JoinColumn(name="id", insertable = false, updatable = false)
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
