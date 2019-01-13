package se.plilja.springdaogen.dbtests.framework;

public class SqlUpdateException extends RuntimeException {
    public SqlUpdateException(String message) {
        super(message);
    }
}