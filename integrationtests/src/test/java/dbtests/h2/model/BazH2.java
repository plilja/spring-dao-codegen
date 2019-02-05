package dbtests.h2.model;

import dbtests.framework.BaseEntity;
import dbtests.framework.ChangedAtTracked;
import dbtests.framework.CreatedAtTracked;
import dbtests.framework.VersionTracked;
import java.time.LocalDateTime;

public class BazH2 implements BaseEntity<Integer>, CreatedAtTracked<LocalDateTime>, ChangedAtTracked<LocalDateTime>, VersionTracked {

    private Integer bazId;
    private String bazName;
    private LocalDateTime changedAt;
    private LocalDateTime createdAt;
    private Integer version;

    public BazH2() {
    }

    public BazH2(Integer bazId, String bazName, LocalDateTime changedAt, LocalDateTime createdAt, Integer version) {
        this.bazId = bazId;
        this.bazName = bazName;
        this.changedAt = changedAt;
        this.createdAt = createdAt;
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
        return version;
    }

    @Override
    public void setVersion(Integer value) {
        this.version = value;
    }

}
