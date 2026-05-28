package model;

import java.util.List;


public class Ballot {
    private List<String> preferences;

    public Ballot(List<String> preferences) {
        this.preferences = preferences;
    }

    public List<String> getPreferences() {
        return preferences;
    }
}