package ru.vldf.sportsportal.model.tourney;

import ru.vldf.sportsportal.model.user.UserEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "TeamComposition", schema = "sportsportal")
public class TeamCompositionEntity {
    private Integer id;
    private String teamName;
    private Integer shiftBalance;

    private TeamEntity teamByTourneyId;
    private TourneyEntity tourneyByTourneyId;

    private Collection<UserEntity> users;
    private Collection<TeamPlayerEntity> players;

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "TeamName", nullable = false, length = 45)
    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Basic
    @Column(name = "ShiftBalance", nullable = false)
    public Integer getShiftBalance() {
        return shiftBalance;
    }

    public void setShiftBalance(Integer shiftBalance) {
        this.shiftBalance = shiftBalance;
    }

//    ==================================================================================
//    === MANY-TO-ONE REFERENCES

    @ManyToOne
    @JoinColumn(name = "Team_ID", referencedColumnName = "ID", nullable = false)
    public TeamEntity getTeamByTourneyId() {
        return teamByTourneyId;
    }

    public void setTeamByTourneyId(TeamEntity teamByTourneyId) {
        this.teamByTourneyId = teamByTourneyId;
    }

    @ManyToOne
    @JoinColumn(name = "Tourney_ID", referencedColumnName = "ID", nullable = false)
    public TourneyEntity getTourneyByTourneyId() {
        return tourneyByTourneyId;
    }

    public void setTourneyByTourneyId(TourneyEntity tourneyByTourneyId) {
        this.tourneyByTourneyId = tourneyByTourneyId;
    }

//    ==================================================================================
//    === MANY-TO-MANY REFERENCES

    @ManyToMany
    @JoinTable(
            name = "TeamMembershipForUser",
            joinColumns = @JoinColumn(name = "TeamComposition_ID"),
            inverseJoinColumns = @JoinColumn(name = "TeamUser_ID")
    )
    public Collection<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Collection<UserEntity> users) {
        this.users = users;
    }

    @ManyToMany
    @JoinTable(
            name = "TeamMembershipForPlayer",
            joinColumns = @JoinColumn(name = "TeamComposition_ID"),
            inverseJoinColumns = @JoinColumn(name = "TeamPlayer_ID")
    )
    public Collection<TeamPlayerEntity> getPlayers() {
        return players;
    }

    public void setPlayers(Collection<TeamPlayerEntity> players) {
        this.players = players;
    }

//    ==================================================================================
//    === OBJECTS METHODS

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeamCompositionEntity that = (TeamCompositionEntity) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
