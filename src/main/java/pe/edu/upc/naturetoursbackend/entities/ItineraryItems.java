package pe.edu.upc.naturetoursbackend.entities;
import jakarta.persistence.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "itinerary_items")
public class ItineraryItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "itinerary_id", nullable = false)
    private Itineraries itinerary;

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tours tour;

    @Column(name = "planned_date", nullable = false)
    private Date plannedDate;

    @Column(name = "num_people_for_tour", nullable = false)
    private int numPeopleForTour;

    @Column(name = "price_at_moment", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtMoment;

    public ItineraryItems() {
    }

    public ItineraryItems(int id, Itineraries itinerary, Tours tour, Date plannedDate, int numPeopleForTour, BigDecimal priceAtMoment) {
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

    public Date getPlannedDate() {
        return plannedDate;
    }

    public void setPlannedDate(Date plannedDate) {
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
}
