package com.kellymariejones.activitiesforwellness.models;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Dimension extends AbstractEntity {

    private String name;

    private String description;

    @OneToMany(mappedBy = "dimension")
    private final List<Activity> activity = new ArrayList<>();

    @OneToMany(mappedBy = "dimension")
    private final List<Sample> sample = new ArrayList<>();

    public Dimension() {}

    public Dimension(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Activity> getActivity() {
        return activity;
    }

    public List<Sample> getSample() {
        return sample;
    }

    // no setter for activity or sample because they are final

    @Override
    public String toString() {
        return name;
    }

}
