package dbtests.postgres;

import dbtests.framework.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class FooPostgresEntity implements BaseEntity<Long> {

    private Long fooId;
    private Boolean booleanBit;
    private Boolean booleanB;
    private String chaR;
    private LocalDate date;
    private LocalDateTime timestamp;
    private BigDecimal bigdecimal;
    private Float floaT;
    private Double doublE;
    private UUID guid;
    private String xml;
    private String text;
    private byte[] bytea;

    public FooPostgresEntity() {
    }

    public FooPostgresEntity(Long fooId, Boolean booleanBit, Boolean booleanB, String chaR, LocalDate date, LocalDateTime timestamp, BigDecimal bigdecimal, Float floaT, Double doublE, UUID guid, String xml, String text, byte[] bytea) {
        this.fooId = fooId;
        this.booleanBit = booleanBit;
        this.booleanB = booleanB;
        this.chaR = chaR;
        this.date = date;
        this.timestamp = timestamp;
        this.bigdecimal = bigdecimal;
        this.floaT = floaT;
        this.doublE = doublE;
        this.guid = guid;
        this.xml = xml;
        this.text = text;
        this.bytea = bytea;
    }

    public Long getFooId() {
        return fooId;
    }

    public void setFooId(Long fooId) {
        this.fooId = fooId;
    }

    public Boolean getBooleanBit() {
        return booleanBit;
    }

    public void setBooleanBit(Boolean booleanBit) {
        this.booleanBit = booleanBit;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getBigdecimal() {
        return bigdecimal;
    }

    public void setBigdecimal(BigDecimal bigdecimal) {
        this.bigdecimal = bigdecimal;
    }

    public Float getFloaT() {
        return floaT;
    }

    public void setFloaT(Float floaT) {
        this.floaT = floaT;
    }

    public Double getDoublE() {
        return doublE;
    }

    public void setDoublE(Double doublE) {
        this.doublE = doublE;
    }

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public byte[] getBytea() {
        return bytea;
    }

    public void setBytea(byte[] bytea) {
        this.bytea = bytea;
    }

    public Long getId() {
        return fooId;
    }

    public void setId(Long id) {
        this.fooId = id;
    }

}
