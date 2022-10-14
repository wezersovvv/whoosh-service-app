package edu.utdallas.whoosh.appservices;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.TreeMap;

import edu.utdallas.whoosh.api.INode;
import edu.utdallas.whoosh.api.IRoute;
import edu.utdallas.whoosh.api.IRoutingService;
import edu.utdallas.whoosh.api.RouteType;

/**
 * Created by Dustin on 11/1/2015.
 */
public class RoutingService implements IRoutingService
{
    private NodeManager nodeManager = NodeManager.getInstance();
    private final float AVERAGE_HEIGHT = 10f;

    public RoutingService(){}

    /**
     * Get Route from origin node to destination node. Should be called from a separate thread
     * than the UI thread.
     * @return Route between the nodes. If null, no path was found.
     */
    @Override
    public IRoute getRoute(INode start, INode end, RouteType type) {

        PriorityQueue<NodeHolder> openSet = new PriorityQueue<NodeHolder>();
        HashMap<String, NodeHolder> closedSet = new HashMap<String, NodeHolder>();
        HashMap<String, NodeHolder> nodeMap = new HashMap<String, NodeHolder>();
        NodeHolder current, temp,
                   origin = new NodeHolder((Node)start),
                   destination = new NodeHolder((Node)end);
        float tempRating;

        for(Node n: nodeManager.getNodes()){
            nodeMap.put(n.getId(), new NodeHolder(n));
        }
        openSet.add(origin);
        Log.d("RoutingService", "Getting route");

        while(openSet.size() != 0){

            current = openSet.poll();
            closedSet.put(current.key, current);

            if(current.equals(destination)){
                return buildRoute(current, nodeMap, type);
            }
            Log.d("RoutingService","Building route");

            for(Node n: current.node.getAdjacentNodes()){

                Log.d("RoutingService","Current: "+current.node.getId()+" Checking node "+n.getId()+" in route");

                if(closedSet.containsKey(n.getId())){
                    continue;
                }
                tempRating = current.g + Node.distanceInFeet(current.node, n) + Math.abs(current.node.getFloor() - n.getFloor())*AVERAGE_HEIGHT;
                temp = nodeMap.get(n.getId());

                if(!openSet.contains(temp)){
                    openSet.add(temp);
                }
                else if(tempRating > temp.g){
                    continue;
                }

                temp.parent = current.node.getId();
                temp.g = tempRating;
                temp.f = temp.g + getH(temp, destination);
            }
        }

        return null;
    }

    //Heuristic using the Manhattan Distance
    private float getH(NodeHolder n1, NodeHolder n2){

        float difLat = (float)(Math.abs(n2.node.getCoordinates().latitude - n1.node.getCoordinates().latitude));
        float difLong = (float)(Math.abs(n2.node.getCoordinates().longitude - n1.node.getCoordinates().longitude));

        return Node.distanceInFeet(n1.node.getCoordinates().latitude, n1.node.getCoordinates().longitude,
                n1.node.getCoordinates().latitude, n1.node.getCoordinates().longitude+difLong) +
                Node.distanceInFeet(n1.node.getCoordinates().latitude, n1.node.getCoordinates().longitude,
                        n1.node.getCoordinates().latitude+difLat, n1.node.getCoordinates().longitude);
        //return 0;
    }

    private Route buildRoute(NodeHolder start, HashMap<String, NodeHolder> pathMap, RouteType type){

        ArrayList<Node> path = new ArrayList<Node>();
        NodeHolder current = start;

        while(true){
            path.add(0, current.node);

            if(current.parent == null){
                break;
            }

            current = pathMap.get(current.parent);
        }
        return new Route(path, type);
    }

    private class NodeHolder implements Comparable{
        public Node node;
        public float f, g;
        public String key, parent;

        public NodeHolder(Node n){
            this.node = n;
            this.key = node.getId();
        }

        @Override
        public int compareTo(Object another) {
            if(((NodeHolder)another).f < f){
                return 1;
            }
            else if(((NodeHolder)another).f > f){
                return -1;
            }
            else return 0;
        }

        @Override
        public boolean equals(Object another){
            if(((NodeHolder)another).key == key){
                return true;
            }
            return false;
        }
    }
}
