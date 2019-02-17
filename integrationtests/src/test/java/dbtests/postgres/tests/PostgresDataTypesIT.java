package dbtests.postgres.tests;

import dbtests.postgres.model.DataTypesPostgresDao;
import dbtests.postgres.model.DataTypesPostgresEntity;
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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = {PostgresITConfig.class})
@ExtendWith(SpringExtension.class)
public class PostgresDataTypesIT {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private DataTypesPostgresDao repo;

    @BeforeEach
    void before() {
        jdbcTemplate.update("DELETE FROM public.data_types_postgres", new MapSqlParameterSource());
    }

    @Test
    void test() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        OffsetDateTime nowAsOffsetDateTime = now.atOffset(ZoneOffset.UTC);

        DataTypesPostgresEntity r = new DataTypesPostgresEntity();
        r.setBigint(123412341234413214L);
        r.setBooleanB(true);
        r.setBytea(new byte[]{1, 2, 3});
        r.setChaR("d");
        r.setChar10("AAAAAAAAAA");
        r.setDate(nowAsOffsetDateTime.toLocalDate());
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
        r.setSmallint(14); // TODO more compact?
        r.setText("foo bar 123");
        r.setTimestamp(now);
        r.setTimestampTz(nowAsOffsetDateTime);
        r.setTime(now.toLocalTime());
        r.setVarchar10("sabtw");
        r.setXml("<foo>bar</foo>");

        // when
        repo.save(r);

        // then
        DataTypesPostgresEntity r2 = repo.getOne(r.getId());
        assertEquals(r.getId(), r2.getId());
        assertEquals(r.getBigint(), r2.getBigint());
        assertEquals(r.getBooleanB(), r2.getBooleanB());
        assertArrayEquals(r.getBytea(), r2.getBytea());
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
        assertEquals(r.getXml(), r2.getXml());
    }

    @Test
    void timeStampWithTimeZone() {
        var dateTime = OffsetDateTime.parse("2019-02-15T12:00:00+01:00[Europe/Paris]", DateTimeFormatter.ISO_ZONED_DATE_TIME);
        var r = new DataTypesPostgresEntity();
        r.setTimestampTz(dateTime);

        // when
        repo.save(r);

        // then
        var retrieved = repo.getOne(r.getId());
        assertEquals(OffsetDateTime.parse("2019-02-15T11:00Z", DateTimeFormatter.ISO_ZONED_DATE_TIME), retrieved.getTimestampTz());
    }

    @Test
    void infinityDate() {
        jdbcTemplate.update("INSERT INTO public.data_types_postgres(timestamp, timestamp_tz) values ('infinity', 'infinity')", new MapSqlParameterSource());

        List<DataTypesPostgresEntity> res = repo.findAll();

        LocalDateTime farIntoTheFuture = LocalDateTime.now().plusYears(100000);
        assertNotNull(res.get(0).getTimestamp());
        assertTrue(res.get(0).getTimestamp().isAfter(farIntoTheFuture));
        assertNotNull(res.get(0).getTimestampTz().toLocalDateTime());
        assertTrue(res.get(0).getTimestampTz().toLocalDateTime().isAfter(farIntoTheFuture));
    }

    @Test
    void epochDate() {
        jdbcTemplate.update("INSERT INTO public.data_types_postgres(timestamp, timestamp_tz) values ('epoch', 'epoch')", new MapSqlParameterSource());

        List<DataTypesPostgresEntity> res = repo.findAll();

        assertEquals(0L, res.get(0).getTimestamp().toEpochSecond(ZoneOffset.UTC));
        assertEquals(0L, res.get(0).getTimestampTz().toEpochSecond());
    }


}
