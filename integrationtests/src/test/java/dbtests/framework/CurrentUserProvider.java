package dbtests.framework;

/**
 * Determines what should be written to
 * to a createdBy/changedBy-column.
 */
public interface CurrentUserProvider {
    String getCurrentUser();
}