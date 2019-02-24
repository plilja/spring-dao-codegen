package dbtests.framework;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Column<EntityType extends BaseEntity<?>, ValueType> {
    private final String columnName;
    private final String fieldName;
    private final Class<ValueType> type;

    public Column(String columnName, String fieldName, Class<ValueType> type) {
        this.columnName = columnName;
        this.fieldName = fieldName;
        this.type = type;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Class<ValueType> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Column{" +
                "name='" + columnName + '\'' +
                ", type=" + type.getSimpleName() +
                '}';
    }

    public static class StringColumn<EntityType extends BaseEntity<?>> extends Column<EntityType, String> {
        public StringColumn(String columnName, String fieldName) {
            super(columnName, fieldName, String.class);
        }
    }

    public static class IntColumn<EntityType extends BaseEntity<?>> extends Column<EntityType, Integer> {
        public IntColumn(String columnName, String fieldName) {
            super(columnName, fieldName, Integer.class);
        }
    }

    public static class LongColumn<EntityType extends BaseEntity<?>> extends Column<EntityType, Long> {
        public LongColumn(String columnName, String fieldName) {
            super(columnName, fieldName, Long.class);
        }
    }

    public static class DoubleColumn<EntityType extends BaseEntity<?>> extends Column<EntityType, Double> {
        public DoubleColumn(String columnName, String fieldName) {
            super(columnName, fieldName, Double.class);
        }
    }

    public static class BooleanColumn<EntityType extends BaseEntity<?>> extends Column<EntityType, Boolean> {
        public BooleanColumn(String columnName, String fieldName) {
            super(columnName, fieldName, Boolean.class);
        }
    }

    public static class DateColumn<EntityType extends BaseEntity<?>> extends Column<EntityType, LocalDate> {
        public DateColumn(String columnName, String fieldName) {
            super(columnName, fieldName, LocalDate.class);
        }
    }

    public static class DateTimeColumn<EntityType extends BaseEntity<?>> extends Column<EntityType, LocalDateTime> {
        public DateTimeColumn(String columnName, String fieldName) {
            super(columnName, fieldName, LocalDateTime.class);
        }
    }

    public static class BigDecimalColumn<EntityType extends BaseEntity<?>> extends Column<EntityType, BigDecimal> {
        public BigDecimalColumn(String columnName, String fieldName) {
            super(columnName, fieldName, BigDecimal.class);
        }
    }

    public static class BigIntegerColumn<EntityType extends BaseEntity<?>> extends Column<EntityType, BigInteger> {
        public BigIntegerColumn(String columnName, String fieldName) {
            super(columnName, fieldName, BigInteger.class);
        }
    }
}