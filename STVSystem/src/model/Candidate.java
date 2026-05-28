package model;

public class Candidate {

    private String name;
    private int votes;

    // Constructor
    public Candidate(String name) {
        this.name = name;
        this.votes = 0;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for votes
    public int getVotes() {
        return votes;
    }

    // ✅ SETTER (THIS WAS MISSING)
    public void setVotes(int votes) {
        this.votes = votes;
    }

    // Optional (you can use this too)
    public void addVote() {
        this.votes++;
    }
 // Reset votes (REQUIRED for STV)
    public void resetVotes() {
        this.votes = 0;
    }
}