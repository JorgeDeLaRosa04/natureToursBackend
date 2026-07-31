package pe.edu.upc.naturetoursbackend.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Tours")
public class Tours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "slug", nullable = false, length = 150, unique = true)
    private String slug;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "shortDescription", length = 255, nullable = false)
    private String shortDescription;

    @Column(name = "full_description", columnDefinition = "text", nullable = false)
    private String fullDescription;

    @Column(name = "duration_days", nullable = false)
    private int duration_days;

    @Column(name = "duration_hours", nullable = false)
    private int duration_hours;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "difficulty_level", nullable = false)
    private String difficulty_level;

    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Column(name = "latitude", precision = 10, scale = 7, nullable = true)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7, nullable = true)
    private BigDecimal longitude;

    @Column(name = "map_icon_type", length = 50, nullable = false)
    private String mapIconType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "includes", columnDefinition = "jsonb", nullable = false)
    private List<String> includes;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "excludes", columnDefinition = "jsonb", nullable = false)
    private List<String> excludes;

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    @Column(nullable = false)
    private Boolean enabled = true;

    public Tours() {
    }

    public Tours(int id, String slug, String name, String shortDescription, String fullDescription, int duration_days, int duration_hours, BigDecimal price, String difficulty_level, String category, BigDecimal latitude, BigDecimal longitude, String mapIconType, List<String> includes, List<String> excludes, String imageUrl, Boolean enabled) {
        this.id = id;
        this.slug = slug;
        this.name = name;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.duration_days = duration_days;
        this.duration_hours = duration_hours;
        this.price = price;
        this.difficulty_level = difficulty_level;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mapIconType = mapIconType;
        this.includes = includes;
        this.excludes = excludes;
        this.imageUrl = imageUrl;
        this.enabled = enabled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public int getDuration_days() {
        return duration_days;
    }

    public void setDuration_days(int duration_days) {
        this.duration_days = duration_days;
    }

    public int getDuration_hours() {
        return duration_hours;
    }

    public void setDuration_hours(int duration_hours) {
        this.duration_hours = duration_hours;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDifficulty_level() {
        return difficulty_level;
    }

    public void setDifficulty_level(String difficulty_level) {
        this.difficulty_level = difficulty_level;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getMapIconType() {
        return mapIconType;
    }

    public void setMapIconType(String mapIconType) {
        this.mapIconType = mapIconType;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public void setIncludes(List<String> includes) {
        this.includes = includes;
    }

    public List<String> getExcludes() {
        return excludes;
    }

    public void setExcludes(List<String> excludes) {
        this.excludes = excludes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
