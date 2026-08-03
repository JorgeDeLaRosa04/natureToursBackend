package pe.edu.upc.naturetoursbackend.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ItinerariesDTO {
    private int idItineraries;
    private String title;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    
    private int numPeople;
    private BigDecimal totalEstimatedPrice;
    private Long idUser;
    private int idQuiz;
    private List<ItineraryItemDTO> items;

    public ItinerariesDTO() {
    }

    public ItinerariesDTO(int idItineraries, String title, LocalDate startDate, LocalDate endDate, int numPeople, 
                          BigDecimal totalEstimatedPrice, Long idUser, int idQuiz, List<ItineraryItemDTO> items) {
        this.idItineraries = idItineraries;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numPeople = numPeople;
        this.totalEstimatedPrice = totalEstimatedPrice;
        this.idUser = idUser;
        this.idQuiz = idQuiz;
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

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public int getIdQuiz() {
        return idQuiz;
    }

    public void setIdQuiz(int idQuiz) {
        this.idQuiz = idQuiz;
    }

    public List<ItineraryItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItineraryItemDTO> items) {
        this.items = items;
    }
}
