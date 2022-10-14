package edu.utdallas.whoosh.api;

import java.util.List;
import edu.utdallas.whoosh.appservices.*;

/**
 * Data interface that represents a path between two {@link INode}s as a collection of intermediate
 * {@link INode}s, as well as the parameters used to generate it.
 *
 * Created by sasha on 9/21/15.
 */
public interface IRoute {

    /**
     * the "from" {@link INode}; should be the first element in {@link #getPath()}
     */
    INode getOrigin();

    /**
     * the "to" {@link INode}; should be the last element in {@link #getPath()}
     */
    INode getDestination();

    /**
     * the {@link RouteType mode of transit}
     */
    RouteType getType();

    /**
     * ordered list of {@link INode}s that describes the path between this route's origin and destination
     */
    List<INode> getPath();

    /**
     * the estimated length of time in minutes to complete this route, given its {@link #getType() mode of transit}
     */
    float getTimeInMinutes();

    /**
     * the calculated distance in feet of this route
     */
    int getDistanceInFeet();

}