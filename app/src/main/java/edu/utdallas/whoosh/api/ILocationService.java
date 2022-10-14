package edu.utdallas.whoosh.api;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Service interface that provides functionality to search and retrieve {@link INode}s,
 * {@link INodeGroup}s, and map graphics (as {@link BitmapDescriptor}s).
 *
 * Created by sasha on 9/22/15.
 */
public interface ILocationService {

    /**
     * given a set of coordinates, returns the closest {@link INode} by distance
     */
    INode getClosestNode(LatLng coordinates);

    /**
     * Given a search string and list of {@link NodeType}s, returns a list of matching {@link INode}s.
     * If the intent is to locate origin/destination nodes for routing, an example call could be
     * {@code searchNodesByTypes("2.2", {@link NodeType#getEndpointTypes()})}.
     */
    List<INode> searchNodesByTypes(String query, List<NodeType> types);

    /**
     * Given a search string, a list of {@link NodeType}s, and a {@link INodeGroup}, returns a list
     * of matching {@link INode}s.
     */
    List<INode> searchNodesByTypesAndGroup(String query, List<NodeType> types, INodeGroup group);

    /**
     * Given a list of {@link NodeType}s, a {@link INodeGroup}, and a floor, returns a list
     * of matching {@link INode}s. If the intent is to locate handicap-related nodes for browsing
     * within a certain building floor, an example call could be
     * {@code getNodesByTypesAndGroupAndFloor({@link NodeType#getHandicapRelevantTypes()}, group)}.
     */
    List<INode> getNodesByTypesAndGroupAndFloor(List<NodeType> types, INodeGroup group, Integer floor);

    /**
     * given a set of coordinates, returns the closest {@link INodeGroup} by distance
     */
    INodeGroup getClosestGroup(LatLng coordinates);

    /**
     * given a {@link GroupType}, returns all {@link INodeGroup}s of that type
     */
    List<INodeGroup> getGroupsByType(GroupType type);

    /**
     * given a {@link INodeGroup} and {@code floor} index, returns a {@link IMapImage} for the
     * map of that building/floor
     */
    IMapImage getGroupMap(INodeGroup group, Integer floor);

    INodeGroup getSpanningGroup(LatLng coordinates);
}