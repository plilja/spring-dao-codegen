package dbtests.mssql.model;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BazMsSqlEntity implements BaseEntity<Integer>, CreatedAtTracked<LocalDateTime>, ChangedAtTracked<LocalDateTime>, CreatedByTracked, ChangedByTracked, VersionTracked {

    private Integer id;
    private ColorEnumMsSql color;
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

    @JsonIgnore
    @Override
    public LocalDateTime getCreatedAt() {
        return insertedAt;
    }

    @Override
    public void setCreatedNow() {
        insertedAt = LocalDateTime.now();
    }

    @JsonIgnore
    @Override
    public LocalDateTime getChangedAt() {
        return modifiedAt;
    }

    @Override
    public void setChangedNow() {
        modifiedAt = LocalDateTime.now();
    }

    @JsonIgnore
    @Override
    public String getCreatedBy() {
        return insertedBy;
    }

    @JsonIgnore
    @Override
    public void setCreatedBy(String value) {
        this.insertedBy = value;
    }

    @JsonIgnore
    @Override
    public String getChangedBy() {
        return modifiedBy;
    }

    @JsonIgnore
    @Override
    public void setChangedBy(String value) {
        this.modifiedBy = value;
    }

}
