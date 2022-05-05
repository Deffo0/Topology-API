import java.util.HashMap;

public class Resistor implements Device {
    private String type;
    private String id;
    private Specifications resistance;
    private HashMap<String, String> netList ;

    public Resistor(String type, String id, Specifications resistance, String t1, String t2){
        this.type = type;
        this.id = id;
        this.resistance = resistance;
        this.netList = new HashMap<>();
        this.netList.put("t1", t1);
        this.netList.put("t2", t2);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Specifications getSpecifications() {
        return resistance;
    }

    @Override
    public HashMap<String, String> getNetList() {
        return netList;
    }

}
