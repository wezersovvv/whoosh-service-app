package edu.utdallas.whoosh.appservices;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marie on 10/3/2015.
 * This class is intended to be a entry point for ParseDB and the application
 */
public class DBManager {


    private static DBManager instance = null;
    public static boolean isReady = false;
    Callback initCallback;

    /**init(): initializes the data variables for the application*/
    public void init(Callback callback){

        this.initCallback = callback;

        Log.d(getClass().getName(), "DBManager.init(): Started");

        initNodeGroups();
    }

    /**initNodeGroups(): retrieves all NodeGroup ParseObjects from Parse
     *  & adds them to NodeManager's list of NodeGroups
     */
    private void initNodeGroups(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("NodeGroup");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseNodeGroups, ParseException e) {
                if (e == null) {
                    for (ParseObject object : parseNodeGroups) {
                        NodeGroup nodegroup = new NodeGroup(object);
                        NodeManager.getInstance().addNodeGroup(nodegroup);
                    }
                    initNodes();
                } else {
                    //something went wrong
                    Log.d(getClass().getName(), "DBManager.initNodeGroups(): " + e);
                    initCallback.call(Boolean.FALSE);
                }
            }
        });
    }

    /**initNodes(): retrieves all Node ParseObjects from Parse
     * creates a list of nodes with them, and adds the list to ModeManager
     */
    private void initNodes(){
        Log.d("DBManager", "Trying to init nodes...");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Node").setLimit(200);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseNodes, ParseException e) {

                if (e == null) {
                    List<Node> nodes = new ArrayList<Node>();
                    //instantiate each node

                    for(ParseObject object: parseNodes){
                        Node node = new Node(object);
                        nodes.add(node);

                        Log.d("DBManager", "added " + node.getId() + " to node list.");
                    }
                    NodeManager.getInstance().putNodes(nodes);

                    //set each node's adjacent nodes
                    int i = 0;
                    for(ParseObject object: parseNodes){
                        nodes.get(i).setAdjacentNodes(object);
                        i++;
                    }

                    isReady = true;
                    Log.d(getClass().getName(), "DBManager.init(): Finished");

                    initCallback.call(Boolean.TRUE);

                } else {
                    //something went wrong
                    Log.d(getClass().getName(), "DBManager.initNodes(): " + e);
                    initCallback.call(Boolean.FALSE);
                }
            }
        });
    }

    /**
     * getInstance() : returns an instance of DBManger*/
    public static DBManager getInstance(){

        if(instance==null){
            instance = new DBManager();
        }

        return instance;
    }
}

