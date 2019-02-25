package db.h2;

import java.time.LocalDateTime;

public class BazPostgres implements BaseEntity<Integer> {

    private Integer bazId;
    private String bazName;
    private LocalDateTime changedAt;
    private String changedBy;
    private String color;
    private Long counter;
    private LocalDateTime createdAt;
    private String createdBy;

    public BazPostgres() {
    }

    public BazPostgres(Integer bazId, String bazName, LocalDateTime changedAt, String changedBy, String color, Long counter, LocalDateTime createdAt, String createdBy) {
        this.bazId = bazId;
        this.bazName = bazName;
        this.changedAt = changedAt;
        this.changedBy = changedBy;
        this.color = color;
        this.counter = counter;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public Integer getBazId() {
        return bazId;
    }

    public void setBazId(Integer bazId) {
        this.bazId = bazId;
    }

    public String getBazName() {
        return bazName;
    }

    public void setBazName(String bazName) {
        this.bazName = bazName;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getCounter() {
        return counter;
    }

    public void setCounter(Long counter) {
        this.counter = counter;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public Integer getId() {
        return bazId;
    }

    @Override
    public void setId(Integer id) {
        this.bazId = id;
    }

    @Override
    public String toString() {
        return "BazPostgres{bazId=" + bazId + ", bazName=" + bazName + "}";
    }

}
