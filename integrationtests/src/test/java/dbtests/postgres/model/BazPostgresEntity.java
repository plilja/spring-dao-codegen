package dbtests.postgres.model;

import dbtests.framework.BaseEntity;
import dbtests.framework.ChangedAtTracked;
import dbtests.framework.CreatedAtTracked;
import dbtests.framework.VersionTracked;
import java.time.LocalDateTime;

public class BazPostgresEntity implements BaseEntity<Integer>, CreatedAtTracked<LocalDateTime>, ChangedAtTracked<LocalDateTime>, VersionTracked {

    private Integer bazId;
    private String bazName;
    private LocalDateTime changedAt;
    private Integer counter;
    private LocalDateTime createdAt;

    public BazPostgresEntity() {
    }

    public BazPostgresEntity(Integer bazId, String bazName, LocalDateTime changedAt, Integer counter, LocalDateTime createdAt) {
        this.bazId = bazId;
        this.bazName = bazName;
        this.changedAt = changedAt;
        this.counter = counter;
        this.createdAt = createdAt;
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
    public Integer getVersion() {
        return counter;
    }

    @Override
    public void setVersion(Integer value) {
        this.counter = value;
    }

}
