package edu.utdallas.whoosh.api;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Data interface that represents a group of {@link INode}s, such as the {@link GroupType#Building} "ECSS"
 *
 * Created by sasha on 9/21/15.
 */
public interface INodeGroup {

    /**
     * natural key, such as "ECSS"
     */
    String getId();

    /**
     * colloquial name, such as "Engineering and Computer Science South"
     */
    String getName();

    /**
     * the type of group, such as {@link GroupType#Building}
     */
    GroupType getType();

    /**
     * {@link List} of routable vertical levels within this group, typically floor id's within a building;
     * {@link GroupType#OutdoorArea} groups should return a single element of {@code 1}
     */
    List<Integer> getFloors();

    /**
     * the initially selected level of this group (typically the "ground" level)
     */
    Integer getDefaultFloor();

    /**
     * the center latitude/longitude of this group; could be used to provide a marker/control to switch
     * from global map to detailed maps (for this node group)
     */
    LatLng getCenterCoordinates();

}