package dbtests.mssql.model;

import dbtests.framework.BaseEntity;
import dbtests.framework.ChangedAtTracked;
import dbtests.framework.CreatedAtTracked;
import dbtests.framework.VersionTracked;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BazMsSqlEntity implements BaseEntity<Integer>, CreatedAtTracked<LocalDateTime>, ChangedAtTracked<LocalDateTime>, VersionTracked {

    private Integer id;
    private ColorEnumMsSql color;
    private LocalDateTime insertedAt;
    private LocalDateTime modifiedAt;
    private String name;
    private Integer version;

    @Override
    public LocalDateTime getCreatedAt() {
        return insertedAt;
    }

    @Override
    public void setCreatedNow() {
        insertedAt = LocalDateTime.now();
    }

    @Override
    public LocalDateTime getChangedAt() {
        return modifiedAt;
    }

    @Override
    public void setChangedNow() {
        modifiedAt = LocalDateTime.now();
    }

}
