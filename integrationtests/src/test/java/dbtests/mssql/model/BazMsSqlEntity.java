package dbtests.mssql.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dbtests.framework.BaseEntity;
import dbtests.framework.ChangedAtTracked;
import dbtests.framework.CreatedAtTracked;
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
public class BazMsSqlEntity implements BaseEntity<Integer>, CreatedAtTracked<LocalDateTime>, ChangedAtTracked<LocalDateTime>, VersionTracked {

    private Integer id;
    private ColorEnumMsSql color;
    private LocalDateTime insertedAt;
    private LocalDateTime modifiedAt;
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

}
