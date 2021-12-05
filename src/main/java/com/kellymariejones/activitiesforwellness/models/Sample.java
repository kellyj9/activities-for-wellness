package com.kellymariejones.activitiesforwellness.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

@Entity
public class Sample extends AbstractEntity {

    @Size(min=1, max=400, message=
            "Sample description only allows up to 400 characters.")
    private String description;

    // many samples to one dimension
    @ManyToOne
    private Dimension dimension;

    public Sample () {}

    public Sample(String description) {
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

    @Override
    public String toString() {
        return description;
    }

}
