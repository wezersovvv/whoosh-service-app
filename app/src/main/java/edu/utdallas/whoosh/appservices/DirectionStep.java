package edu.utdallas.whoosh.appservices;

import edu.utdallas.whoosh.api.INode;
import edu.utdallas.whoosh.api.StepDirection;
import edu.utdallas.whoosh.api.StepType;

/**
 * Created by Marie on 11/3/2015.
 * Implementation of interface IDirectionStep
 * data representaton of a single direction step
 * intended to be used in a set of direction steps to form 'directions'
 */
public class DirectionStep {

    private INode start, end;
    StepType type;
    StepDirection direction;
    int distance; //distance from start to end node in feet (round error?)


    DirectionStep(INode start, INode end, StepType type){
        this.start = start;
        this.end = end;
        this.type = type;
        findDirection(); //calculate and set direction
        calcDistance(); //calculate and set distance
    }

    /*the start node of the step*/
    INode getStartNode(){
        return start;
    }

    INode getEndNode(){
        return end;
    }

    /*returns the distance from this step to the next*/
    Integer getDistanceInFeet(){
            return distance;
    }

    void calcDistance(){
        int dist = 0;
        /**TODO: calculate the distance between steps
         * ask about acceptable error for rounding to whole feet
         */
        distance = dist;

    }

    /*returns the step type*/
    StepType getType(){
        return type;
    }

    /*returns the direction of the step*/
    StepDirection getDirection(){
        return direction;
    }

    /*calculates the direction of the step from start to end */
    void findDirection(){
        /*TODO: finish this*/
        direction = StepDirection.Straight; // default for now..
    }

    String getInstructions(){
        String instructions = "";

        return instructions; //TODO: create instruction string based on distance and direction
    }
}
