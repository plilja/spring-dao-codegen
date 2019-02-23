package db;

/**
 * An entity with a column tracking when
 * it was last changed.
 */
public interface ChangedAtTracked<T> {
    /**
     * Mark the entity as changed right now.
     */
    void setChangedNow();

    T getChangedAt();
}