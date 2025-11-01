import javax.swing.*;
import java.awt.*;
import java.util.*;

class Player {
    int id;
    String name;
    String position;
    int matchesPlayed;
    int runsOrGoals;

    Player(int id, String name, String position, int matchesPlayed, int runsOrGoals) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.matchesPlayed = matchesPlayed;
        this.runsOrGoals = runsOrGoals;
    }

    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Position: " + position +
               " | Matches: " + matchesPlayed + " | Runs/Goals: " + runsOrGoals;
    }
}

class Team {
    int teamId;
    String teamName;
    ArrayList<Player> players = new ArrayList<>();

    Team(int id, String name) {
        this.teamId = id;
        this.teamName = name;
    }

    void addPlayer(Player p) {
        players.add(p);
    }

    public String toString() {
        return teamName;
    }
}

class Fixture {
    int fixtureId;
    Team teamA, teamB;
    String date, venue, result = "Pending";

    Fixture(int id, Team a, Team b, String date, String venue) {
        this.fixtureId = id;
        this.teamA = a;
        this.teamB = b;
        this.date = date;
        this.venue = venue;
    }

    public String toString() {
        return "Fixture ID: " + fixtureId + " | " + teamA + " vs " + teamB +
               " | Date: " + date + " | Venue: " + venue + " | Result: " + result;
    }
}

public class SportManagementGUI extends JFrame {
    ArrayList<Team> teams = new ArrayList<>();
    ArrayList<Fixture> fixtures = new ArrayList<>();
    JTextArea outputArea = new JTextArea(10, 40);

    SportManagementGUI() {
        setTitle("Sport Management System");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);

        JLabel heading = new JLabel("SPORT MANAGEMENT SYSTEM", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 28));
        heading.setForeground(Color.RED);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(heading, BorderLayout.NORTH);

        JButton btnAddTeam = new JButton("Add Team");
        JButton btnAddPlayer = new JButton("Add Player");
        JButton btnScheduleFixture = new JButton("Schedule Fixture");
        JButton btnViewTeams = new JButton("View Teams");
        JButton btnViewPlayer = new JButton("View Player by ID");
        JButton btnViewFixtures = new JButton("View All Fixtures");

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JButton[] buttons = {btnAddTeam, btnAddPlayer, btnScheduleFixture, btnViewTeams, btnViewPlayer, btnViewFixtures};
        int y = 0;
        for (JButton b : buttons) {
            gbc.gridy = y++;
            b.setFont(new Font("Arial", Font.BOLD, 16));
            b.setBackground(Color.DARK_GRAY);
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            b.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    b.setBackground(Color.RED);
                    b.setForeground(Color.BLACK);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    b.setBackground(Color.DARK_GRAY);
                    b.setForeground(Color.WHITE);
                }
            });
            panel.add(b, gbc);
        }

        add(panel, BorderLayout.CENTER);
        outputArea.setBackground(Color.BLACK);
        outputArea.setForeground(Color.GREEN);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        btnAddTeam.addActionListener(e -> addTeam());
        btnAddPlayer.addActionListener(e -> addPlayer());
        btnScheduleFixture.addActionListener(e -> scheduleFixture());
        btnViewTeams.addActionListener(e -> viewTeams());
        btnViewPlayer.addActionListener(e -> viewPlayerById());
        btnViewFixtures.addActionListener(e -> viewFixtures());

        setVisible(true);
        setLocationRelativeTo(null);
    }

    void addTeam() {
        try {
            String idStr = JOptionPane.showInputDialog("Enter Team ID:");
            if (idStr == null) return;
            int id = Integer.parseInt(idStr);
            String name = JOptionPane.showInputDialog("Enter Team Name:");
            if (name == null || name.trim().isEmpty()) return;

            for (Team t : teams) {
                if (t.teamId == id) {
                    JOptionPane.showMessageDialog(this, "Error: Team ID already exists!");
                    return;
                }
                if (t.teamName.equalsIgnoreCase(name)) {
                    JOptionPane.showMessageDialog(this, "Error: Team Name already exists!");
                    return;
                }
            }

            teams.add(new Team(id, name));
            outputArea.append("Team added: " + name + "\n");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Team ID! Please enter a number.");
        }
    }

    void addPlayer() {
        if (teams.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No teams available!");
            return;
        }

        try {
            String teamIdStr = JOptionPane.showInputDialog("Enter Team ID:");
            if (teamIdStr == null) return;
            int id = Integer.parseInt(teamIdStr);
            Team team = null;
            for (Team t : teams)
                if (t.teamId == id)
                    team = t;

            if (team == null) {
                JOptionPane.showMessageDialog(this, "Team not found!");
                return;
            }

            int pid = Integer.parseInt(JOptionPane.showInputDialog("Enter Player ID (Integer Only):"));
            String pname = JOptionPane.showInputDialog("Enter Player Name:");
            String position = JOptionPane.showInputDialog("Enter Player Position:");
            int matches = Integer.parseInt(JOptionPane.showInputDialog("Enter Matches Played:"));
            int score = Integer.parseInt(JOptionPane.showInputDialog("Enter Runs/Goals:"));

            team.addPlayer(new Player(pid, pname, position, matches, score));
            outputArea.append("Player added to " + team.teamName + ": " + pname + "\n");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Input! Player ID, Matches, and Runs/Goals must be integers.");
        }
    }

    void scheduleFixture() {
        if (teams.size() < 2) {
            JOptionPane.showMessageDialog(this, "Need at least two teams!");
            return;
        }

        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Fixture ID:"));
            int a = Integer.parseInt(JOptionPane.showInputDialog("Enter Team A ID:"));
            int b = Integer.parseInt(JOptionPane.showInputDialog("Enter Team B ID:"));
            String date = JOptionPane.showInputDialog("Enter Date (DD-MM-YYYY):");
            String venue = JOptionPane.showInputDialog("Enter Venue:");

            Team teamA = null, teamB = null;
            for (Team t : teams) {
                if (t.teamId == a) teamA = t;
                if (t.teamId == b) teamB = t;
            }

            if (teamA == null || teamB == null) {
                JOptionPane.showMessageDialog(this, "Invalid team IDs!");
                return;
            }

            fixtures.add(new Fixture(id, teamA, teamB, date, venue));
            outputArea.append("Fixture Scheduled: " + teamA + " vs " + teamB + " on " + date + "\n");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Input! IDs must be integers.");
        }
    }

    void viewTeams() {
        outputArea.append("\n=== Teams ===\n");
        for (Team t : teams) {
            outputArea.append("Team: " + t.teamName + "\n");
            for (Player p : t.players)
                outputArea.append("  - " + p.name + " (" + p.position + ")\n");
        }
    }

    void viewPlayerById() {
        try {
            String idStr = JOptionPane.showInputDialog("Enter Player ID to view:");
            if (idStr == null) return;
            int pid = Integer.parseInt(idStr);

            for (Team t : teams) {
                for (Player p : t.players) {
                    if (p.id == pid) {
                        outputArea.append("\n" + p.toString() + "\n");
                        return;
                    }
                }
            }

            JOptionPane.showMessageDialog(this, "Player not found!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Input! Please enter a valid Player ID (integer).");
        }
    }

    void viewFixtures() {
        outputArea.append("\n=== Fixtures ===\n");
        for (Fixture f : fixtures)
            outputArea.append(f.toString() + "\n");
    }

    public static void main(String[] args) {
        new SportManagementGUI();
    }
}

