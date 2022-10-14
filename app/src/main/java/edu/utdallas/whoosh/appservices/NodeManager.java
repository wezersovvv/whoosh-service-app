package edu.utdallas.whoosh.appservices;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import edu.utdallas.whoosh.api.NodeType;

/**
 * Singleton used to store and retrieve nodes in the application. Intended to be used by
 * all service classes for simple node management.
 *
 * @author Dustin Grannemann
 */
public class NodeManager {

    private static NodeManager instance = null;
    private HashMap<String, ArrayList<String>> nodesByGroup = new HashMap<String, ArrayList<String>>();
    private HashMap<String, ArrayList<String>> nodesBySubgroup = new HashMap<String, ArrayList<String>>();
    private HashMap<String, NodeGroup> nodeGroups = new HashMap<String, NodeGroup>();
    private HashMap<NodeType, ArrayList<String>> nodeTypes = new HashMap<NodeType, ArrayList<String>>();
    private static HashMap<String, Node> nodes = new HashMap<String, Node>();

    /**Puts a single node into the node manager.
     * @param n - Node*/
    public void putNode(Node n){
        nodes.put(n.getId(), n);

        if(!nodesByGroup.containsKey(n.getGroup().getId())){
            Log.d("NodeManager", "added " + n.getName() + " to node list.");
            nodesByGroup.put(n.getGroup().getId(), new ArrayList<String>());
        }
        if(!nodesBySubgroup.containsKey(n.getName().toLowerCase())){
            nodesBySubgroup.put(n.getName().toLowerCase(), new ArrayList<String>());
        }
        if(!nodeTypes.containsKey(n.getType())){
            nodeTypes.put(n.getType(), new ArrayList<String>());
        }

        nodeTypes.get(n.getType()).add(n.getId());
        nodesByGroup.get(n.getGroup().getId()).add(n.getId());
        nodesBySubgroup.get(n.getName().toLowerCase()).add(n.getId());
    }

    /**
     * Puts several nodes into the node manager.
     * @param nodes - list of nodes
     */
    public void putNodes(List<Node> nodes){
        for(Node n: nodes){
            putNode(n);
        }
    }

    /**Get a node by its id
     * @param id - id of node to retrieve
     * @return Node
     */
    public Node getNode(String id) {
        return nodes.get(id);
    }

    public Collection<Node> getNodes(){
        return nodes.values();
    }

    /**
     * Get a list of nodes in a particular group
     * @param group - NodeGroup to retrieve nodes from
     * @return list of nodes
     */
    public List<Node> getNodesFromGroup(NodeGroup group){
        return getNodes(nodesByGroup.get(group.getId()));
    }

    /**Get a list of nodes in a particular subgroup
     * @param subgroup - Name of subgroup to retrieve nodes from
     * @return list of nodes*/
    public List<Node> getNodesFromSubgroup(String subgroup){
        return getNodes(nodesBySubgroup.get(subgroup.toLowerCase()));
    }

    public List<Node> doNodeQuery(String query){
        try{
            String[] tokens = query.split(" ");
            HashSet<Node> nodeSet = new HashSet<Node>();

            if(tokens.length == 2){

                if(tokens[0].toUpperCase().compareTo("ATEC") == 0){
                    tokens[0] = "ATC";
                }

                for(Node n: getNodesFromSubgroup(tokens[1].toLowerCase())){
                    if(n.getGroup().getId().compareTo(tokens[0].toUpperCase()) == 0){
                        nodeSet.add(n);
                    }
                }
            }
            else{
                nodeSet.addAll(getNodesFromSubgroup(query.toLowerCase()));
            }
            return new ArrayList<Node>(nodeSet);
        }
        catch(Exception e){
            Log.d("NodeManager","Invalid query string");
        }
        return new ArrayList<Node>();
    }

    /**Get all nodes of a particular type
     * @param type - value of type of node to retrieve
     * @return list of nodes*/
    public List<Node> getNodesFromType(NodeType type){
        if(nodeTypes.containsKey(type)){
            return getNodes(nodeTypes.get(type));
        }
        else return new ArrayList<Node>();
    }

    public void addNodeGroup(NodeGroup n){
        nodeGroups.put(n.getId(), n);
    }

    public NodeGroup getNodeGroup(String id){
        return nodeGroups.get(id);
    }

    public List<NodeGroup> getNodeGroups(){
        return new ArrayList<NodeGroup>(nodeGroups.values());
    }

    private List<Node> getNodes(ArrayList<String> nodeList){
        ArrayList<Node> temp = new ArrayList<Node>();

        for(String s: nodeList){
            temp.add(nodes.get(s));
        }
        return temp;
    }

    public static NodeManager getInstance(){

        if(instance==null){
            instance = new NodeManager();
        }
        return instance;
    }
}
