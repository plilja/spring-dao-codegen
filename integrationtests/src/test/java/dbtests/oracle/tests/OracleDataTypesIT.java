package dbtests.oracle.tests;

import dbtests.oracle.model.DataTypesOracle;
import dbtests.oracle.model.DataTypesOracleRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {OracleITConfig.class})
@ExtendWith(SpringExtension.class)
public class OracleDataTypesIT {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private DataTypesOracleRepository repo;

    @BeforeEach
    void before() {
        jdbcTemplate.update("DELETE FROM DOCKER.DATA_TYPES_ORACLE", new MapSqlParameterSource());
    }

    @Test
    void test() {
        LocalDateTime now = LocalDateTime.now();

        DataTypesOracle r = new DataTypesOracle();
        r.setId("ID");
        r.setBinaryDouble(0.7);
        r.setBinaryFloat(0.4F);
        r.setBlob(new byte[]{1, 2, 3});
        r.setChar1("A");
        r.setChar10("ABCDEFGHI");
        r.setClob(new byte[]{4, 5, 6});
        r.setDate(now.toLocalDate());
        r.setNumberEighteenZero(123412341224L);
        r.setNumberNineZero(4711);
        r.setNumberNineteenZero(new BigInteger("999999999999999999"));
        r.setNumberTenTwo(new BigDecimal("242112.23"));
        r.setNumberTenZero(213412234L);
        r.setTimestamp(now);
        r.setVarchar("foo bar");
        r.setVarchar2("foo bar");

        // when
        repo.save(r);

        // then
        DataTypesOracle r2 = repo.getOne("ID");
        assertEquals(r.getId(), r2.getId());
        assertEquals(r.getBinaryDouble(), r2.getBinaryDouble(), 1e-10);
        assertEquals(r.getBinaryFloat(), r2.getBinaryFloat(), 1e-10);
        assertEquals(r.getChar1(), r2.getChar1());
        assertEquals(r.getChar10(), r2.getChar10().strip()); // Gets padded by DB
        assertEquals(r.getDate(), r2.getDate());
        assertEquals(r.getNumberEighteenZero(), r2.getNumberEighteenZero());
        assertEquals(r.getNumberNineZero(), r2.getNumberNineZero());
        assertEquals(r.getNumberNineteenZero(), r2.getNumberNineteenZero());
        assertEquals(r.getNumberTenTwo(), r2.getNumberTenTwo());
        assertEquals(r.getNumberTenZero(), r2.getNumberTenZero());
        assertEquals(r.getVarchar(), r2.getVarchar2());
    }
}
