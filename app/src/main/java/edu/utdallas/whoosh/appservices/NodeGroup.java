package edu.utdallas.whoosh.appservices;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import edu.utdallas.whoosh.api.GroupType;
import edu.utdallas.whoosh.api.INodeGroup;


/**
 * Created by Marie on 9/30/2015.
 */
public class NodeGroup implements INodeGroup {
    private String id;
    private String name;
    private GroupType type;
    private List<Integer> floors;
    private Integer defaultFloor;
    private LatLng centerCoordinates;

    NodeGroup(){}

    NodeGroup( String id, String name, GroupType type, List<Integer> floors, Integer defaultFloor, LatLng centerCoordinates){
        this.id = id;
        this.name = name;
        this.type = type;
        this.floors = floors;
        this.defaultFloor = defaultFloor;
        this.centerCoordinates = centerCoordinates;
    }

    NodeGroup(ParseObject object){
        this.id = object.getString("myID");
        this.name = object.getString("name");

        Log.d(getClass().getName(), "name: "+name+ " grouptype: "+ object.getString("grouptype"));
        this.type = GroupType.valueOf(object.getString("grouptype"));
        this.floors = new ArrayList<Integer>(); floors = object.getList("floors");
        this.defaultFloor = object.getInt("defaultfloor");
        this.centerCoordinates = new LatLng(object.getParseGeoPoint("centerCoordinates").getLatitude(), object.getParseGeoPoint("centerCoordinates").getLongitude());
    }

    /**
     * natural key, such as "ECSS"
     */
    public String getId(){
        return id;
    };

    /**
     * colloquial name, such as "Engineering and Computer Science South"
     */
    public String getName(){
        return name;
    };

    /**
     * the type of group, such as {@link GroupType#Building}
     */
    public GroupType getType(){
        return type;
    }

    /**
     * {@link List} of routable vertical levels within this group, typically floor id's within a building;
     * {@link GroupType#OutdoorArea} groups should return a single element of {@code 1}
     */
    public List<Integer> getFloors(){
        return floors;
    }

    /**
     * the initially selected level of this group (typically the "ground" level)
     */
    public Integer getDefaultFloor(){
        return defaultFloor;
    }

    /**
     * the center latitude/longitude of this group; could be used to provide a marker/control to switch
     * from global map to detailed maps (for this node group)
     */
    public LatLng getCenterCoordinates(){
        return centerCoordinates;
    }
}
