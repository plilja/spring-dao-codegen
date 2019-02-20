package db.h2;

public class OneColumnNaturalIdH2 implements BaseEntity<String> {

    private String id;

    public OneColumnNaturalIdH2() {
    }

    public OneColumnNaturalIdH2(String id) {
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
