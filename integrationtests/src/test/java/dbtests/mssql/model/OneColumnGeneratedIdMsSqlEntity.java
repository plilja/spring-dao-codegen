package dbtests.mssql.model;

import dbtests.framework.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OneColumnGeneratedIdMsSqlEntity implements BaseEntity<Integer> {

    private Integer id;

}
