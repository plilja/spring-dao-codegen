package db.h2;

import java.time.LocalDateTime;

public class BazH2 implements BaseEntity<Integer> {

    private Integer bazId;
    private String bazName;
    private LocalDateTime changedAt;
    private String changedBy;
    private String color;
    private LocalDateTime createdAt;
    private String createdBy;
    private Integer version;

    public BazH2() {
    }

    public BazH2(Integer bazId, String bazName, LocalDateTime changedAt, String changedBy, String color, LocalDateTime createdAt, String createdBy, Integer version) {
        this.bazId = bazId;
        this.bazName = bazName;
        this.changedAt = changedAt;
        this.changedBy = changedBy;
        this.color = color;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.version = version;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public Integer getId() {
        return bazId;
    }

    @Override
    public void setId(Integer id) {
        this.bazId = id;
    }

}
