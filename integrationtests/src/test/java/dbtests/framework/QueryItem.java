package dbtests.framework;

public class QueryItem<Entity extends BaseEntity<?>, ValueType> {
    private Column<Entity, ValueType> column;
    private final ValueType value;
    private final Operator operator;
    private final boolean includeNulls;

    private QueryItem(Column<Entity, ValueType> column, ValueType value, Operator operator, boolean includeNulls) {
        this.column = column;
        this.value = value;
        this.operator = operator;
        this.includeNulls = includeNulls;
    }

    public static <Entity extends BaseEntity<?>, ValueType> QueryItem<Entity, ValueType> eq(Column<Entity, ValueType> column, ValueType value) {
        return new QueryItem<>(column, value, Operator.EQ, false);
    }

    public static <Entity extends BaseEntity<?>, ValueType> QueryItem<Entity, ValueType> neq(Column<Entity, ValueType> column, ValueType value, boolean includeNulls) {
        return new QueryItem<>(column, value, Operator.NEQ, includeNulls);
    }

    public static <Entity extends BaseEntity<?>, ValueType> QueryItem<Entity, ValueType> lt(Column<Entity, ValueType> column, ValueType value) {
        return new QueryItem<>(column, value, Operator.LT, false);
    }

    public static <Entity extends BaseEntity<?>, ValueType> QueryItem<Entity, ValueType> lte(Column<Entity, ValueType> column, ValueType value) {
        return new QueryItem<>(column, value, Operator.LTE, false);
    }

    public static <Entity extends BaseEntity<?>, ValueType> QueryItem<Entity, ValueType> gt(Column<Entity, ValueType> column, ValueType value) {
        return new QueryItem<>(column, value, Operator.GT, false);
    }

    public static <Entity extends BaseEntity<?>, ValueType> QueryItem<Entity, ValueType> gte(Column<Entity, ValueType> column, ValueType value) {
        return new QueryItem<>(column, value, Operator.GTE, false);
    }

    public ValueType getValue() {
        return value;
    }

    public Column<Entity, ValueType> getColumn() {
        return column;
    }

    public Operator getOperator() {
        return operator;
    }

    public boolean isIncludeNulls() {
        return includeNulls;
    }

    public enum Operator {
        EQ("="),
        NEQ("<>"),
        LT("<"),
        LTE("<="),
        GT(">"),
        GTE(">=");

        private String symbol;

        Operator(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }
}