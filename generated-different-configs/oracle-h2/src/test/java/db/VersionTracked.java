package db;

/**
 * An entity with a column tracking the version
 * of the entity. This protects it from concurrent
 * updates with stale state.
 */
public interface VersionTracked {

    void setVersion(Integer value);

    Integer getVersion();
}