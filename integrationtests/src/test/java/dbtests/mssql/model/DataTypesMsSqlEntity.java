package dbtests.mssql.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dbtests.framework.BaseEntity;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataTypesMsSqlEntity implements BaseEntity<Long> {

    private Long id;
    private byte[] binary10;
    private Boolean bit;
    @Size(max = 1)
    private String chaR;
    @Size(max = 10)
    private String char10;
    private LocalDate date;
    private LocalDateTime datetime;
    private LocalDateTime datetime2;
    private Long decimalEighteenZero;
    private Integer decimalNineZero;
    private BigInteger decimalNineteenZero;
    private BigDecimal decimalTenTwo;
    private Long decimalTenZero;
    private Float floaT;
    private Integer inT;
    private BigDecimal money;
    @Size(max = 10)
    private String nchar10;
    @Size(max = 1073741823)
    private String ntext;
    @Size(max = 10)
    private String nvarchar10;
    private Float real;
    private Integer smallint;
    private BigDecimal smallmoney;
    @Size(max = 2147483647)
    private String text;
    private LocalTime time;
    private Integer tinyint;
    private byte[] varbinary10;
    @Size(max = 10)
    private String varchar10;
    @Size(max = 2147483647)
    private String xml;

}
