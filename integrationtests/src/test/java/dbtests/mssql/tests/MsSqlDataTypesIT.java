package dbtests.mssql.tests;

import dbtests.mssql.model.DataTypesMsSqlDao;
import dbtests.mssql.model.DataTypesMsSqlEntity;
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

@ContextConfiguration(classes = {MsSqlITConfig.class})
@ExtendWith(SpringExtension.class)
public class MsSqlDataTypesIT {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private DataTypesMsSqlDao repo;

    @BeforeEach
    void before() {
        jdbcTemplate.update("DELETE FROM DATA_TYPES_MS_SQL", new MapSqlParameterSource());
    }

    @Test
    void test() {
        LocalDateTime now = trimNanons(LocalDateTime.now());

        DataTypesMsSqlEntity r = new DataTypesMsSqlEntity();
        r.setBinary10(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        r.setBit(true);
        r.setChaR("a");
        r.setChar10("AAAAAAAAAA");
        r.setDate(now.toLocalDate());
        r.setDatetime(now);
        r.setDatetime2(now);
        r.setDecimalEighteenZero(999999999999999999L);
        r.setDecimalNineZero(999999999);
        r.setDecimalNineteenZero(new BigInteger("999999999999999999"));
        r.setDecimalTenTwo(new BigDecimal("214321.34"));
        r.setDecimalTenZero(9999999999L);
        r.setFloaT(234234.3421F);
        r.setInT(4711);
        r.setMoney(new BigDecimal("234331.34"));
        r.setNchar10("aaaaaaaaaa");
        r.setNtext("fasdgfjkbstogjrowjvrwerjwvweortjbwiort");
        r.setNvarchar10("ferht");
        r.setReal(43423.23F);
        r.setSmallint(17838);
        r.setSmallmoney(new BigDecimal("104.2"));
        r.setText("jgiowjlkbjstiobjtoibtrjbertbertb");
        r.setTime(now.toLocalTime());
        r.setTinyint(127);
        r.setVarbinary10(new byte[]{1, 3, 5});
        r.setVarchar10("abetdfv");
        r.setXml("<foo>bar</foo>");

        // when
        repo.save(r);

        // then
        DataTypesMsSqlEntity r2 = repo.getOne(r.getId());
        assertEquals(r.getId(), r2.getId());
        assertArrayEquals(r.getBinary10(), r2.getBinary10());
        assertEquals(r.getBit(), r2.getBit());
        assertEquals(r.getChaR(), r2.getChaR());
        assertEquals(r.getChar10(), r2.getChar10());
        assertEquals(r.getDate(), r2.getDate());
        assertEquals(r.getDatetime(), r2.getDatetime());
        assertEquals(r.getDatetime2(), r2.getDatetime2());
        assertEquals(r.getDecimalEighteenZero(), r2.getDecimalEighteenZero());
        assertEquals(r.getDecimalNineZero(), r2.getDecimalNineZero());
        assertEquals(r.getDecimalNineteenZero(), r2.getDecimalNineteenZero());
        assertEquals(r.getDecimalTenTwo(), r2.getDecimalTenTwo());
        assertEquals(r.getDecimalTenZero(), r2.getDecimalTenZero());
        assertEquals(r.getFloaT(), r2.getFloaT());
        assertEquals(r.getInT(), r2.getInT());
        assertEquals(r.getMoney().stripTrailingZeros(), r2.getMoney().stripTrailingZeros());
        assertEquals(r.getNchar10(), r2.getNchar10());
        assertEquals(r.getNtext(), r2.getNtext());
        assertEquals(r.getNvarchar10(), r2.getNvarchar10());
        assertEquals(r.getReal(), r2.getReal(), 1e-9);
        assertEquals(r.getSmallint(), r2.getSmallint());
        assertEquals(r.getSmallmoney().stripTrailingZeros(), r2.getSmallmoney().stripTrailingZeros());
        assertEquals(r.getText(), r2.getText());
        assertEquals(r.getTime(), r2.getTime());
        assertEquals(r.getTinyint(), r2.getTinyint());
        assertArrayEquals(r.getVarbinary10(), r2.getVarbinary10());
        assertEquals(r.getVarchar10(), r2.getVarchar10());
        assertEquals(r.getXml(), r2.getXml());
    }

    private LocalDateTime trimNanons(LocalDateTime localDateTime) {
        return localDateTime.truncatedTo(ChronoUnit.SECONDS);
    }
}
