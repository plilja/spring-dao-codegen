package dbtests.oracle.model;

import dbtests.framework.BaseEntity;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DataTypesOracle implements BaseEntity<String> {

    private String id;
    private Double binaryDouble;
    private Float binaryFloat;
    private byte[] blob;
    private String CHAR1;
    private String CHAR10;
    private byte[] clob;
    private LocalDate date;
    private Long numberEighteenZero;
    private Integer numberNineZero;
    private BigInteger numberNineteenZero;
    private BigDecimal numberTenTwo;
    private Long numberTenZero;
    private LocalDateTime timestamp;
    private String varchar;
    private String VARCHAR2;

    public DataTypesOracle() {
    }

    public DataTypesOracle(String id, Double binaryDouble, Float binaryFloat, byte[] blob, String CHAR1, String CHAR10, byte[] clob, LocalDate date, Long numberEighteenZero, Integer numberNineZero, BigInteger numberNineteenZero, BigDecimal numberTenTwo, Long numberTenZero, LocalDateTime timestamp, String varchar, String VARCHAR2) {
        this.id = id;
        this.binaryDouble = binaryDouble;
        this.binaryFloat = binaryFloat;
        this.blob = blob;
        this.CHAR1 = CHAR1;
        this.CHAR10 = CHAR10;
        this.clob = clob;
        this.date = date;
        this.numberEighteenZero = numberEighteenZero;
        this.numberNineZero = numberNineZero;
        this.numberNineteenZero = numberNineteenZero;
        this.numberTenTwo = numberTenTwo;
        this.numberTenZero = numberTenZero;
        this.timestamp = timestamp;
        this.varchar = varchar;
        this.VARCHAR2 = VARCHAR2;
    }

    public Double getBinaryDouble() {
        return binaryDouble;
    }

    public void setBinaryDouble(Double binaryDouble) {
        this.binaryDouble = binaryDouble;
    }

    public Float getBinaryFloat() {
        return binaryFloat;
    }

    public void setBinaryFloat(Float binaryFloat) {
        this.binaryFloat = binaryFloat;
    }

    public byte[] getBlob() {
        return blob;
    }

    public void setBlob(byte[] blob) {
        this.blob = blob;
    }

    public String getCHAR1() {
        return CHAR1;
    }

    public void setCHAR1(String CHAR1) {
        this.CHAR1 = CHAR1;
    }

    public String getCHAR10() {
        return CHAR10;
    }

    public void setCHAR10(String CHAR10) {
        this.CHAR10 = CHAR10;
    }

    public byte[] getClob() {
        return clob;
    }

    public void setClob(byte[] clob) {
        this.clob = clob;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getNumberEighteenZero() {
        return numberEighteenZero;
    }

    public void setNumberEighteenZero(Long numberEighteenZero) {
        this.numberEighteenZero = numberEighteenZero;
    }

    public Integer getNumberNineZero() {
        return numberNineZero;
    }

    public void setNumberNineZero(Integer numberNineZero) {
        this.numberNineZero = numberNineZero;
    }

    public BigInteger getNumberNineteenZero() {
        return numberNineteenZero;
    }

    public void setNumberNineteenZero(BigInteger numberNineteenZero) {
        this.numberNineteenZero = numberNineteenZero;
    }

    public BigDecimal getNumberTenTwo() {
        return numberTenTwo;
    }

    public void setNumberTenTwo(BigDecimal numberTenTwo) {
        this.numberTenTwo = numberTenTwo;
    }

    public Long getNumberTenZero() {
        return numberTenZero;
    }

    public void setNumberTenZero(Long numberTenZero) {
        this.numberTenZero = numberTenZero;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getVarchar() {
        return varchar;
    }

    public void setVarchar(String varchar) {
        this.varchar = varchar;
    }

    public String getVARCHAR2() {
        return VARCHAR2;
    }

    public void setVARCHAR2(String VARCHAR2) {
        this.VARCHAR2 = VARCHAR2;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
