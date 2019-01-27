package dbtests.postgres.model;

import dbtests.framework.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class FooPostgresEntity implements BaseEntity<Long> {

    private Long fooId;
    private BigDecimal bigdecimal;
    private Boolean booleanB;
    private Boolean booleanBit;
    private byte[] bytea;
    private String chaR;
    private LocalDate date;
    private Double doublE;
    private Float floaT;
    private UUID guid;
    private String text;
    private LocalDateTime timestamp;
    private String xml;

    public FooPostgresEntity() {
    }

    public FooPostgresEntity(Long fooId, BigDecimal bigdecimal, Boolean booleanB, Boolean booleanBit, byte[] bytea, String chaR, LocalDate date, Double doublE, Float floaT, UUID guid, String text, LocalDateTime timestamp, String xml) {
        this.fooId = fooId;
        this.bigdecimal = bigdecimal;
        this.booleanB = booleanB;
        this.booleanBit = booleanBit;
        this.bytea = bytea;
        this.chaR = chaR;
        this.date = date;
        this.doublE = doublE;
        this.floaT = floaT;
        this.guid = guid;
        this.text = text;
        this.timestamp = timestamp;
        this.xml = xml;
    }

    public Long getFooId() {
        return fooId;
    }

    public void setFooId(Long fooId) {
        this.fooId = fooId;
    }

    public BigDecimal getBigdecimal() {
        return bigdecimal;
    }

    public void setBigdecimal(BigDecimal bigdecimal) {
        this.bigdecimal = bigdecimal;
    }

    public Boolean getBooleanB() {
        return booleanB;
    }

    public void setBooleanB(Boolean booleanB) {
        this.booleanB = booleanB;
    }

    public Boolean getBooleanBit() {
        return booleanBit;
    }

    public void setBooleanBit(Boolean booleanBit) {
        this.booleanBit = booleanBit;
    }

    public byte[] getBytea() {
        return bytea;
    }

    public void setBytea(byte[] bytea) {
        this.bytea = bytea;
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

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    @Override
    public Long getId() {
        return fooId;
    }

    @Override
    public void setId(Long id) {
        this.fooId = id;
    }

}
