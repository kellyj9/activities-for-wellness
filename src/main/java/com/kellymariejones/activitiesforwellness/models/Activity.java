package com.kellymariejones.activitiesforwellness.models;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

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


    @ManyToOne // relate one user to an activity
    //@JoinColumn(name="id", insertable = false, updatable = false)
    private User user;

    public Activity () {}

    //note to self: might need to put in constructor this line:
    // @Size(min=1, max=400, message="Activity description is required.")
    public Activity(String description, Dimension dimension, User user) {
        this.description = description;
        this.dimension = dimension;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return description;
    }

}
