package edu.utdallas.whoosh.api;

import java.util.ArrayList;
import java.util.List;

/**
 * An enumeration listing the various types of {@link //Node}s.
 *
 * Created by sasha on 9/21/15.
 */
public enum NodeType {

    Path,
    Curb,
    Ramp(false, true),
    Skybridge,
    Elevator(false, true),
    Door(false, true),
    Room(true, false),
    Restroom,
    Stair,
    ;

    boolean endpoint;
    boolean handicapRelevant;

    NodeType() {
        this(false, false);
    }

    NodeType(boolean endpoint, boolean handicapRelevant) {
        this.endpoint = endpoint;
        this.handicapRelevant = handicapRelevant;
    }

    /**
     * a flag indicating whether this type is a valid origin/destination (see {@link IRoute#getOrigin()}
     * and {@link IRoute#getDestination()})
     */
    public boolean isEndpoint() {
        return this.endpoint;
    }

    /**
     * a flag indicating whether this type is relevant to handicap browsing
     */
    public boolean isHandicapRelevant() {
        return this.handicapRelevant;
    }

    /**
     * a {@link List} of types valid as origins/destinations
     * (see {@link ILocationService#searchNodesByTypes(String, List)})
     */
    public static List<NodeType> getEndpointTypes() {

        List<NodeType> results = new ArrayList<>();

        for (NodeType type : NodeType.values()) {
            if (type.isEndpoint()) {
                results.add(type);
            }
        }

        return results;
    }

    /**
     * a {@link List} of types relevant to handicap browsing
     * (see {@link ILocationService#getNodesByTypesAndGroupAndFloor(List, INodeGroup, Integer)})
     */
    public static List<NodeType> getHandicapRelevantTypes() {

        List<NodeType> results = new ArrayList<>();

        for (NodeType type : NodeType.values()) {
            if (type.isHandicapRelevant()) {
                results.add(type);
            }
        }

        return results;
    }

    public static List<NodeType> getAll(){
        List<NodeType> results = new ArrayList<>();

        for(NodeType t: values()){
            results.add(t);
        }
        return results;
    }
}