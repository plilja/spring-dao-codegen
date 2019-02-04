package dbtests.postgres.model;

import dbtests.framework.BaseEntity;
import java.time.LocalDateTime;

public class BazPostgresEntity implements BaseEntity<Integer> {

    private Integer bazId;
    private String bazName;
    private LocalDateTime changedAt;
    private LocalDateTime createdAt;

    public BazPostgresEntity() {
    }

    public BazPostgresEntity(Integer bazId, String bazName, LocalDateTime changedAt, LocalDateTime createdAt) {
        this.bazId = bazId;
        this.bazName = bazName;
        this.changedAt = changedAt;
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

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
