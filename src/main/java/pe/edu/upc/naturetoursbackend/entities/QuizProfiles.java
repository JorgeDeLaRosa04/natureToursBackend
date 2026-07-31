package pe.edu.upc.naturetoursbackend.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "QuizProfiles")
public class QuizProfiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idQuizProfiles;
    @Column(name = "interestType", length = 30,nullable = false)
    private String interestType;
    @Column(name = "availableDays", nullable = false)
    private int availableDays;
    @Column(name = "adventureLevel", length = 30, nullable = false)
    private String adventureLevel;
    @Column(name = "createdAt", nullable = false, updatable = false)
    private Date createdAt;
    @ManyToOne
    @JoinColumn(name = "idUser", nullable = false)
    private Users user;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    public QuizProfiles() {
    }

    public QuizProfiles(int idQuizProfiles, String interestType, int availableDays, String adventureLevel, Date createdAt, Users user) {
        this.idQuizProfiles = idQuizProfiles;
        this.interestType = interestType;
        this.availableDays = availableDays;
        this.adventureLevel = adventureLevel;
        this.createdAt = createdAt;
        this.user = user;
    }

    public int getIdQuizProfiles() {
        return idQuizProfiles;
    }

    public void setIdQuizProfiles(int idQuizProfiles) {
        this.idQuizProfiles = idQuizProfiles;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public int getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(int availableDays) {
        this.availableDays = availableDays;
    }

    public String getAdventureLevel() {
        return adventureLevel;
    }

    public void setAdventureLevel(String adventureLevel) {
        this.adventureLevel = adventureLevel;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}