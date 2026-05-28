package service;
 
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
 
import model.Ballot;
import model.Candidate;
 
import java.util.*;
 
import database.DatabaseConnection;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
 
public class ElectionService {
 
    private static ObservableList<Candidate> candidates =
            FXCollections.observableArrayList();
 
    private static List<Ballot> ballots =
            new ArrayList<>();
 
    private static int seats = 1;
 
    // ✅ ADD CANDIDATE
    public static void addCandidate(String name) {
 
        try {
 
            Connection conn =
                    DatabaseConnection.getConnection();
 
            String query =
                    "INSERT INTO candidates(name, votes) VALUES (?, ?)";
 
            PreparedStatement ps =
                    conn.prepareStatement(query);
 
            ps.setString(1, name);
            ps.setInt(2, 0);
 
            ps.executeUpdate();
 
            candidates.add(new Candidate(name));
 
            System.out.println("Candidate added to database!");
 
        } catch (Exception e) {
 
            e.printStackTrace();
        }
    }
 
    // ✅ LOAD CANDIDATES
    public static void loadCandidatesFromDatabase() {
 
        candidates.clear();
 
        try {
 
            Connection conn =
                    DatabaseConnection.getConnection();
 
            String query =
                    "SELECT * FROM candidates";
 
            PreparedStatement ps =
                    conn.prepareStatement(query);
 
            ResultSet rs =
                    ps.executeQuery();
 
            while (rs.next()) {
 
                String name =
                        rs.getString("name");
 
                int votes =
                        rs.getInt("votes");
 
                Candidate candidate =
                        new Candidate(name);
 
                candidate.setVotes(votes);
 
                candidates.add(candidate);
            }
 
            System.out.println("Candidates loaded!");
 
        } catch (Exception e) {
 
            e.printStackTrace();
        }
    }
 
    // ✅ LOAD BALLOTS FROM DATABASE
    public static void loadBallotsFromDatabase() {
 
        ballots.clear();
 
        try {
 
            Connection conn =
                    DatabaseConnection.getConnection();
 
            String query =
                    "SELECT * FROM ballots";
 
            PreparedStatement ps =
                    conn.prepareStatement(query);
 
            ResultSet rs =
                    ps.executeQuery();
 
            while (rs.next()) {
 
                List<String> prefs = new ArrayList<>();
 
                String first  = rs.getString("first_preference");
                String second = rs.getString("second_preference");
                String third  = rs.getString("third_preference");
 
                if (first  != null) prefs.add(first);
                if (second != null) prefs.add(second);
                if (third  != null) prefs.add(third);
 
                ballots.add(new Ballot(prefs));
            }
 
            System.out.println("Ballots loaded from database!");
 
        } catch (Exception e) {
 
            e.printStackTrace();
        }
    }
 
    // ✅ GET CANDIDATES
    public static ObservableList<Candidate> getCandidates() {
        return candidates;
    }
 
    // ✅ ADD BALLOT TO DATABASE
    public static void addBallot(Ballot ballot) {
 
        ballots.add(ballot);
 
        try {
 
            Connection conn =
                    DatabaseConnection.getConnection();
 
            String query =
                    "INSERT INTO ballots(first_preference, second_preference, third_preference) VALUES (?, ?, ?)";
 
            PreparedStatement ps =
                    conn.prepareStatement(query);
 
            List<String> prefs =
                    ballot.getPreferences();
 
            String first =
                    prefs.size() > 0 ? prefs.get(0) : null;
 
            String second =
                    prefs.size() > 1 ? prefs.get(1) : null;
 
            String third =
                    prefs.size() > 2 ? prefs.get(2) : null;
 
            ps.setString(1, first);
            ps.setString(2, second);
            ps.setString(3, third);
 
            ps.executeUpdate();
 
            System.out.println("Ballot saved to database!");
 
        } catch (Exception e) {
 
            e.printStackTrace();
        }
    }
 
    // ✅ GET BALLOTS
    public static List<Ballot> getBallots() {
        return ballots;
    }
 
    // ✅ SET SEATS
    public static void setSeats(int s) {
        seats = Math.max(1, s);
    }
 
    // 🔥 STV LOGIC
    public static List<Candidate> calculateSTWWinners() {
 
        for (Candidate c : candidates) {
            c.resetVotes();
        }
 
        List<Candidate> activeCandidates =
                new ArrayList<>(candidates);
 
        List<Ballot> activeBallots =
                new ArrayList<>(ballots);
 
        Set<String> eliminated =
                new HashSet<>();
 
        while (true) {
 
            for (Candidate c : activeCandidates) {
                c.resetVotes();
            }
 
            for (Ballot b : activeBallots) {
 
                for (String pref : b.getPreferences()) {
 
                    if (!eliminated.contains(pref)) {
 
                        Candidate candidate =
                                getCandidateByName(pref);
 
                        if (candidate != null) {
                            candidate.addVote();
                        }
 
                        break;
                    }
                }
            }
 
            List<Candidate> roundWinners =
                    new ArrayList<>();
 
            int quota =
                    (activeBallots.size() / (seats + 1)) + 1;
 
            for (Candidate c : activeCandidates) {
 
                if (c.getVotes() >= quota) {
 
                    roundWinners.add(c);
 
                    eliminated.add(c.getName());
                }
            }
 
            if (!roundWinners.isEmpty()) {
 
                activeCandidates.removeAll(roundWinners);
            }
 
            if (activeCandidates.size()
                    <= seats - roundWinners.size()) {
 
                roundWinners.addAll(activeCandidates);
 
                return roundWinners;
            }
 
            Candidate minCandidate = null;
 
            for (Candidate c : activeCandidates) {
 
                if (minCandidate == null
                        || c.getVotes() < minCandidate.getVotes()) {
 
                    minCandidate = c;
                }
            }
 
            if (minCandidate != null) {
 
                eliminated.add(minCandidate.getName());
 
                activeCandidates.remove(minCandidate);
            }
        }
    }
 
    // ✅ HELPER METHOD
    private static Candidate getCandidateByName(String name) {
 
        for (Candidate c : candidates) {
 
            if (c.getName().equals(name)) {
                return c;
            }
        }
 
        return null;
    }
 
    // ✅ RESET — clears database tables AND in-memory state
    public static boolean resetElection() {
 
        try {
 
            Connection conn =
                    DatabaseConnection.getConnection();
 
            // DELETE all ballots from DB
            PreparedStatement deleteBallots =
                    conn.prepareStatement("DELETE FROM ballots");
            deleteBallots.executeUpdate();
 
            // DELETE all candidates from DB
            PreparedStatement deleteCandidates =
                    conn.prepareStatement("DELETE FROM candidates");
            deleteCandidates.executeUpdate();
 
            // RESET has_voted flag for all users
            PreparedStatement resetVotes =
                    conn.prepareStatement("UPDATE users SET has_voted = false");
            resetVotes.executeUpdate();
 
            System.out.println("Database cleared for election reset!");
 
        } catch (Exception e) {
 
            e.printStackTrace();
            return false;
        }
 
        // CLEAR in-memory state
        candidates.clear();
 
        ballots.clear();
 
        seats = 1;
 
        return true;
    }
}