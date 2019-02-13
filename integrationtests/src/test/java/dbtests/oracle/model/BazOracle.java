package dbtests.oracle.model;

import dbtests.framework.BaseEntity;
import dbtests.framework.ChangedAtTracked;
import dbtests.framework.CreatedAtTracked;
import dbtests.framework.VersionTracked;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BazOracle implements BaseEntity<Integer>, CreatedAtTracked<LocalDateTime>, ChangedAtTracked<LocalDateTime>, VersionTracked {

    private Integer id;
    private LocalDateTime changedAt;
    private ColorEnumOracle color;
    private LocalDateTime createdAt;
    @NotNull
    @Size(max = 30)
    private String name;
    private Integer version;

    public BazOracle() {
    }

    public BazOracle(Integer id, LocalDateTime changedAt, ColorEnumOracle color, LocalDateTime createdAt, String name, Integer version) {
        this.id = id;
        this.changedAt = changedAt;
        this.color = color;
        this.createdAt = createdAt;
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
    public Integer getVersion() {
        return version;
    }

    @Override
    public void setVersion(Integer value) {
        this.version = value;
    }

}
