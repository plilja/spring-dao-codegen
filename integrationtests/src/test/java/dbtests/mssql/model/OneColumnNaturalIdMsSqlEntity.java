package dbtests.mssql.model;

import dbtests.framework.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OneColumnNaturalIdMsSqlEntity implements BaseEntity<String> {

    private String id;

}
