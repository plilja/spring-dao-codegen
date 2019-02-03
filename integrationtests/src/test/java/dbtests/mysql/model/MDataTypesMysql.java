package dbtests.mysql.model;

import dbtests.framework.BaseEntity;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class MDataTypesMysql implements BaseEntity<Long> {

    private Long id;
    private Long bigint;
    private Boolean bit;
    private byte[] blob;
    private Boolean bool;
    private LocalDate date;
    private LocalDateTime datetime;
    private Long decimalEighteenZero;
    private Integer decimalNineZero;
    private BigInteger decimalNineteenZero;
    private BigDecimal decimalTenTwo;
    private Long decimalTenZero;
    private Double doublE;
    private Float floaT;
    private Integer inT;
    private Integer integer;
    private String json;
    private Integer mediumint;
    private Integer smallint;
    private String text;
    private LocalTime time;
    private LocalDateTime timestamp;
    private byte[] tinyblob;
    private Integer tinyint;
    private String varchar10;
    private byte[] varcharBinary10;
    private LocalDate year;

    public MDataTypesMysql() {
    }

    public MDataTypesMysql(Long id, Long bigint, Boolean bit, byte[] blob, Boolean bool, LocalDate date, LocalDateTime datetime, Long decimalEighteenZero, Integer decimalNineZero, BigInteger decimalNineteenZero, BigDecimal decimalTenTwo, Long decimalTenZero, Double doublE, Float floaT, Integer inT, Integer integer, String json, Integer mediumint, Integer smallint, String text, LocalTime time, LocalDateTime timestamp, byte[] tinyblob, Integer tinyint, String varchar10, byte[] varcharBinary10, LocalDate year) {
        this.id = id;
        this.bigint = bigint;
        this.bit = bit;
        this.blob = blob;
        this.bool = bool;
        this.date = date;
        this.datetime = datetime;
        this.decimalEighteenZero = decimalEighteenZero;
        this.decimalNineZero = decimalNineZero;
        this.decimalNineteenZero = decimalNineteenZero;
        this.decimalTenTwo = decimalTenTwo;
        this.decimalTenZero = decimalTenZero;
        this.doublE = doublE;
        this.floaT = floaT;
        this.inT = inT;
        this.integer = integer;
        this.json = json;
        this.mediumint = mediumint;
        this.smallint = smallint;
        this.text = text;
        this.time = time;
        this.timestamp = timestamp;
        this.tinyblob = tinyblob;
        this.tinyint = tinyint;
        this.varchar10 = varchar10;
        this.varcharBinary10 = varcharBinary10;
        this.year = year;
    }

    public Long getBigint() {
        return bigint;
    }

    public void setBigint(Long bigint) {
        this.bigint = bigint;
    }

    public Boolean getBit() {
        return bit;
    }

    public void setBit(Boolean bit) {
        this.bit = bit;
    }

    public byte[] getBlob() {
        return blob;
    }

    public void setBlob(byte[] blob) {
        this.blob = blob;
    }

    public Boolean getBool() {
        return bool;
    }

    public void setBool(Boolean bool) {
        this.bool = bool;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
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

    public Integer getInT() {
        return inT;
    }

    public void setInT(Integer inT) {
        this.inT = inT;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Integer getMediumint() {
        return mediumint;
    }

    public void setMediumint(Integer mediumint) {
        this.mediumint = mediumint;
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

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] getTinyblob() {
        return tinyblob;
    }

    public void setTinyblob(byte[] tinyblob) {
        this.tinyblob = tinyblob;
    }

    public Integer getTinyint() {
        return tinyint;
    }

    public void setTinyint(Integer tinyint) {
        this.tinyint = tinyint;
    }

    public String getVarchar10() {
        return varchar10;
    }

    public void setVarchar10(String varchar10) {
        this.varchar10 = varchar10;
    }

    public byte[] getVarcharBinary10() {
        return varcharBinary10;
    }

    public void setVarcharBinary10(byte[] varcharBinary10) {
        this.varcharBinary10 = varcharBinary10;
    }

    public LocalDate getYear() {
        return year;
    }

    public void setYear(LocalDate year) {
        this.year = year;
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
