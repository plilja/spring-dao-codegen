package dbtests.framework;

public interface ChangedAtTracked<T> {
    void setChangedNow();

    T getChangedAt();
}