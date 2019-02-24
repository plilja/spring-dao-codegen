package dbtests.framework;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Column<EntityType extends BaseEntity<?>, ValueType> {
    private final String name;
    private final Class<ValueType> type;

    public Column(String name, Class<ValueType> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class<ValueType> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", type=" + type.getSimpleName() +
                '}';
    }

    public static class StringColumn<EntityType extends BaseEntity<?>> extends Column<EntityType, String> {
        public StringColumn(String name) {
            super(name, String.class);
        }
    }

    public static class IntColumn<EntityType extends BaseEntity<?>> extends Column<EntityType, Integer> {
        public IntColumn(String name) {
            super(name, Integer.class);
        }
    }

    public static class LongColumn<EntityType extends BaseEntity<?>> extends Column<EntityType, Long> {
        public LongColumn(String name) {
            super(name, Long.class);
        }
    }

    public static class DoubleColumn<EntityType extends BaseEntity<?>> extends Column<EntityType, Double> {
        public DoubleColumn(String name) {
            super(name, Double.class);
        }
    }

    public static class BooleanColumn<EntityType extends BaseEntity<?>> extends Column<EntityType, Boolean> {
        public BooleanColumn(String name) {
            super(name, Boolean.class);
        }
    }

    public static class DateColumn<EntityType extends BaseEntity<?>> extends Column<EntityType, LocalDate> {
        public DateColumn(String name) {
            super(name, LocalDate.class);
        }
    }

    public static class DateTimeColumn<EntityType extends BaseEntity<?>> extends Column<EntityType, LocalDateTime> {
        public DateTimeColumn(String name) {
            super(name, LocalDateTime.class);
        }
    }

    public static class BigDecimalColumn<EntityType extends BaseEntity<?>> extends Column<EntityType, BigDecimal> {
        public BigDecimalColumn(String name) {
            super(name, BigDecimal.class);
        }
    }

    public static class BigIntegerColumn<EntityType extends BaseEntity<?>> extends Column<EntityType, BigInteger> {
        public BigIntegerColumn(String name) {
            super(name, BigInteger.class);
        }
    }
}