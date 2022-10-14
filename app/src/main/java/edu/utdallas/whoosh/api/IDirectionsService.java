package edu.utdallas.whoosh.api;

/**
 * Service interface that provides functionality to convert a {@link IRoute} (technical representation)
 * to {@link IDirections} (human-oriented representation).
 *
 * Created by sasha on 9/22/15.
 */
public interface IDirectionsService {

    /**
     * given a {@link IRoute}, calculates and returns the {@link IDirections}
     */
    IDirections getDirections(IRoute route);

}