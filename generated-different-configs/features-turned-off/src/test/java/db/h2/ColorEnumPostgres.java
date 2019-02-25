package db.h2;

public class ColorEnumPostgres implements BaseEntity<String> {

    private String id;
    private String hex;

    public ColorEnumPostgres() {
    }

    public ColorEnumPostgres(String id, String hex) {
        this.id = id;
        this.hex = hex;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ColorEnumPostgres{id=" + id + "}";
    }

}
