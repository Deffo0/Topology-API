import java.util.HashMap;

public interface Device {
    String type = "";
    String ID = "";
    Specifications specifications = null;
    HashMap<String, String> netList = null;

    String getType();
    String getId();
    Specifications getSpecifications();
    HashMap<String, String> getNetList();

}
