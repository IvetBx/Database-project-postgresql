package rdg.Investigation;

public class Category {

    private Integer id;
    private String type_act;
    private boolean is_crime;
    private boolean is_misdemeanor;
    private boolean is_protecting;
    private Integer serious;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType_act() {
        return type_act;
    }

    public void setType_act(String type_act) {
        this.type_act = type_act;
    }

    public boolean isIs_crime() {
        return is_crime;
    }

    public void setIs_crime(boolean is_crime) {
        this.is_crime = is_crime;
    }

    public boolean isIs_misdemeanor() {
        return is_misdemeanor;
    }

    public void setIs_misdemeanor(boolean is_misdemeanor) {
        this.is_misdemeanor = is_misdemeanor;
    }

    public boolean isIs_protecting() {
        return is_protecting;
    }

    public void setIs_protecting(boolean is_protecting) {
        this.is_protecting = is_protecting;
    }

    public Integer getSerious() {
        return serious;
    }

    public void setSerious(Integer serious) {
        this.serious = serious;
    }
}
