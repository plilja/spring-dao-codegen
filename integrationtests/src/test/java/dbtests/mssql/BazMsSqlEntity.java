package dbtests.mssql;

import dbtests.framework.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BazMsSqlEntity implements BaseEntity<Integer> {

    private Integer id;
    private String name;

}
