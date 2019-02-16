package dbtests.postgres.model;

import dbtests.framework.BaseEntity;
import dbtests.framework.ChangedAtTracked;
import dbtests.framework.ChangedByTracked;
import dbtests.framework.CreatedAtTracked;
import dbtests.framework.CreatedByTracked;
import dbtests.framework.VersionTracked;
import java.time.LocalDateTime;

public class BazPostgresEntity implements BaseEntity<Integer>, CreatedAtTracked<LocalDateTime>, ChangedAtTracked<LocalDateTime>, CreatedByTracked, ChangedByTracked, VersionTracked {

    private Integer bazId;
    private String bazName;
    private LocalDateTime changedAt;
    private String changedBy;
    private ColorEnumPostgres color;
    private Integer counter;
    private LocalDateTime createdAt;
    private String createdBy;

    public BazPostgresEntity() {
    }

    public BazPostgresEntity(Integer bazId, String bazName, LocalDateTime changedAt, String changedBy, ColorEnumPostgres color, Integer counter, LocalDateTime createdAt, String createdBy) {
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

    public ColorEnumPostgres getColor() {
        return color;
    }

    public void setColor(ColorEnumPostgres color) {
        this.color = color;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
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
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime value) {
        this.createdAt = value;
    }

    @Override
    public void setCreatedNow() {
        createdAt = LocalDateTime.now();
    }

    @Override
    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime value) {
        this.changedAt = value;
    }

    @Override
    public void setChangedNow() {
        changedAt = LocalDateTime.now();
    }

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String value) {
        this.createdBy = value;
    }

    @Override
    public String getChangedBy() {
        return changedBy;
    }

    @Override
    public void setChangedBy(String value) {
        this.changedBy = value;
    }

    @Override
    public Integer getVersion() {
        return counter;
    }

    @Override
    public void setVersion(Integer value) {
        this.counter = value;
    }

}
