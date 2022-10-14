package edu.utdallas.whoosh.api;

import edu.utdallas.whoosh.appservices.Node;

/**
 * Service interface that provides functionality to calculate a {@link IRoute} between two {@link INode}s.
<<<<<<< HEAD
 *
=======
>>>>>>> master
 * Created by sasha on 9/21/15.
 */
public interface IRoutingService {

    /**
     * Given an {@link INode origin}, {@link INode destination}, and {@link RouteType mode of transit},
     * calculates and returns a {@link IRoute} describing the shortest distance (in terms of time).
     */
    IRoute getRoute(INode origin, INode destination, RouteType type);

}