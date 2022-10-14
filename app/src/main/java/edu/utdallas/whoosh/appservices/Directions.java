package edu.utdallas.whoosh.appservices;

import java.util.List;

import edu.utdallas.whoosh.api.INode;
import edu.utdallas.whoosh.api.StepType;

/**
 * Created by Marie on 11/3/2015.
 */
public class Directions {

    Route route;
    List<DirectionStep> directionSteps;

    Directions(){}

    Directions(Route route){
        this.route = route;
        getSteps();
    }

    /*builds a list of Direction steps based on the nodes in route*/
    public List<DirectionStep> getSteps(){
        List<INode> nodes = route.getPath();

        //notice we skip the last node
        for(int i = 0; i < nodes.size()-1; i++){
            //default is turn
            StepType type = StepType.Turn;
            if (i == 0){
                type = StepType.Head;
            }
            //2nd to last node means it's an arrival step
            if (i == nodes.size()-2){
                type = StepType.Arrive;
            }

            DirectionStep step = new DirectionStep(nodes.get(i), nodes.get(i++), type);
            directionSteps.add(step);
        }
        return directionSteps;
    }


}
