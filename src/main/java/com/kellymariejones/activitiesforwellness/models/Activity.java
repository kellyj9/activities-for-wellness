package com.kellymariejones.activitiesforwellness.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Activity extends AbstractEntity{

    @NotBlank(message="Description of activity must not be blank.")
    @Size(min=1, max=400, message="Please enter between 1 and 400 characters.")
    private String description;

    // many activities to one dimension
    @ManyToOne
    private Dimension dimension;

    // many activities to one user
    @ManyToOne
    private User user;

    public Activity () {}

    public Activity(String description) {
        this.description = description;
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
