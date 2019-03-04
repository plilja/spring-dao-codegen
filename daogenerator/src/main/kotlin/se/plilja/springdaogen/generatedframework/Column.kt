package se.plilja.springdaogen.generatedframework


fun columnClass(_package: String): Pair<String, String> {
    return Pair(
        "Column", """
package $_package;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Column<EntityType, ValueType> {
    private final String columnName;
    private final String fieldName;
    private String escapedColumnName;
    private final Class<ValueType> type;

    public Column(String columnName, String escapedColumnName, String fieldName, Class<ValueType> type) {
        this.columnName = columnName;
        this.fieldName = fieldName;
        this.escapedColumnName = escapedColumnName;
        this.type = type;
    }

    public Column(String columnName, String fieldName, Class<ValueType> type) {
        this(columnName, columnName, fieldName, type);
    }

    public String getColumnName() {
        return columnName;
    }

    public String getFieldName() {
        return fieldName;
    }

    String getEscapedColumnName() {
        return escapedColumnName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Column)) return false;
        Column<?, ?> column = (Column<?, ?>) o;
        return Objects.equals(columnName, column.columnName) &&
                Objects.equals(fieldName, column.fieldName) &&
                Objects.equals(escapedColumnName, column.escapedColumnName) &&
                Objects.equals(type, column.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, fieldName, escapedColumnName, type);
    }

    public static class StringColumn<EntityType> extends Column<EntityType, String> {
        public StringColumn(String columnName, String fieldName) {
            super(columnName, fieldName, String.class);
        }

        public StringColumn(String columnName, String escapedColumnName, String fieldName) {
            super(columnName, escapedColumnName, fieldName, String.class);
        }
    }

    public static class IntColumn<EntityType> extends Column<EntityType, Integer> {
        public IntColumn(String columnName, String fieldName) {
            super(columnName, fieldName, Integer.class);
        }

        public IntColumn(String columnName, String escapedColumnName, String fieldName) {
            super(columnName, escapedColumnName, fieldName, Integer.class);
        }
    }

    public static class LongColumn<EntityType> extends Column<EntityType, Long> {
        public LongColumn(String columnName, String fieldName) {
            super(columnName, fieldName, Long.class);
        }

        public LongColumn(String columnName, String escapedColumnName, String fieldName) {
            super(columnName, escapedColumnName, fieldName, Long.class);
        }
    }

    public static class DoubleColumn<EntityType> extends Column<EntityType, Double> {
        public DoubleColumn(String columnName, String fieldName) {
            super(columnName, fieldName, Double.class);
        }
        public DoubleColumn(String columnName, String escapedColumnName, String fieldName) {
            super(columnName, escapedColumnName, fieldName, Double.class);
        }
    }

    public static class BooleanColumn<EntityType> extends Column<EntityType, Boolean> {
        public BooleanColumn(String columnName, String fieldName) {
            super(columnName, fieldName, Boolean.class);
        }

        public BooleanColumn(String columnName, String escapedColumnName, String fieldName) {
            super(columnName, escapedColumnName, fieldName, Boolean.class);
        }
    }

    public static class DateColumn<EntityType> extends Column<EntityType, LocalDate> {
        public DateColumn(String columnName, String fieldName) {
            super(columnName, fieldName, LocalDate.class);
        }

        public DateColumn(String columnName, String escapedColumnName, String fieldName) {
            super(columnName, escapedColumnName, fieldName, LocalDate.class);
        }
    }

    public static class DateTimeColumn<EntityType> extends Column<EntityType, LocalDateTime> {
        public DateTimeColumn(String columnName, String fieldName) {
            super(columnName, fieldName, LocalDateTime.class);
        }

        public DateTimeColumn(String columnName, String escapedColumnName, String fieldName) {
            super(columnName, escapedColumnName, fieldName, LocalDateTime.class);
        }
    }

    public static class BigDecimalColumn<EntityType> extends Column<EntityType, BigDecimal> {
        public BigDecimalColumn(String columnName, String fieldName) {
            super(columnName, fieldName, BigDecimal.class);
        }

        public BigDecimalColumn(String columnName, String escapedColumnName, String fieldName) {
            super(columnName, escapedColumnName, fieldName, BigDecimal.class);
        }
    }

    public static class BigIntegerColumn<EntityType> extends Column<EntityType, BigInteger> {
        public BigIntegerColumn(String columnName, String fieldName) {
            super(columnName, fieldName, BigInteger.class);
        }

        public BigIntegerColumn(String columnName, String escapedColumnName, String fieldName) {
            super(columnName, escapedColumnName, fieldName, BigInteger.class);
        }
    }
}
    """.trimIndent())
}
