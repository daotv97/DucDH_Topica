public class Hosting {

    private int id;
    private String name;
    private long websites;

    public Hosting(int id, String name, long websites) {
        this.id = id;
        this.name = name;
        this.websites = websites;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getWebsites() {
        return websites;
    }

    public void setWebsites(long websites) {
        this.websites = websites;
    }
}
