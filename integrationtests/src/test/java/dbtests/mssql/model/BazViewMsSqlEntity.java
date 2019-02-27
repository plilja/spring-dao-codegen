package dbtests.mssql.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BazViewMsSqlEntity {

    @Size(max = 30)
    private String color;
    @NotNull
    private Integer id;
    @NotNull
    @Size(max = 30)
    private String name;

}
