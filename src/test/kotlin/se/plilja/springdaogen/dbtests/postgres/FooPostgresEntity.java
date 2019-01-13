package se.plilja.springdaogen.dbtests.postgres;

import se.plilja.springdaogen.dbtests.framework.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class FooPostgresEntity implements BaseEntity<FooPostgresEntity, Long> {

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

    public Boolean getBooleanBit() {
        return booleanBit;
    }

    public FooPostgresEntity setBooleanBit(Boolean booleanBit) {
        this.booleanBit = booleanBit;
        return this;
    }

    public Boolean getBooleanB() {
        return booleanB;
    }

    public FooPostgresEntity setBooleanB(Boolean booleanB) {
        this.booleanB = booleanB;
        return this;
    }

    public String getChaR() {
        return chaR;
    }

    public FooPostgresEntity setChaR(String chaR) {
        this.chaR = chaR;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public FooPostgresEntity setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public FooPostgresEntity setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public BigDecimal getBigdecimal() {
        return bigdecimal;
    }

    public FooPostgresEntity setBigdecimal(BigDecimal bigdecimal) {
        this.bigdecimal = bigdecimal;
        return this;
    }

    public Float getFloaT() {
        return floaT;
    }

    public FooPostgresEntity setFloaT(Float floaT) {
        this.floaT = floaT;
        return this;
    }

    public Double getDoublE() {
        return doublE;
    }

    public FooPostgresEntity setDoublE(Double doublE) {
        this.doublE = doublE;
        return this;
    }

    public UUID getGuid() {
        return guid;
    }

    public FooPostgresEntity setGuid(UUID guid) {
        this.guid = guid;
        return this;
    }

    public String getXml() {
        return xml;
    }

    public FooPostgresEntity setXml(String xml) {
        this.xml = xml;
        return this;
    }

    public String getText() {
        return text;
    }

    public FooPostgresEntity setText(String text) {
        this.text = text;
        return this;
    }

    public byte[] getBytea() {
        return bytea;
    }

    public FooPostgresEntity setBytea(byte[] bytea) {
        this.bytea = bytea;
        return this;
    }

    public Long getId() {
        return fooId;
    }

    public FooPostgresEntity setId(Long id) {
        this.fooId = id;
        return this;
    }

}
