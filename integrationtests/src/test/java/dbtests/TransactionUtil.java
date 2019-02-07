package dbtests;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Component
public class TransactionUtil {
    @Transactional
    public <T> T inTransaction(Supplier<T> supplier) {
        return supplier.get();
    }
}
