package edu.utdallas.whoosh.api;

import java.util.List;

/**
 * Data interface that represents a {@link IRoute} in a form suited for a user interface and
 * human consumption.
 *
 * Created by sasha on 9/22/15.
 */
public interface IDirections {

    /**
     * the {@link IRoute} from which these directions were calculated
     */
    IRoute getRoute();

    /**
     * a set of steps, each of which represent a single {@link Node} in the {@link IRoute}
     */
    List<IDirectionsStep> getSteps();

}