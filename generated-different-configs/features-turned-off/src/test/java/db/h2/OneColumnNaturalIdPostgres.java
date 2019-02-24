package db.h2;

public class OneColumnNaturalIdPostgres implements BaseEntity<String> {

    private String id;

    public OneColumnNaturalIdPostgres() {
    }

    public OneColumnNaturalIdPostgres(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
