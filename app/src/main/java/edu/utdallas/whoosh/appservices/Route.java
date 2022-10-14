package edu.utdallas.whoosh.appservices;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.utdallas.whoosh.api.INode;
import edu.utdallas.whoosh.api.IRoute;
import edu.utdallas.whoosh.api.RouteType;

/**
 * Created by Dustin on 11/1/2015.
 */
public class Route implements IRoute {

    private List<Node> nodes;
    private RouteType type;
    private int distanceInFeet=0;
    private float time=0;

    public Route(List<Node> nodes, RouteType type)
    {
        this.nodes = new ArrayList<Node>(nodes);
        this.type = type;

        float tempDistance = 0;

        for(int i=0; i<nodes.size()-1; i++){
            tempDistance += Node.distanceInFeet(nodes.get(i), nodes.get(i+1));
        }
        distanceInFeet = (int)tempDistance;
        time = (float)((tempDistance / type.getRateInFeetPerSecond()) / 60);
    }

    @Override
    public INode getOrigin() {
        return nodes.get(0);
    }

    @Override
    public INode getDestination() {
        return nodes.get(nodes.size()-1);
    }

    @Override
    public RouteType getType() {
        return type;
    }

    @Override
    public List<INode> getPath() {
        return new ArrayList<INode>(nodes);
    }

    @Override
    public float getTimeInMinutes() {
        return time;
    }

    @Override
    public int getDistanceInFeet() {
        return distanceInFeet;
    }
}
