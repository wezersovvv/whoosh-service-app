package edu.utdallas.whoosh.appservices;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.utdallas.whoosh.api.GroupType;
import edu.utdallas.whoosh.api.ILocationService;
import edu.utdallas.whoosh.api.IMapImage;
import edu.utdallas.whoosh.api.INode;
import edu.utdallas.whoosh.api.INodeGroup;
import edu.utdallas.whoosh.api.NodeType;

/**
 * Created by Dustin on 10/7/2015.
 * Updated by Taber Hurst.
 */
public class LocationService implements ILocationService {
    private HashMap<String, IMapImage> mapImages = new HashMap<String, IMapImage>();
    private IMapImage campusMap;
    private Context context;
    private static LocationService instance = null;

    @Override
    public Node getClosestNode(LatLng coordinates) {
        NodeManager manager = NodeManager.getInstance();

        List<Node> possibleNodes = manager.getNodesFromGroup(getClosestGroup(coordinates));
        Node currentNode,shortestNode;
        shortestNode = null;
        double currDistance,shortestDistance,latDif,lngDif;

        shortestDistance = -1.0;

        Iterator<Node> iterator = possibleNodes.listIterator();
        while(iterator.hasNext())
        {
            currentNode = iterator.next();
            latDif = coordinates.latitude - currentNode.getCoordinates().latitude;
            lngDif = coordinates.longitude - currentNode.getCoordinates().longitude;

            currDistance = Math.sqrt((latDif*latDif) + (lngDif*lngDif));
            if(shortestDistance == -1.0)
            {
                shortestDistance = currDistance;
                shortestNode = currentNode;
            }
            else if(shortestDistance > currDistance)
            {
                shortestDistance = currDistance;
                shortestNode = currentNode;
            }
        }

        return shortestNode;
    }

    @Override
    public List<INode> searchNodesByTypes(String query, List<NodeType> types) {
        NodeManager manager = NodeManager.getInstance();
        HashSet<Node> tempSet = new HashSet<Node>();

        if (query != null) {
            try {
                tempSet.addAll(manager.doNodeQuery(query));
            } catch (Exception e) {
            }
        } else {
            tempSet.addAll(manager.getNodes());
        }

        Iterator<Node> i = tempSet.iterator();
        while (i.hasNext()) {
            Node n = i.next();
            if (!types.contains(n.getType())) {
                i.remove();
            }
        }
        return new ArrayList<INode>(tempSet);
    }

    @Override //Not tested
    public List<INode> searchNodesByTypesAndGroup(String query, List<NodeType> types, INodeGroup group) {
        HashSet<INode> tempSet = new HashSet<INode>();

        for(INode n : searchNodesByTypes(query, types)){
            if(n.getGroup().getId().compareTo(group.getId()) == 0){
                tempSet.add(n);
            }
        }

        return new ArrayList<INode>(tempSet);
    }

    @Override //Not tested
    public List<INode> getNodesByTypesAndGroupAndFloor(List<NodeType> types, INodeGroup group, Integer floor) {
        HashSet<INode> tempSet = new HashSet<INode>();

        for(INode n: searchNodesByTypesAndGroup(null, types, group)){
            if(n.getFloor() == floor){
                tempSet.add(n);
            }
        }

        return new ArrayList<INode>(tempSet);
    }

    @Override
    public NodeGroup getClosestGroup(LatLng coordinates) {
        NodeGroup currGroup,closestGroup;
        closestGroup = null;

        double currDistance,shortestDistance,latDif,lngDif;
        shortestDistance = -1.0;

        NodeManager manager = NodeManager.getInstance();

        List<NodeGroup> groups = manager.getNodeGroups();
        Iterator<NodeGroup> iterator = groups.listIterator();

        while(iterator.hasNext())
        {
            currGroup = iterator.next();
            latDif = coordinates.latitude - currGroup.getCenterCoordinates().latitude;
            lngDif = coordinates.longitude - currGroup.getCenterCoordinates().longitude;

            currDistance = Math.sqrt((latDif * latDif) + (lngDif * lngDif));
            if(shortestDistance == -1.0
                    || shortestDistance > currDistance)
            {
                shortestDistance = currDistance;
                closestGroup = currGroup;
            }
        }

        return closestGroup;
    }

    @Override
    public List<INodeGroup> getGroupsByType(GroupType type) {
        NodeManager manager = NodeManager.getInstance();
        List<NodeGroup> groups = manager.getNodeGroups();
        List<INodeGroup> groupsByType = new ArrayList<>();
        NodeGroup currGroup;
        Iterator<NodeGroup> iterator = groups.listIterator();
        while(iterator.hasNext()) {
            currGroup = iterator.next();
            if(currGroup.getType() == type)
            {
                groupsByType.add(currGroup);
            }
        }

        return groupsByType;
    }

    @Override
    public IMapImage getGroupMap(INodeGroup group, Integer floor) {
        return mapImages.get(getImageKey(group, floor));
    }

    @Override
    public INodeGroup getSpanningGroup(LatLng coordinates) {

        INodeGroup result = null;

        for (IMapImage image : mapImages.values()) {
            LatLngBounds bounds = new LatLngBounds(image.getBottomLeftCoordinates(), image.getTopRightCoordinates());
            if (bounds.contains(coordinates)) {
                result = image.getGroup();
                break;
            }
        }

        return result;
    }

    public static LocationService getInstance() {
        if (instance == null) {
            instance = new LocationService();
        }
        return instance;
    }

    public void init(Context c) {
        this.context = c;
    }

    public void putMapImage(IMapImage image) {
        mapImages.put(getImageKey(image), image);
    }

    private String getImageKey(IMapImage image) {
        return this.getImageKey(image.getGroup(), image.getFloor());
    }

    private String getImageKey(INodeGroup group, Integer floor) {
        return group.getId() + floor;
    }
}