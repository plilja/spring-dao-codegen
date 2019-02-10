package dbtests.mssql.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dbtests.framework.BaseEntity;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OneColumnNaturalIdMsSqlEntity implements BaseEntity<String> {

    @Size(max = 10)
    private String id;

}
