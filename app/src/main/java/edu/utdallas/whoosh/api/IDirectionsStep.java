package edu.utdallas.whoosh.api;

/**
 * Data interface that represents a single step in a set of {@link IDirections}.
 *
 * Created by sasha on 9/22/15.
 */
public interface IDirectionsStep {

    /**
     * the starting {@link INode} of this step
     */
    INode getNode();

    /**
     * distance from this step to the next
     */
    Integer getDistanceInFeet();

    /**
     * the type of direction this step represents (ex. {@link StepType#Turn})
     */
    StepType getType();

    /**
     * the direction of this step (ex. {@link StepDirection#Left})
     */
    StepDirection getDirection();

    /**
     * the human-readable instructions for this step, in complete sentence(s)
     */
    String getInstructions();

}