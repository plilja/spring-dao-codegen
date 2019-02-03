package dbtests.mssql.model;

import dbtests.framework.BaseEntity;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataTypesMsSqlEntity implements BaseEntity<Long> {

    private Long id;
    private byte[] binary10;
    private Boolean bit;
    private String chaR;
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
    private String nchar10;
    private String ntext;
    private String nvarchar10;
    private Float real;
    private Integer smallint;
    private BigDecimal smallmoney;
    private String text;
    private LocalTime time;
    private Integer tinyint;
    private byte[] varbinary10;
    private String varchar10;
    private String xml;

}
