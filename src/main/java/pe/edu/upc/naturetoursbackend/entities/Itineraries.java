package pe.edu.upc.naturetoursbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Itineraries")
public class Itineraries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idItineraries;
    
    @Column(name = "title", length = 30, nullable = false)
    private String title;
    
    @Column(name = "startDate", nullable = false)
    private LocalDate startDate;
    
    @Column(name = "endDate", nullable = false)
    private LocalDate endDate;
    
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

    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ItineraryItems> items = new ArrayList<>();

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

    public Itineraries(int idItineraries, String title, LocalDate startDate, LocalDate endDate, int numPeople, 
                       BigDecimal totalEstimatedPrice, boolean status, Date createdAt, Date updatedAt, 
                       Users user, QuizProfiles quiz, List<ItineraryItems> items) {
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
        this.items = items;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
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

    public List<ItineraryItems> getItems() {
        return items;
    }

    public void setItems(List<ItineraryItems> items) {
        this.items = items;
    }

    public void addItem(ItineraryItems item) {
        items.add(item);
        item.setItinerary(this);
    }

    public void removeItem(ItineraryItems item) {
        items.remove(item);
        item.setItinerary(null);
    }
}