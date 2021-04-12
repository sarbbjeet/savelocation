package uk.ac.tees.a0321466.model;

public class locationModel {
    private String iconUrl;
    private String name;
    private String addr;
    private String rating;
    private int id;

    public locationModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public locationModel(int id,String name, String addr, String rating, String iconUrl) {
        this.id=id;
        this.iconUrl = iconUrl;
        this.name = name;
        this.addr = addr;
        this.rating = rating;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
