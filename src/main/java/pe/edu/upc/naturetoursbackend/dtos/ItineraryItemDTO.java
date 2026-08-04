package pe.edu.upc.naturetoursbackend.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ItineraryItemDTO {
    private int id;
    private int tourId;
    private LocalDate plannedDate;
    private int numPeopleForTour;
    private BigDecimal priceAtMoment;

    public ItineraryItemDTO() {
    }

    public ItineraryItemDTO(int id, int tourId, LocalDate plannedDate, int numPeopleForTour, BigDecimal priceAtMoment) {
        this.id = id;
        this.tourId = tourId;
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

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
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
}
