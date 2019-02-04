package dbtests.oracle.model;

import dbtests.framework.BaseEntity;
import java.time.LocalDateTime;

public class BazOracle implements BaseEntity<Integer> {

    private Integer id;
    private LocalDateTime changedAt;
    private LocalDateTime createdAt;
    private String name;

    public BazOracle() {
    }

    public BazOracle(Integer id, LocalDateTime changedAt, LocalDateTime createdAt, String name) {
        this.id = id;
        this.changedAt = changedAt;
        this.createdAt = createdAt;
        this.name = name;
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

}
