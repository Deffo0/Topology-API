import java.util.ArrayList;

public class Topology {
    private String id;
    private ArrayList<Device> components;
    public Topology(String id){
        this.id = id;
        this.components = new ArrayList<>();
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Device> getComponents() {
        return components;
    }

    public void addComponent(Device device){
        this.components.add(device);
    }
}
