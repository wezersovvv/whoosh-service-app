package edu.utdallas.whoosh.api;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import edu.utdallas.whoosh.appservices.NodeGroup;

/**
 * Data interface that represents a routable geographic point, as well as the various data describing it.
 *
 * Created by sasha on 9/20/15.
 */
public interface INode {

    /**
     * a natural key
     */
    String getId();

    /**
     * geographic coordinates represented by this node
     */
    LatLng getCoordinates();

    /**
     * the {@link NodeGroup} this node belongs to, such as the {@link GroupType#Building} "ECSS"
     */
    NodeGroup getGroup();

    /**
     * a second-level grouping this node belongs to, such as {@link NodeType#Room} "2.123"
     */
    String getSubgroup();

    /**
     * a colloquial name given this geographic point, or {@link #getSubgroup()} if no such name exists
     */
    String getName();

    /**
     * the type of geographic point this node represents, such as {@link NodeType#Room}
     */
    NodeType getType();

    /**
     * a {@link List} of nodes directly routable from this one
     */
    List<edu.utdallas.whoosh.appservices.Node> getAdjacentNodes();

    /**
     * a vertical qualifier for this geographic point, expressed in terms of its floor id within that building;
     * {@link GroupType#OutdoorArea} nodes should indicate a value of {@code 1}
     */
    Integer getFloor();

}