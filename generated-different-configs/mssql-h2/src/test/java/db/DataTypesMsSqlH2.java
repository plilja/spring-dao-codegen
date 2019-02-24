package db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataTypesMsSqlH2 implements BaseEntity<Long> {

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
    private String text;
    private LocalTime time;
    private Integer tinyint;
    private byte[] varbinary10;
    @Size(max = 10)
    private String varchar10;
    private String xml;

    public DataTypesMsSqlH2() {
    }

    public DataTypesMsSqlH2(Long id, byte[] binary10, Boolean bit, String chaR, String char10, LocalDate date, LocalDateTime datetime, LocalDateTime datetime2, Long decimalEighteenZero, Integer decimalNineZero, BigInteger decimalNineteenZero, BigDecimal decimalTenTwo, Long decimalTenZero, Float floaT, Integer inT, BigDecimal money, String nchar10, String ntext, String nvarchar10, Float real, Integer smallint, BigDecimal smallmoney, String text, LocalTime time, Integer tinyint, byte[] varbinary10, String varchar10, String xml) {
        this.id = id;
        this.binary10 = binary10;
        this.bit = bit;
        this.chaR = chaR;
        this.char10 = char10;
        this.date = date;
        this.datetime = datetime;
        this.datetime2 = datetime2;
        this.decimalEighteenZero = decimalEighteenZero;
        this.decimalNineZero = decimalNineZero;
        this.decimalNineteenZero = decimalNineteenZero;
        this.decimalTenTwo = decimalTenTwo;
        this.decimalTenZero = decimalTenZero;
        this.floaT = floaT;
        this.inT = inT;
        this.money = money;
        this.nchar10 = nchar10;
        this.ntext = ntext;
        this.nvarchar10 = nvarchar10;
        this.real = real;
        this.smallint = smallint;
        this.smallmoney = smallmoney;
        this.text = text;
        this.time = time;
        this.tinyint = tinyint;
        this.varbinary10 = varbinary10;
        this.varchar10 = varchar10;
        this.xml = xml;
    }

    public byte[] getBinary10() {
        return binary10;
    }

    public void setBinary10(byte[] binary10) {
        this.binary10 = binary10;
    }

    public Boolean getBit() {
        return bit;
    }

    public void setBit(Boolean bit) {
        this.bit = bit;
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

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public LocalDateTime getDatetime2() {
        return datetime2;
    }

    public void setDatetime2(LocalDateTime datetime2) {
        this.datetime2 = datetime2;
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

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getNchar10() {
        return nchar10;
    }

    public void setNchar10(String nchar10) {
        this.nchar10 = nchar10;
    }

    public String getNtext() {
        return ntext;
    }

    public void setNtext(String ntext) {
        this.ntext = ntext;
    }

    public String getNvarchar10() {
        return nvarchar10;
    }

    public void setNvarchar10(String nvarchar10) {
        this.nvarchar10 = nvarchar10;
    }

    public Float getReal() {
        return real;
    }

    public void setReal(Float real) {
        this.real = real;
    }

    public Integer getSmallint() {
        return smallint;
    }

    public void setSmallint(Integer smallint) {
        this.smallint = smallint;
    }

    public BigDecimal getSmallmoney() {
        return smallmoney;
    }

    public void setSmallmoney(BigDecimal smallmoney) {
        this.smallmoney = smallmoney;
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

    public Integer getTinyint() {
        return tinyint;
    }

    public void setTinyint(Integer tinyint) {
        this.tinyint = tinyint;
    }

    public byte[] getVarbinary10() {
        return varbinary10;
    }

    public void setVarbinary10(byte[] varbinary10) {
        this.varbinary10 = varbinary10;
    }

    public String getVarchar10() {
        return varchar10;
    }

    public void setVarchar10(String varchar10) {
        this.varchar10 = varchar10;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
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
