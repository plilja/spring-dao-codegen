package dbtests.mssql.model;

import dbtests.framework.BaseEntity;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OneColumnNaturalIdMsSqlEntity implements BaseEntity<String> {

    @Size(max = 10)
    private String id;

}
