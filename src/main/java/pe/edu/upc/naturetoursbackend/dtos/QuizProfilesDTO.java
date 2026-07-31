package pe.edu.upc.naturetoursbackend.dtos;

import java.util.Date;

public class QuizProfilesDTO {
    private int idQuizProfiles;
    private String interestType;
    private int availableDays;
    private String adventureLevel;
    private Date createdAt;
    private Long idUser;


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

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }
}
