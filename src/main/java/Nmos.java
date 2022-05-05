import java.util.HashMap;

public class Nmos implements Device{
    private String type;
    private String id;
    private Specifications ml;
    private HashMap<String, String> netList ;

    public Nmos(String type, String id, Specifications ml, String drain, String gate, String source){
        this.type = type;
        this.id = id;
        this.ml = ml;
        this.netList = new HashMap<>();
        this.netList.put("drain", drain);
        this.netList.put("gate", gate);
        this.netList.put("source", source);
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
        return ml;
    }

    @Override
    public HashMap<String, String> getNetList() {
        return netList;
    }
}
