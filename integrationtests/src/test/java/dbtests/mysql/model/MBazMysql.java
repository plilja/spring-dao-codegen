package dbtests.mysql.model;

import dbtests.framework.BaseEntity;
import dbtests.framework.ChangedAtTracked;
import dbtests.framework.CreatedAtTracked;
import dbtests.framework.VersionTracked;
import java.time.LocalDateTime;

public class MBazMysql implements BaseEntity<Integer>, CreatedAtTracked<LocalDateTime>, ChangedAtTracked<LocalDateTime>, VersionTracked {

    private Integer id;
    private LocalDateTime changedAt;
    private ColorEnumMysql colorEnumMysql;
    private LocalDateTime createdAt;
    private String name;
    private Integer version;

    public MBazMysql() {
    }

    public MBazMysql(Integer id, LocalDateTime changedAt, ColorEnumMysql colorEnumMysql, LocalDateTime createdAt, String name, Integer version) {
        this.id = id;
        this.changedAt = changedAt;
        this.colorEnumMysql = colorEnumMysql;
        this.createdAt = createdAt;
        this.name = name;
        this.version = version;
    }

    public ColorEnumMysql getColorEnumMysql() {
        return colorEnumMysql;
    }

    public void setColorEnumMysql(ColorEnumMysql colorEnumMysql) {
        this.colorEnumMysql = colorEnumMysql;
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
