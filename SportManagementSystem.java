import java.util.*;

// ---------------- Player Class ----------------
class Player {
    int id;
    String name;
    String skillLevel;
    int matchesPlayed;
    int runsOrGoals; // for cricket/football etc.

    Player(int id, String name, String skillLevel, int matchesPlayed, int runsOrGoals) {
        this.id = id;
        this.name = name;
        this.skillLevel = skillLevel;
        this.matchesPlayed = matchesPlayed;
        this.runsOrGoals = runsOrGoals;
    }

    void displayPlayer() {
        System.out.println("ID: " + id + " | Name: " + name + " | Skill: " + skillLevel +
                " | Matches: " + matchesPlayed + " | Score: " + runsOrGoals);
    }
}

// ---------------- Team Class ----------------
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

    void displayTeam() {
        System.out.println("\nTeam ID: " + teamId + " | Team Name: " + teamName);
        System.out.println("Players:");
        for (Player p : players) {
            p.displayPlayer();
        }
    }
}

// ---------------- Fixture Class ----------------
class Fixture {
    int fixtureId;
    Team teamA;
    Team teamB;
    String date;
    String venue;
    String result = "Pending";

    Fixture(int id, Team a, Team b, String date, String venue) {
        this.fixtureId = id;
        this.teamA = a;
        this.teamB = b;
        this.date = date;
        this.venue = venue;
    }

    void setResult(String res) {
        this.result = res;
    }

    void displayFixture() {
        System.out.println("\nFixture ID: " + fixtureId + " | " + teamA.teamName + " vs " + teamB.teamName);
        System.out.println("Date: " + date + " | Venue: " + venue + " | Result: " + result);
    }
}

// ---------------- Custom Exception ----------------
class InvalidChoiceException extends Exception {
    InvalidChoiceException(String message) {
        super(message);
    }
}

// ---------------- Main Management System ----------------
public class SportManagementSystem {

    static ArrayList<Team> teams = new ArrayList<>();
    static ArrayList<Fixture> fixtures = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    // -------- Main Menu --------
    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n==== SPORT MANAGEMENT SYSTEM ====");
            System.out.println("1. Manage Teams");
            System.out.println("2. Manage Fixtures");
            System.out.println("3. View All Data");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = sc.nextInt();
                switch (choice) {
                    case 1 -> manageTeams();
                    case 2 -> manageFixtures();
                    case 3 -> displayAll();
                    case 0 -> System.out.println("Exiting System... Goodbye!");
                    default -> throw new InvalidChoiceException("Invalid menu option!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                sc.nextLine(); // clear buffer
                choice = -1;
            } catch (InvalidChoiceException e) {
                System.out.println(e.getMessage());
                choice = -1;
            }
        } while (choice != 0);
    }

    // -------- Manage Teams --------
    static void manageTeams() {
        int ch;
        do {
            System.out.println("\n-- TEAM MANAGEMENT --");
            System.out.println("1. Add Team");
            System.out.println("2. Add Player to Team");
            System.out.println("3. View Teams");
            System.out.println("0. Back");
            System.out.print("Enter your choice: ");
            ch = sc.nextInt();

            switch (ch) {
                case 1 -> addTeam();
                case 2 -> addPlayerToTeam();
                case 3 -> viewTeams();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid choice!");
            }
        } while (ch != 0);
    }

    static void addTeam() {
        System.out.print("Enter Team ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Team Name: ");
        String name = sc.nextLine();
        teams.add(new Team(id, name));
        System.out.println("Team added successfully!");
    }

    static void addPlayerToTeam() {
        if (teams.isEmpty()) {
            System.out.println("No teams available. Add a team first!");
            return;
        }

        System.out.print("Enter Team ID to add player: ");
        int id = sc.nextInt();
        Team t = null;
        for (Team temp : teams)
            if (temp.teamId == id)
                t = temp;

        if (t == null) {
            System.out.println("Team not found!");
            return;
        }

        System.out.print("Enter Player ID: ");
        int pid = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Player Name: ");
        String pname = sc.nextLine();
        System.out.print("Enter Skill Level (Beginner/Intermediate/Pro): ");
        String skill = sc.nextLine();
        System.out.print("Enter Matches Played: ");
        int matches = sc.nextInt();
        System.out.print("Enter Runs/Goals: ");
        int score = sc.nextInt();

        t.addPlayer(new Player(pid, pname, skill, matches, score));
        System.out.println("Player added successfully to " + t.teamName + "!");
    }

    static void viewTeams() {
        if (teams.isEmpty()) System.out.println("No teams available!");
        else for (Team t : teams) t.displayTeam();
    }

    // -------- Manage Fixtures --------
    static void manageFixtures() {
        int ch;
        do {
            System.out.println("\n-- FIXTURE MANAGEMENT --");
            System.out.println("1. Schedule Fixture");
            System.out.println("2. Update Result");
            System.out.println("3. View Fixtures");
            System.out.println("0. Back");
            System.out.print("Enter your choice: ");
            ch = sc.nextInt();

            switch (ch) {
                case 1 -> scheduleFixture();
                case 2 -> updateResult();
                case 3 -> viewFixtures();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid choice!");
            }
        } while (ch != 0);
    }

    static void scheduleFixture() {
        if (teams.size() < 2) {
            System.out.println("Need at least two teams to schedule a fixture!");
            return;
        }
        System.out.print("Enter Fixture ID: ");
        int id = sc.nextInt();
        System.out.print("Enter Team A ID: ");
        int a = sc.nextInt();
        System.out.print("Enter Team B ID: ");
        int b = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Date (DD-MM-YYYY): ");
        String date = sc.nextLine();
        System.out.print("Enter Venue: ");
        String venue = sc.nextLine();

        Team teamA = null, teamB = null;
        for (Team t : teams) {
            if (t.teamId == a) teamA = t;
            if (t.teamId == b) teamB = t;
        }

        if (teamA == null || teamB == null) {
            System.out.println("Invalid team IDs!");
            return;
        }

        fixtures.add(new Fixture(id, teamA, teamB, date, venue));
        System.out.println("Fixture scheduled successfully!");
    }

    static void updateResult() {
        if (fixtures.isEmpty()) {
            System.out.println("No fixtures found!");
            return;
        }
        System.out.print("Enter Fixture ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        for (Fixture f : fixtures) {
            if (f.fixtureId == id) {
                System.out.print("Enter Result (e.g., TeamA Won / TeamB Won / Draw): ");
                String res = sc.nextLine();
                f.setResult(res);
                System.out.println("Result updated successfully!");
                return;
            }
        }
        System.out.println("Fixture not found!");
    }

    static void viewFixtures() {
        if (fixtures.isEmpty()) System.out.println("No fixtures available!");
        else for (Fixture f : fixtures) f.displayFixture();
    }

    // -------- Display All Data --------
    static void displayAll() {
        viewTeams();
        viewFixtures();
    }
}
