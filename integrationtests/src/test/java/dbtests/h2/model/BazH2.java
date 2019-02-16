package dbtests.h2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dbtests.framework.BaseEntity;
import dbtests.framework.ChangedAtTracked;
import dbtests.framework.ChangedByTracked;
import dbtests.framework.CreatedAtTracked;
import dbtests.framework.CreatedByTracked;
import dbtests.framework.VersionTracked;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BazH2 implements BaseEntity<Integer>, CreatedAtTracked<LocalDateTime>, ChangedAtTracked<LocalDateTime>, CreatedByTracked, ChangedByTracked, VersionTracked {

    private Integer bazId;
    @Size(max = 100)
    private String bazName;
    private LocalDateTime changedAt;
    @Size(max = 50)
    private String changedBy;
    private ColorEnumH2 color;
    private LocalDateTime createdAt;
    @NotNull
    @Size(max = 50)
    private String createdBy;
    @JsonIgnore
    private Integer version;

    public BazH2() {
    }

    public BazH2(Integer bazId, String bazName, LocalDateTime changedAt, String changedBy, ColorEnumH2 color, LocalDateTime createdAt, String createdBy, Integer version) {
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

    public ColorEnumH2 getColor() {
        return color;
    }

    public void setColor(ColorEnumH2 color) {
        this.color = color;
    }

    @JsonIgnore
    @Override
    public Integer getId() {
        return bazId;
    }

    @JsonIgnore
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
        return version;
    }

    @Override
    public void setVersion(Integer value) {
        this.version = value;
    }

}
