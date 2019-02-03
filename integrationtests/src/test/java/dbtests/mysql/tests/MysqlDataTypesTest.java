package dbtests.mysql.tests;

import dbtests.mysql.model.MDataTypesMysql;
import dbtests.mysql.model.MDataTypesMysqlRepo;
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
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {MysqlITConfig.class})
@ExtendWith(SpringExtension.class)
public class MysqlDataTypesTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private MDataTypesMysqlRepo repo;

    @BeforeEach
    void before() {
        jdbcTemplate.update("DELETE FROM DATA_TYPES_MYSQL", new MapSqlParameterSource());
    }

    @Test
    void test() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS); // Nano seconds gets truncated by DB

        MDataTypesMysql r = new MDataTypesMysql();
        r.setBigint(214234123412341234L);
        r.setBit(true);
        r.setBlob(new byte[]{1, 2, 3});
        r.setBool(true);
        r.setDate(now.toLocalDate());
        r.setDatetime(now);
        r.setDecimalEighteenZero(999999999999999999L);
        r.setDecimalNineZero(999999999);
        r.setDecimalNineteenZero(new BigInteger("9999999999999999999"));
        r.setDecimalTenTwo(new BigDecimal("42314123.34"));
        r.setDecimalTenZero(9999999999L);
        r.setDoublE(234.3435);
        r.setFloaT(3432.321F);
        r.setInT(4711);
        r.setInteger(4712);
        r.setJson("{\"value\": 3}");
        r.setMediumint(4322);
        r.setSmallint(342);
        r.setText("adfjhasdf3qghvwrwhw4");
        r.setTime(now.toLocalTime());
        r.setTimestamp(now);
        r.setTinyblob(new byte[]{4, 5, 6});
        r.setTinyint(127);
        r.setVarchar10("AAAAAAAAAA");
        r.setVarcharBinary10(new byte[]{7, 8, 9});
        r.setYear(2019);

        // when
        repo.save(r);

        // then
        MDataTypesMysql r2 = repo.getOne(r.getId());
        assertEquals(r.getId(), r2.getId());
        assertEquals(r.getBigint(), r2.getBigint());
        assertEquals(r.getBit(), r2.getBit());
        assertEquals(r.getBool(), r2.getBool());
        assertArrayEquals(r.getBlob(), r2.getBlob());
        assertEquals(r.getDate(), r2.getDate());
        assertEquals(r.getDatetime(), r2.getDatetime());
        assertEquals(r.getDecimalEighteenZero(), r2.getDecimalEighteenZero());
        assertEquals(r.getDecimalNineZero(), r2.getDecimalNineZero());
        assertEquals(r.getDecimalNineteenZero(), r2.getDecimalNineteenZero());
        assertEquals(r.getDecimalTenTwo(), r2.getDecimalTenTwo());
        assertEquals(r.getDecimalTenZero(), r2.getDecimalTenZero());
        assertEquals(r.getDoublE(), r2.getDoublE(), 1e-9);
        assertEquals(r.getFloaT(), r2.getFloaT(), 1e-3);
        assertEquals(r.getInT(), r2.getInT());
        assertEquals(r.getInteger(), r2.getInteger());
        assertEquals(r.getJson(), r2.getJson());
        assertEquals(r.getMediumint(), r2.getMediumint());
        assertEquals(r.getSmallint(), r2.getSmallint());
        assertEquals(r.getText(), r2.getText());
        assertEquals(r.getTime(), now.toLocalTime());
        assertEquals(r.getTimestamp(), r2.getTimestamp());
        assertArrayEquals(r.getTinyblob(), r2.getTinyblob());
        assertEquals(r.getTinyint(), r2.getTinyint());
        assertEquals(r.getVarchar10(), r2.getVarchar10());
        assertArrayEquals(r.getVarcharBinary10(), r2.getVarcharBinary10());
        assertEquals(r.getYear(), r2.getYear());
    }

}
