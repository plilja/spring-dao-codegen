package dbtests.framework;

public interface CreatedAtTracked<T> {
    void setCreatedNow();

    T getCreatedAt();
}