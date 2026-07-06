package model;

public class Company extends BaseModel {
    private String name;

    public Company() {
        super();
    }

    public Company(String name) {
        super();
        this.name = name;
    }

    public Company(String id, String name) {
        super(id);
        this.name = name;
    }

    @Override
    public String toString() {
        return "Company{id=" + getId() + ", name='" + name + "'}";
    }

    // Getter and Setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
