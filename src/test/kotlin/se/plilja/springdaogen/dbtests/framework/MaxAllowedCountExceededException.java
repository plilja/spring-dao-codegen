package se.plilja.springdaogen.dbtests.framework;

public class MaxAllowedCountExceededException extends RuntimeException {
    public MaxAllowedCountExceededException(String message) {
        super(message);
    }
}