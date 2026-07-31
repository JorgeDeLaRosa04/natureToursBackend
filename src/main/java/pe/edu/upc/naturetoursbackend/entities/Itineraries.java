package pe.edu.upc.naturetoursbackend.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "Itineraries")
public class Itineraries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idItineraries;
    @Column(name = "title", length = 30, nullable = false)
    private String title;
    @Column(name = "startDate", nullable = false)
    private Date startDate;
    @Column(name = "endDate", nullable = false)
    private Date endDate;
    @Column(name = "numPeople", nullable = false)
    private int numPeople;
    @Column(name = "totalEstimatedPrice", nullable = false)
    private BigDecimal totalEstimatedPrice;
    @Column(name = "status", nullable = false)
    private boolean status = true;
    @Column(name = "createdAt", nullable = false, updatable = false)
    private Date createdAt;
    @Column(name = "updatedAt", nullable = false)
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "idUser", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "idQuizProfiles", nullable = false)
    private QuizProfiles quiz;

    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }

    public Itineraries() {
    }

    public Itineraries(int idItineraries, String title, Date startDate, Date endDate, int numPeople, BigDecimal totalEstimatedPrice, boolean status, Date createdAt, Date updatedAt, Users user, QuizProfiles quiz) {
        this.idItineraries = idItineraries;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numPeople = numPeople;
        this.totalEstimatedPrice = totalEstimatedPrice;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user;
        this.quiz = quiz;
    }

    public int getIdItineraries() {
        return idItineraries;
    }

    public void setIdItineraries(int idItineraries) {
        this.idItineraries = idItineraries;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    public BigDecimal getTotalEstimatedPrice() {
        return totalEstimatedPrice;
    }

    public void setTotalEstimatedPrice(BigDecimal totalEstimatedPrice) {
        this.totalEstimatedPrice = totalEstimatedPrice;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public QuizProfiles getQuiz() {
        return quiz;
    }

    public void setQuiz(QuizProfiles quiz) {
        this.quiz = quiz;
    }
}