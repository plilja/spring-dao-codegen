package db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BazMsSqlH2 implements BaseEntity<Integer>, VersionTracked {

    private Integer id;
    private ColorEnumMsSql color;
    @NotNull
    private LocalDateTime insertedAt;
    @NotNull
    @Size(max = 50)
    private String insertedBy;
    private LocalDateTime modifiedAt;
    @Size(max = 50)
    private String modifiedBy;
    @NotNull
    @Size(max = 30)
    private String name;
    @JsonIgnore
    private Integer version;

    public BazMsSqlH2() {
    }

    public BazMsSqlH2(Integer id, ColorEnumMsSql color, LocalDateTime insertedAt, String insertedBy, LocalDateTime modifiedAt, String modifiedBy, String name, Integer version) {
        this.id = id;
        this.color = color;
        this.insertedAt = insertedAt;
        this.insertedBy = insertedBy;
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
        this.name = name;
        this.version = version;
    }

    public ColorEnumMsSql getColor() {
        return color;
    }

    public void setColor(ColorEnumMsSql color) {
        this.color = color;
    }

    public LocalDateTime getInsertedAt() {
        return insertedAt;
    }

    public void setInsertedAt(LocalDateTime insertedAt) {
        this.insertedAt = insertedAt;
    }

    public String getInsertedBy() {
        return insertedBy;
    }

    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
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
    public Integer getVersion() {
        return version;
    }

    @Override
    public void setVersion(Integer value) {
        this.version = value;
    }

}
