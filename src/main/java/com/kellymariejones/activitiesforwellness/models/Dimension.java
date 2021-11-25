package com.kellymariejones.activitiesforwellness.models;

import javax.persistence.Entity;

@Entity
public class Dimension extends AbstractEntity {

    private String name;

    public Dimension() {}

    public Dimension(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
