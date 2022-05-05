import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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
        ObjectMapper objectMapper = new ObjectMapper();
        Topology topology = this.topologies.get(topologyID);
        try {
            objectMapper.writeValue(new File("src\\main\\resources\\" + topologyID + ".json"), topology);
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

}
