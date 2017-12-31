package mk.ukim.finki.np.midterms.second;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Partial exam II 2016/2017
 */
public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}


class FootballTable {
    private HashMap<String, Team> teams;

    public FootballTable() {
        teams = new HashMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        Team teamHome = teams.computeIfAbsent(homeTeam, k -> new Team(homeTeam));
        Team teamAway = teams.computeIfAbsent(awayTeam, k -> new Team(awayTeam));

        if (homeGoals == awayGoals) {
            // draw
            teamHome.setGamesDrawn(teamHome.getGamesDrawn() + 1);
            teamAway.setGamesDrawn(teamAway.getGamesDrawn() + 1);
        } else if (homeGoals > awayGoals) {
            // homeTeam won
            teamHome.setGamesWon(teamHome.getGamesWon() + 1);
            teamAway.setGamesLost(teamAway.getGamesLost() + 1);
        } else if (homeGoals < awayGoals) {
            // awayTeam won
            teamHome.setGamesLost(teamHome.getGamesLost() + 1);
            teamAway.setGamesWon(teamAway.getGamesWon() + 1);
        }

        teamHome.setGoalsGiven(teamHome.getGoalsGiven() + homeGoals);
        teamHome.setGoalsTaken(teamHome.getGoalsTaken() + awayGoals);
        teamAway.setGoalsGiven(teamAway.getGoalsGiven() + awayGoals);
        teamAway.setGoalsTaken(teamAway.getGoalsTaken() + homeGoals);
    }

    public void printTable() {
        List<Team> teamsList = teams.values().stream()
                .sorted(Comparator.comparing(Team::getPoints)
                        .thenComparing(Team::getGoalDifference).reversed()
                        .thenComparing(Team::getName))
                .collect(Collectors.toList());
        IntStream
                .range(0, teamsList.size())
                .forEach(i -> System.out.printf("%2d. %s\n", i + 1, teamsList.get(i)));
    }
}


class Team {
    private String name;
    private int goalsGiven;
    private int goalsTaken;
    private int gamesWon;
    private int gamesLost;
    private int gamesDrawn;

    public Team(String name) {
        this.name = name;
        this.goalsGiven = 0;
        this.goalsTaken = 0;
        this.gamesWon = 0;
        this.gamesLost = 0;
        this.gamesDrawn = 0;
    }

    public String getName() {
        return name;
    }

    public int getGoalsGiven() {
        return goalsGiven;
    }

    public int getGoalsTaken() {
        return goalsTaken;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public int getGamesDrawn() {
        return gamesDrawn;
    }

    public int getGoalDifference() {
        return goalsGiven - goalsTaken;
    }

    public int getPoints() {
        return gamesWon * 3 + gamesDrawn;
    }

    public void setGoalsGiven(int goalsGiven) {
        this.goalsGiven = goalsGiven;
    }

    public void setGoalsTaken(int goalsTaken) {
        this.goalsTaken = goalsTaken;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public void setGamesLost(int gamesLost) {
        this.gamesLost = gamesLost;
    }

    public void setGamesDrawn(int gamesDrawn) {
        this.gamesDrawn = gamesDrawn;
    }

    private int getGamesPlayed() {
        return gamesWon + gamesDrawn + gamesLost;
    }

    @Override
    public String toString() {
        return String
                .format("%-15s%5d%5d%5d%5d%5d", name, getGamesPlayed(), gamesWon, gamesDrawn, gamesLost, getPoints());
    }
}
