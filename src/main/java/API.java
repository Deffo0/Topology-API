import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class API {
    private HashMap<String,Topology> topologies;

    public API(){
        this.topologies = new HashMap<>();
    }

    /***
     * @function parse json file and convert it to topology object
     * @param fileName
     * @return 1 or 0 according to the success of the function without errors
     */
    public int readJSON(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            File json = new File(fileName);
            JsonNode jsonRoot = objectMapper.readTree(json);

            //get the id field of the topology
            String topologyID = jsonRoot.get("id").asText();

            // create an instance for a new topology by this ID
            Topology topology = new Topology(topologyID);

            // get a pointer on the list of components in this topology
            JsonNode components = jsonRoot.get("components");

            // for each component get the type, ID, specifications and netList of it
            for (JsonNode jsonNode : components) {
                String type = jsonNode.get("type").asText();
                String id = jsonNode.get("id").asText();
                double Default, min, max;
                JsonNode netList;

                // resistor and nmos have their own name for specifications
                // based on their types, access their entries by a different way
                if (type.equals("resistor")) {
                    JsonNode resistance = jsonNode.path("resistance");
                    Default = resistance.path("default").asDouble();
                    min = resistance.path("min").asDouble();
                    max = resistance.path("max").asDouble();
                    netList = jsonNode.path("netlist");
                    String t1 = netList.path("t1").asText();
                    String t2 = netList.path("t2").asText();

                    // append those collected information about each component to the generated instance of topology
                    topology.addComponent(new Resistor(type, id, new Specifications(Default, min, max), t1, t2));
                } else if (type.equals("nmos")) {
                    JsonNode m1 = jsonNode.path("m(l)");
                    Default = m1.path("default").asDouble();
                    min = m1.path("min").asDouble();
                    max = m1.path("max").asDouble();
                    netList = jsonNode.path("netlist");
                    String drain = netList.path("drain").asText();
                    String gate = netList.path("gate").asText();
                    String source = netList.path("source").asText();

                    // append those collected information about each component to the generated instance of topology
                    topology.addComponent(new Nmos(type, id, new Specifications(Default, min, max), drain, gate, source));
                }
            }
            this.topologies.put(topology.getId(),topology);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
        return 1;
    }

    /***
     * @function select one topology from the resident set of topologies and convert it to json file
     * @param topologyID
     * @return 1 or 0 according to the success of the function without errors
     */
    public int writeJSON(String topologyID){

        // get the desired topology
        Topology topology = this.topologies.get(topologyID);

        // JSON Object for the whole topology
        JSONObject jsonObject = new JSONObject();
        orderJSONObject(jsonObject);

        // get the id field of the topology
        jsonObject.put("id", topology.getId());

        // each topology has its own devices JSON Array
        JSONArray components = new JSONArray();

        // for each device, make a json object contains all its data
        for (Device device: topology.getComponents()) {
            JSONObject jsonDevice = new JSONObject();
            orderJSONObject(jsonDevice);

            jsonDevice.put("type", device.getType());
            jsonDevice.put("id", device.getId());

            // each device has its own specifications
            JSONObject specifications = new JSONObject();
            orderJSONObject(specifications);

            specifications.put("default", device.getSpecifications().getDefault());
            specifications.put("min", device.getSpecifications().getMin());
            specifications.put("max", device.getSpecifications().getMax());

            // resistor and nmos have their own name for specifications
            // based on the device type, the netlist differs from one to another
            if(device.getType().equals("resistor")){
                jsonDevice.put("resistance",specifications);

                JSONObject netList = new JSONObject();
                orderJSONObject(netList);
                netList.put("t1", device.getNetList().get("t1"));
                netList.put("t2", device.getNetList().get("t2"));
                jsonDevice.put("netlist", netList);
            }else if(device.getType().equals("nmos")){
                jsonDevice.put("m(l)", specifications);

                JSONObject netList = new JSONObject();
                orderJSONObject(netList);
                netList.put("drain", device.getNetList().get("drain"));
                netList.put("gate", device.getNetList().get("gate"));
                netList.put("source", device.getNetList().get("source"));
                jsonDevice.put("netlist", netList);
            }
            components.put(jsonDevice);
        }

        jsonObject.put("components", components);
        try {

            FileWriter writer = new FileWriter("src\\main\\resources\\" + topologyID + ".json");
            jsonObject.write(writer);
            writer.close();

        }catch (Exception e){
            System.out.println(e.getMessage());
            return 0;
        }
        return 1;
    }

    /***
     * @return list of the resident set of topologies
     */
    public List<Topology> queryTopologies(){
        return  this.topologies.values().stream().toList();
    }

    /***
     * @function delete one topology from the resident set
     * @param topologyID
     * @return 1 or 0 according to the success of the function without errors
     */
    public int deleteTopology(String topologyID){
        if(this.topologies.remove(topologyID) == null){
            return 0;
        }else {
            return 1;
        }
    }

    /***
     * @param topologyID
     * @return ArrayList for the devices in a specific topology
     */
    public ArrayList<Device> queryDevices(String topologyID){
        return this.topologies.get(topologyID).getComponents();
    }

    /***
     *
     * @param topologyID
     * @param netlistNodeID
     * @return ArrayList for the devices which are in a specific topology and connected to a specific netlistNode
     */
    public ArrayList<Device> queryDevicesWithNetlistNode(String topologyID, String netlistNodeID){
        ArrayList<Device> result = new ArrayList<>();
        for (Device device: this.topologies.get(topologyID).getComponents()) {
            if(device.getNetList().containsValue(netlistNodeID)){
                result.add(device);
            }
        }
        return result;
    }

    /***
     * @function change the JSONObject data structure from HashMap to LinkedHashMap to keep the entries in ordered way
     * @param jsonObject
     */
    public void orderJSONObject(JSONObject jsonObject){
        try {
            Field map = jsonObject.getClass().getDeclaredField("map");
            map.setAccessible(true);
            map.set(jsonObject, new LinkedHashMap<>());
            map.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            System.out.println(e.getMessage());
        }
    }

}
