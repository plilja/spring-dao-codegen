package db.h2;

public class OneColumnGeneratedIdPostgres implements BaseEntity<Integer> {

    private Integer id;

    public OneColumnGeneratedIdPostgres() {
    }

    public OneColumnGeneratedIdPostgres(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

}
