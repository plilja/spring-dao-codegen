package dbtests.h2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dbtests.framework.BaseEntity;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataTypesH2 implements BaseEntity<Long> {

    private Long id;
    private Long bigint;
    private Boolean booleanB;
    @Size(max = 1)
    private String chaR;
    @Size(max = 10)
    private String char10;
    private LocalDate date;
    private Long decimalEighteenZero;
    private Integer decimalNineZero;
    private BigInteger decimalNineteenZero;
    private BigDecimal decimalTenTwo;
    private Long decimalTenZero;
    private Double doublE;
    private Float floaT;
    private UUID guid;
    private Integer integer;
    private BigDecimal numericTenTwo;
    private Integer smallint;
    private String text;
    private LocalDateTime timestamp;
    @Size(max = 10)
    private String varchar10;

    public DataTypesH2() {
    }

    public DataTypesH2(Long id, Long bigint, Boolean booleanB, String chaR, String char10, LocalDate date, Long decimalEighteenZero, Integer decimalNineZero, BigInteger decimalNineteenZero, BigDecimal decimalTenTwo, Long decimalTenZero, Double doublE, Float floaT, UUID guid, Integer integer, BigDecimal numericTenTwo, Integer smallint, String text, LocalDateTime timestamp, String varchar10) {
        this.id = id;
        this.bigint = bigint;
        this.booleanB = booleanB;
        this.chaR = chaR;
        this.char10 = char10;
        this.date = date;
        this.decimalEighteenZero = decimalEighteenZero;
        this.decimalNineZero = decimalNineZero;
        this.decimalNineteenZero = decimalNineteenZero;
        this.decimalTenTwo = decimalTenTwo;
        this.decimalTenZero = decimalTenZero;
        this.doublE = doublE;
        this.floaT = floaT;
        this.guid = guid;
        this.integer = integer;
        this.numericTenTwo = numericTenTwo;
        this.smallint = smallint;
        this.text = text;
        this.timestamp = timestamp;
        this.varchar10 = varchar10;
    }

    public Long getBigint() {
        return bigint;
    }

    public void setBigint(Long bigint) {
        this.bigint = bigint;
    }

    public Boolean getBooleanB() {
        return booleanB;
    }

    public void setBooleanB(Boolean booleanB) {
        this.booleanB = booleanB;
    }

    public String getChaR() {
        return chaR;
    }

    public void setChaR(String chaR) {
        this.chaR = chaR;
    }

    public String getChar10() {
        return char10;
    }

    public void setChar10(String char10) {
        this.char10 = char10;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getDecimalEighteenZero() {
        return decimalEighteenZero;
    }

    public void setDecimalEighteenZero(Long decimalEighteenZero) {
        this.decimalEighteenZero = decimalEighteenZero;
    }

    public Integer getDecimalNineZero() {
        return decimalNineZero;
    }

    public void setDecimalNineZero(Integer decimalNineZero) {
        this.decimalNineZero = decimalNineZero;
    }

    public BigInteger getDecimalNineteenZero() {
        return decimalNineteenZero;
    }

    public void setDecimalNineteenZero(BigInteger decimalNineteenZero) {
        this.decimalNineteenZero = decimalNineteenZero;
    }

    public BigDecimal getDecimalTenTwo() {
        return decimalTenTwo;
    }

    public void setDecimalTenTwo(BigDecimal decimalTenTwo) {
        this.decimalTenTwo = decimalTenTwo;
    }

    public Long getDecimalTenZero() {
        return decimalTenZero;
    }

    public void setDecimalTenZero(Long decimalTenZero) {
        this.decimalTenZero = decimalTenZero;
    }

    public Double getDoublE() {
        return doublE;
    }

    public void setDoublE(Double doublE) {
        this.doublE = doublE;
    }

    public Float getFloaT() {
        return floaT;
    }

    public void setFloaT(Float floaT) {
        this.floaT = floaT;
    }

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public BigDecimal getNumericTenTwo() {
        return numericTenTwo;
    }

    public void setNumericTenTwo(BigDecimal numericTenTwo) {
        this.numericTenTwo = numericTenTwo;
    }

    public Integer getSmallint() {
        return smallint;
    }

    public void setSmallint(Integer smallint) {
        this.smallint = smallint;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getVarchar10() {
        return varchar10;
    }

    public void setVarchar10(String varchar10) {
        this.varchar10 = varchar10;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

}
