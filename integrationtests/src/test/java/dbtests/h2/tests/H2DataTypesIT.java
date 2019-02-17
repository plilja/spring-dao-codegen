package dbtests.h2.tests;

import dbtests.h2.model.DataTypesH2;
import dbtests.h2.model.DataTypesH2Repo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {H2Config.class})
@ExtendWith(SpringExtension.class)
public class H2DataTypesIT {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private DataTypesH2Repo repo;

    @BeforeEach
    void before() {
        jdbcTemplate.update("DELETE FROM public.data_types_h2", new MapSqlParameterSource());
    }

    @Test
    void test() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        OffsetDateTime nowAsOffsetDateTime = now.atOffset(ZoneOffset.UTC);

        var r = new DataTypesH2();
        r.setBigint(123412341234413214L);
        r.setBooleanB(true);
        r.setChaR("d");
        r.setChar10("AAAAAAAAAA");
        r.setDate(now.toLocalDate());
        r.setDecimalEighteenZero(432143143124134321L);
        r.setDecimalNineZero(14312);
        r.setDecimalNineteenZero(new BigInteger("9999999999999999999"));
        r.setDecimalTenTwo(new BigDecimal("4313.45"));
        r.setDecimalTenZero(9999999999L);
        r.setDoublE(0.7);
        r.setFloaT(0.4F);
        r.setGuid(UUID.randomUUID());
        r.setInteger(4711);
        r.setNumericTenTwo(new BigDecimal("12341.11"));
        r.setSmallint(14);
        r.setText("foo bar 123");
        r.setTimestamp(now);
        r.setTimestampTz(nowAsOffsetDateTime);
        r.setTime(now.toLocalTime());
        r.setVarchar10("sabtw");

        // when
        repo.save(r);

        // then
        var r2 = repo.getOne(r.getId());
        assertEquals(r.getId(), r2.getId());
        assertEquals(r.getBigint(), r2.getBigint());
        assertEquals(r.getBooleanB(), r2.getBooleanB());
        assertEquals(r.getChaR(), r2.getChaR());
        assertEquals(r.getChar10(), r2.getChar10());
        assertEquals(r.getDate(), r2.getDate());
        assertEquals(r.getDecimalEighteenZero(), r2.getDecimalEighteenZero());
        assertEquals(r.getDecimalNineteenZero(), r2.getDecimalNineteenZero());
        assertEquals(r.getNumericTenTwo(), r2.getNumericTenTwo());
        assertEquals(r.getDecimalTenZero(), r2.getDecimalTenZero());
        assertEquals(r.getDoublE(), r2.getDoublE(), 1e-10);
        assertEquals(r.getFloaT(), r2.getFloaT(), 1e-10);
        assertEquals(r.getGuid(), r2.getGuid());
        assertEquals(r.getInteger(), r2.getInteger());
        assertEquals(r.getNumericTenTwo(), r2.getNumericTenTwo());
        assertEquals(r.getSmallint(), r2.getSmallint());
        assertEquals(r.getText(), r2.getText());
        assertEquals(r.getTimestamp(), r2.getTimestamp());
        assertEquals(r.getTimestampTz(), r2.getTimestampTz());
        assertEquals(r.getTime(), r2.getTime());
        assertEquals(r.getVarchar10(), r2.getVarchar10());
    }

    @Test
    void timeStampWithTimeZone() {
        OffsetDateTime dateTime = OffsetDateTime.parse("2019-02-15T12:00:00+01:00[Europe/Paris]", DateTimeFormatter.ISO_ZONED_DATE_TIME);
        var r = new DataTypesH2();
        r.setTimestampTz(dateTime);

        // when
        repo.save(r);

        // then
        var retrieved = repo.getOne(r.getId());
        assertEquals(dateTime, retrieved.getTimestampTz());
    }

}
