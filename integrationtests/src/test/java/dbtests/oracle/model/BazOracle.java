package dbtests.oracle.model;

import dbtests.framework.BaseEntity;
import dbtests.framework.ChangedAtTracked;
import dbtests.framework.ChangedByTracked;
import dbtests.framework.CreatedAtTracked;
import dbtests.framework.CreatedByTracked;
import dbtests.framework.VersionTracked;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BazOracle implements BaseEntity<Integer>, CreatedAtTracked<LocalDateTime>, ChangedAtTracked<LocalDateTime>, CreatedByTracked, ChangedByTracked, VersionTracked {

    private Integer id;
    private LocalDateTime changedAt;
    @Size(max = 50)
    private String changedBy;
    private ColorEnumOracle color;
    private LocalDateTime createdAt;
    @NotNull
    @Size(max = 50)
    private String createdBy;
    @NotNull
    @Size(max = 30)
    private String name;
    private Integer version;

    public BazOracle() {
    }

    public BazOracle(Integer id, LocalDateTime changedAt, String changedBy, ColorEnumOracle color, LocalDateTime createdAt, String createdBy, String name, Integer version) {
        this.id = id;
        this.changedAt = changedAt;
        this.changedBy = changedBy;
        this.color = color;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.name = name;
        this.version = version;
    }

    public ColorEnumOracle getColor() {
        return color;
    }

    public void setColor(ColorEnumOracle color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
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
        return version;
    }

    @Override
    public void setVersion(Integer value) {
        this.version = value;
    }

    @Override
    public String toString() {
        return "BazOracle{id=" + id + ", name=" + name + "}";
    }

}
