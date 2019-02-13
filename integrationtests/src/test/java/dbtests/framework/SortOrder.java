package dbtests.framework;

public class SortOrder<E extends BaseEntity<?>> {
    private final Column<E, ?> column;
    private final Order order;

    private SortOrder(Column<E, ?> column, Order order) {
        this.column = column;
        this.order = order;
    }

    public Column<E, ?> getColumn() {
        return column;
    }

    public String getOrder() {
        return order.name();
    }

    public static <E extends BaseEntity<?>> SortOrder<E> asc(Column<E, ?> column) {
        return new SortOrder<>(column, Order.ASC);
    }

    public static <E extends BaseEntity<?>> SortOrder<E> desc(Column<E, ?> column) {
        return new SortOrder<>(column, Order.DESC);
    }

    private enum Order {
        ASC,
        DESC
    }
}