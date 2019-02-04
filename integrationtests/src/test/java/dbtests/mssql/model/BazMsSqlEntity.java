package dbtests.mssql.model;

import dbtests.framework.BaseEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BazMsSqlEntity implements BaseEntity<Integer> {

    private Integer id;
    private LocalDateTime insertedAt;
    private LocalDateTime modifiedAt;
    private String name;

}
