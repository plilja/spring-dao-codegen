package dbtests.oracle.model;

import dbtests.framework.BaseEntity;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DataTypesOracle implements BaseEntity<String> {

    @Size(max = 100)
    private String id;
    @NotNull
    private Double binaryDouble;
    @NotNull
    private Float binaryFloat;
    @NotNull
    private byte[] blob;
    @NotNull
    @Size(max = 1)
    private String char1;
    @NotNull
    @Size(max = 10)
    private String char10;
    @NotNull
    private String clob;
    @NotNull
    private LocalDate date;
    @NotNull
    private String nlob;
    @NotNull
    private Long numberEighteenZero;
    @NotNull
    private Integer numberNineZero;
    @NotNull
    private BigInteger numberNineteenZero;
    @NotNull
    private BigDecimal numberTenTwo;
    @NotNull
    private Long numberTenZero;
    @NotNull
    private LocalDateTime timestamp;
    @NotNull
    private OffsetDateTime timestampTz;
    @NotNull
    @Size(max = 100)
    private String varchar;
    @NotNull
    @Size(max = 100)
    private String varchar2;

    public DataTypesOracle() {
    }

    public DataTypesOracle(String id, Double binaryDouble, Float binaryFloat, byte[] blob, String char1, String char10, String clob, LocalDate date, String nlob, Long numberEighteenZero, Integer numberNineZero, BigInteger numberNineteenZero, BigDecimal numberTenTwo, Long numberTenZero, LocalDateTime timestamp, OffsetDateTime timestampTz, String varchar, String varchar2) {
        this.id = id;
        this.binaryDouble = binaryDouble;
        this.binaryFloat = binaryFloat;
        this.blob = blob;
        this.char1 = char1;
        this.char10 = char10;
        this.clob = clob;
        this.date = date;
        this.nlob = nlob;
        this.numberEighteenZero = numberEighteenZero;
        this.numberNineZero = numberNineZero;
        this.numberNineteenZero = numberNineteenZero;
        this.numberTenTwo = numberTenTwo;
        this.numberTenZero = numberTenZero;
        this.timestamp = timestamp;
        this.timestampTz = timestampTz;
        this.varchar = varchar;
        this.varchar2 = varchar2;
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

    public String getChar1() {
        return char1;
    }

    public void setChar1(String char1) {
        this.char1 = char1;
    }

    public String getChar10() {
        return char10;
    }

    public void setChar10(String char10) {
        this.char10 = char10;
    }

    public String getClob() {
        return clob;
    }

    public void setClob(String clob) {
        this.clob = clob;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNlob() {
        return nlob;
    }

    public void setNlob(String nlob) {
        this.nlob = nlob;
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

    public OffsetDateTime getTimestampTz() {
        return timestampTz;
    }

    public void setTimestampTz(OffsetDateTime timestampTz) {
        this.timestampTz = timestampTz;
    }

    public String getVarchar() {
        return varchar;
    }

    public void setVarchar(String varchar) {
        this.varchar = varchar;
    }

    public String getVarchar2() {
        return varchar2;
    }

    public void setVarchar2(String varchar2) {
        this.varchar2 = varchar2;
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
