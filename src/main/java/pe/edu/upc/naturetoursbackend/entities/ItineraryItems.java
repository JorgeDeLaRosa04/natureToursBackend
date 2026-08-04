package pe.edu.upc.naturetoursbackend.entities;
import jakarta.persistence.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "itinerary_items")
public class ItineraryItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "itinerary_id", nullable = false)
    @JsonBackReference
    private Itineraries itinerary;

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tours tour;

    @Column(name = "planned_date", nullable = false)
    private LocalDate plannedDate;

    @Column(name = "num_people_for_tour", nullable = false)
    private int numPeopleForTour;

    @Column(name = "price_at_moment", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtMoment;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDate now = LocalDate.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }

    public ItineraryItems() {
    }

    public ItineraryItems(int id, Itineraries itinerary, Tours tour, LocalDate plannedDate, int numPeopleForTour, BigDecimal priceAtMoment) {
        this.id = id;
        this.itinerary = itinerary;
        this.tour = tour;
        this.plannedDate = plannedDate;
        this.numPeopleForTour = numPeopleForTour;
        this.priceAtMoment = priceAtMoment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Itineraries getItinerary() {
        return itinerary;
    }

    public void setItinerary(Itineraries itinerary) {
        this.itinerary = itinerary;
    }

    public Tours getTour() {
        return tour;
    }

    public void setTour(Tours tour) {
        this.tour = tour;
    }

    public LocalDate getPlannedDate() {
        return plannedDate;
    }

    public void setPlannedDate(LocalDate plannedDate) {
        this.plannedDate = plannedDate;
    }

    public int getNumPeopleForTour() {
        return numPeopleForTour;
    }

    public void setNumPeopleForTour(int numPeopleForTour) {
        this.numPeopleForTour = numPeopleForTour;
    }

    public BigDecimal getPriceAtMoment() {
        return priceAtMoment;
    }

    public void setPriceAtMoment(BigDecimal priceAtMoment) {
        this.priceAtMoment = priceAtMoment;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
}
