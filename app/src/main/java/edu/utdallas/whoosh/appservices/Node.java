package edu.utdallas.whoosh.appservices;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import edu.utdallas.whoosh.api.INode;
import edu.utdallas.whoosh.api.NodeType;

//So we can use parse to init objects of this class
import com.parse.ParseObject;

/**
 * Created by Marie on 9/30/2015.
 */
public class Node implements INode {
    private String id;
    private LatLng coordinates;
    private String subgroup;
    private String name;
    private NodeType type;
    private ArrayList<Node> adjacentNodes;
    private Integer floor;
    private NodeGroup group;


    /**A Constructor that takes all Node attributes as an argument
     * @param id
     * @param coordinates
     * @param group
     * @param subgroup
     * @param name
     * @param type
     * @param adjacentNodes
     * @param floor
     */
    Node( String id, LatLng coordinates, NodeGroup group, String subgroup,
          String name, NodeType type, Integer floor, List<Object> adjacentNodes){
        this.id = id;
        this.coordinates = coordinates;
        this.group = group;
        this.subgroup = subgroup;
        this.name = name;
        this.type = type;
        this.adjacentNodes = new ArrayList<Node>(); //initalized w/ setAdjacent Nodes after initial instantiation
        this.floor = floor;
    }

    Node(){
        super();
    }

    /**A constructor that takes a ParseObject object as an argument
     * converts the object attributes to Node attributes,
     * then calls the constructor that takes all Node attributes.
     * @param object
     */
    Node(ParseObject object){
//^NodeGroups HAS to be initialized first by DBManager
        this(object.getString("myID"),
                new LatLng(object.getParseGeoPoint("coordinates").getLatitude(), object.getParseGeoPoint("coordinates").getLongitude()),
                NodeManager.getInstance().getNodeGroup(object.getString("nodegroup")),
                object.getString("subgroup"),
                object.getString("name"),
                NodeType.valueOf(object.getString("type")),
                object.getInt("floor"),
                object.getList("adjacentNodes"));
    }

    /**Setter(s?)*/
    public void setAdjacentNodes(ParseObject object){
        //list of adjacent node IDS
        List <Object> neighbors = object.getList("adjacentNodes");

        Log.d("Node","neighbors: "+neighbors.size());

        for(int i = 0; i < neighbors.size(); i++ ){
            //
            Log.d("Node","loading neighbor: "+NodeManager.getInstance().getNode("" + (Integer) neighbors.get(i)).getId());
            adjacentNodes.add(NodeManager.getInstance().getNode("" + (Integer) neighbors.get(i)));
            Log.d("Node", "added " + adjacentNodes.get(i).getId() + " as neighbor");
        }
    }
    /**Getters*/
    public LatLng getCoordinates(){
        return coordinates;
    }

    public String getId(){
        return id;
    }

    public NodeGroup getGroup(){
        return group;
    }

    public NodeType getType(){
        return type;
    }

    public List<Node> getAdjacentNodes(){
        return adjacentNodes;
    }

    public String getSubgroup(){
        return subgroup;
    }

    public String getName(){
        return name;
    }

    public Integer getFloor(){
        return floor;
    }

    @Override
    public int hashCode(){
        return Integer.parseInt(id);
    }

    public static float distanceInFeet(Node n1, Node n2){

        return distanceInFeet(n1.getCoordinates().latitude, n1.getCoordinates().longitude,
                n2.getCoordinates().latitude, n2.getCoordinates().longitude);
    }

    public static float distanceInFeet(double lat1, double long1, double lat2, double long2){

        double radius = 6372.8f;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(long2 - long1);
        double latr1 = Math.toRadians(lat1);
        double latr2 = Math.toRadians(lat2);

        //Haversine formula
        float result = (float)(radius * (2 * Math.asin(Math.sqrt(Math.pow(Math.sin(dLat / 2),2) +
                Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2)))));

        //kilometers to feet
        return (result * 1000) * 3.28084f;
    }
}
