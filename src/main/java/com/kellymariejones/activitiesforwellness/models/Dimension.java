package com.kellymariejones.activitiesforwellness.models;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Dimension extends AbstractEntity {

    private String name;

    private String description;

     // mark final so that the list itself cannot be changed;
     // however, the contents can still be changed

    @OneToMany(mappedBy = "dimension")
    //@JoinColumn(name="dimension_id", referencedColumnName="id")
    private final List<Activity> activity = new ArrayList<>();

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

    //no setter for activity because it is final

    @Override
    public String toString() {
        return name;
    }

}
