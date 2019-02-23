package db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BazMysqlH2 implements BaseEntity<Integer>, CreatedAtTracked<LocalDateTime>, ChangedAtTracked<LocalDateTime>, CreatedByTracked, ChangedByTracked, VersionTracked {

    private Integer id;
    private LocalDateTime changedAt;
    @NotNull
    @Size(max = 50)
    private String changedBy;
    private ColorEnumMysql colorEnumMysql;
    private LocalDateTime createdAt;
    @NotNull
    @Size(max = 50)
    private String createdBy;
    @NotNull
    @Size(max = 30)
    private String name;
    @JsonIgnore
    private Integer version;

    public BazMysqlH2() {
    }

    public BazMysqlH2(Integer id, LocalDateTime changedAt, String changedBy, ColorEnumMysql colorEnumMysql, LocalDateTime createdAt, String createdBy, String name, Integer version) {
        this.id = id;
        this.changedAt = changedAt;
        this.changedBy = changedBy;
        this.colorEnumMysql = colorEnumMysql;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
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
