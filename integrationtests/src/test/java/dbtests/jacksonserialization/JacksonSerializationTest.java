package dbtests.jacksonserialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dbtests.h2.model.BazH2;
import dbtests.h2.model.ColorEnumH2;
import dbtests.mssql.model.BazMsSqlEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JacksonSerializationTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    void versionColumnsShouldNotBeSerialized() throws Exception {
        // The version column is only a guard against concurrent
        // modifications, if an object with a version column
        // is serialized for example in a rest endpoint, that
        // detail should not leak.

        BazH2 o = new BazH2();
        o.setId(1);
        o.setBazName("Name");
        o.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        o.setVersion(5);

        // when
        String json = objectMapper.writeValueAsString(o);

        // then
        assertFalse(json.contains("\"version\""));
    }

    @Test
    void versionColumnsShouldNotBeDeSerialized() throws Exception {
        String json = "{\"bazId\":1,\"version\":4,\"bazName\":\"Name\",\"changedAt\":null,\"color\":null,\"createdAt\":\"2019-02-10T11:26:07\"}";

        // when
        BazH2 o = objectMapper.readValue(json, BazH2.class);

        // then
        assertEquals(Integer.valueOf(1), o.getId());
        assertEquals("Name", o.getBazName());
        assertNull(o.getColor());
        assertEquals(LocalDateTime.of(2019, 2, 10, 11, 26, 7), o.getCreatedAt());
        assertNull(o.getVersion());
    }

    @Test
    void pkColumnIsNotNamedIdSerialization() throws Exception {
        // If the pk column is not named id there will be a method named
        // id on the class as well as a getter for the original id field.
        BazH2 o = new BazH2();
        o.setId(1);
        o.setBazName("Name");
        o.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        o.setVersion(5);

        // when
        String json = objectMapper.writeValueAsString(o);

        // then
        assertFalse(json.contains("\"id\""));
    }

    @Test
    void pkColumnIsNotNamedIdDeSerialization() throws Exception {
        String json = "{\"id\":1,\"bazName\":\"Name\",\"changedAt\":null,\"color\":null,\"createdAt\":\"2019-02-10T11:26:07\"}";

        // when
        BazH2 o = objectMapper.readValue(json, BazH2.class);

        // then
        assertNull(o.getId());
        assertEquals("Name", o.getBazName());
        assertNull(o.getColor());
        assertEquals(LocalDateTime.of(2019, 2, 10, 11, 26, 7), o.getCreatedAt());
    }

    @Test
    void pkColumnIsNamedId() throws Exception {
        // If the pk column is not named id there will be a method named
        // id on the class as well as a getter for the original id field.
        BazMsSqlEntity o = new BazMsSqlEntity();
        o.setId(1);
        o.setName("Name");
        o.setInsertedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        o.setVersion(5);

        // when
        String json = objectMapper.writeValueAsString(o);

        // then
        assertTrue(json.contains("\"id\""));
    }

    @Test
    void pkColumnIsNamedIdDeSerialization() throws Exception {
        String json = "{\"id\":1,\"name\":\"Name\",\"modifiedAt\":null,\"color\":null,\"insertedAt\":\"2019-02-10T11:26:07\"}";

        // when
        BazMsSqlEntity o = objectMapper.readValue(json, BazMsSqlEntity.class);

        // then
        assertEquals(Integer.valueOf(1), o.getId());
        assertEquals("Name", o.getName());
        assertNull(o.getColor());
        assertEquals(LocalDateTime.of(2019, 2, 10, 11, 26, 7), o.getCreatedAt());
    }

    @Test
    void changedAtAndCreatedWhenColumnsAreNotNamedChangeAtAndCreatedAt() throws Exception {
        BazMsSqlEntity o = new BazMsSqlEntity();
        o.setId(1);
        o.setName("Name");
        o.setInsertedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        o.setModifiedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        o.setVersion(5);

        // when
        String json = objectMapper.writeValueAsString(o);

        // then
        assertTrue(json.contains("\"insertedAt\""));
        assertTrue(json.contains("\"modifiedAt\""));
        assertFalse(json.contains("\"createdAt\""));
        assertFalse(json.contains("\"changedAt\""));
    }

    @Test
    void changedAtAndCreatedWhenColumnsAreNamedChangeAtAndCreatedAt() throws Exception {
        BazH2 o = new BazH2();
        o.setId(1);
        o.setBazName("Name");
        o.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        o.setChangedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        o.setVersion(5);

        // when
        String json = objectMapper.writeValueAsString(o);

        // then
        assertTrue(json.contains("\"createdAt\""));
        assertTrue(json.contains("\"changedAt\""));
    }

    @Test
    void serializationEnum() throws Exception {
        BazH2 o = new BazH2();
        o.setId(1);
        o.setBazName("Name");
        o.setColor(ColorEnumH2.BLUE);

        // when
        String json = objectMapper.writeValueAsString(o);
        BazH2 deserialized = objectMapper.readValue(json, BazH2.class);

        // then
        assertTrue(json.contains("\"BLUE\""));
        assertEquals(ColorEnumH2.BLUE, deserialized.getColor());
    }
}
