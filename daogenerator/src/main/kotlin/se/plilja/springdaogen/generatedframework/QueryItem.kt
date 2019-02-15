package se.plilja.springdaogen.generatedframework


fun queryItem(_package: String): Pair<String, String> {
    return Pair(
        "QueryItem", """
package $_package;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.function.Supplier;

public abstract class QueryItem<Entity extends BaseEntity<?>> {

    protected abstract String getClause(MapSqlParameterSource params, Supplier<String> paramNameGenerator);

    private static class Or<Entity extends BaseEntity<?>> extends QueryItem<Entity> {
        private final QueryItem<Entity> clause1;
        private final QueryItem<Entity> clause2;

        private Or(QueryItem<Entity> clause1, QueryItem<Entity> clause2) {
            this.clause1 = clause1;
            this.clause2 = clause2;
        }

        @Override
        protected String getClause(MapSqlParameterSource params, Supplier<String> paramNameGenerator) {
            String q1 = clause1.getClause(params, paramNameGenerator);
            String q2 = clause2.getClause(params, paramNameGenerator);
            return String.format("((%s) OR (%s))", q1, q2);
        }
    }

    private static class ComparisonQueryItem<Entity extends BaseEntity<?>, ValueType> extends QueryItem<Entity> {
        private Column<Entity, ValueType> column;
        private final ValueType value;
        private final Operator operator;
        private final boolean includeNulls;

        private ComparisonQueryItem(Column<Entity, ValueType> column, ValueType value, Operator operator, boolean includeNulls) {
            this.column = column;
            this.value = value;
            this.operator = operator;
            this.includeNulls = includeNulls;
        }

        @Override
        protected String getClause(MapSqlParameterSource params, Supplier<String> paramNameGenerator) {
            String param = paramNameGenerator.get();
            String clause;
            if (value == null) {
                if (operator == QueryItem.Operator.EQ) {
                    clause = String.format("%s IS NULL", column.getName());
                } else if (operator == QueryItem.Operator.NEQ) {
                    clause = String.format("%s IS NOT NULL", column.getName());
                } else {
                    throw new IllegalArgumentException(String.format("Unsupported operator %s for comparison with NULL", operator.name()));
                }
            } else {
                if (operator == QueryItem.Operator.NEQ && includeNulls) {
                    clause = String.format("(%s %s :%s OR %s IS NULL)", column.getName(), operator.getSymbol(), param, column.getName());
                } else {
                    clause = String.format("%s %s :%s", column.getName(), operator.getSymbol(), param);
                }
                if (value instanceof BaseDatabaseEnum<?>) {
                    params.addValue(param, ((BaseDatabaseEnum<?>) value).getId());
                } else {
                    params.addValue(param, value);
                }
            }
            assert clause != null;
            return clause;
        }
    }

    public static <Entity extends BaseEntity<?>, ValueType> QueryItem<Entity> eq(Column<Entity, ValueType> column, ValueType value) {
        return new ComparisonQueryItem<>(column, value, Operator.EQ, false);
    }

    public static <Entity extends BaseEntity<?>, ValueType> QueryItem<Entity> neq(Column<Entity, ValueType> column, ValueType value, boolean includeNulls) {
        return new ComparisonQueryItem<>(column, value, Operator.NEQ, includeNulls);
    }

    public static <Entity extends BaseEntity<?>, ValueType> QueryItem<Entity> lt(Column<Entity, ValueType> column, ValueType value) {
        return new ComparisonQueryItem<>(column, value, Operator.LT, false);
    }

    public static <Entity extends BaseEntity<?>, ValueType> QueryItem<Entity> lte(Column<Entity, ValueType> column, ValueType value) {
        return new ComparisonQueryItem<>(column, value, Operator.LTE, false);
    }

    public static <Entity extends BaseEntity<?>, ValueType> QueryItem<Entity> gt(Column<Entity, ValueType> column, ValueType value) {
        return new ComparisonQueryItem<>(column, value, Operator.GT, false);
    }

    public static <Entity extends BaseEntity<?>, ValueType> QueryItem<Entity> gte(Column<Entity, ValueType> column, ValueType value) {
        return new ComparisonQueryItem<>(column, value, Operator.GTE, false);
    }

    public static <Entity extends BaseEntity<?>, ValueType> QueryItem<Entity> or(QueryItem<Entity> clause1, QueryItem<Entity> clause2) {
        return new Or<>(clause1, clause2);
    }

    private enum Operator {
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
    """.trimIndent())
}
