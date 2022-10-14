package com.utdreqeng.whoosh.whoosh;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;

import java.util.Calendar;
import java.util.List;

import edu.utdallas.whoosh.api.INode;
import edu.utdallas.whoosh.api.IRoute;
import edu.utdallas.whoosh.api.NodeType;
import edu.utdallas.whoosh.api.RouteType;
import edu.utdallas.whoosh.appservices.Callback;
import edu.utdallas.whoosh.appservices.InitService;
import edu.utdallas.whoosh.appservices.LocationService;
import edu.utdallas.whoosh.appservices.RoutingService;

public class MainActivity extends AppCompatActivity  {

    //Parse App ID and client key
    public static final String PARSE_APPLICATION_ID = "YB7DqZB3O0LfmIKzd9NzMQ6uhIXRhoawgK7p5B1M";
    public static final String PARSE_CLIENT_KEY = "kA1LGDmyML7rhXEDkQjgNIKw8cNM5VoDpsO7Drxl";

    private View lastTopBar = null;
    private int lastTopId = 0;
    private RouteMap map;

    private RoutingService routingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Enable Local Datastore
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);

        setContentView(R.layout.activity_main);
        map = new RouteMap(this);
        map.locateUser();

        ((FloatingActionButton)findViewById(R.id.navButtonTop)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    map.locateUser();
                }
                return false;
            }
        });

        ((FloatingActionButton)findViewById(R.id.navButtonBottom)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setTopBar(2);
                }
                return false;
            }
        });

        setTopBar(0);

        //Initialize the backend services
        InitService.init(
                this.getApplicationContext(),
                new Callback() {
                    @Override
                    public void call(boolean success) {
                        if (success) {
                            map.initMap();
                            routingService = new RoutingService();
                        }
                    }
                }
        );

        String[] osArray = { "Campus Map", "Directory", "About" };
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        ListView mDrawerList = (ListView)findViewById(R.id.navList);

        ImageView iv = new ImageView(getApplicationContext());
        iv.setImageResource(R.drawable.nav_title);
        mDrawerList.addHeaderView(iv);

        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                if(position == 1){

                }
                else if(position == 2){
                    startActivityForResult(new Intent(getApplicationContext(), DirectoryActivity.class), RESULT_OK);
                }
                else if(position == 3){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://utdwhoosh.github.io/")));
                }
            }
        });
    }

    public void showBottomBar(float time, INode destination){

        View temp = findViewById(R.id.bottomBar);
        double rTime = Math.round(time * 100f) / 100;

        Calendar date = Calendar.getInstance();
        date.add(Calendar.MILLISECOND, (int) (time * 60000f));

        temp.setVisibility(View.VISIBLE);
        ((TextView)temp.findViewById(R.id.bottom_destination)).setText(destination.getGroup().getId() + " " + destination.getName());
        ((TextView)temp.findViewById(R.id.bottom_time)).setText("" + rTime + " min");
        ((TextView)temp.findViewById(R.id.bottom_eta)).setText("ETA: " + date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE));
    }

    public void hideBottomBar(){
        findViewById(R.id.bottomBar).setVisibility(View.INVISIBLE);
    }

    public void setTopBar(int id){

        View temp = null;
        hideBottomBar();

        if(lastTopBar != null){
            lastTopBar.clearFocus();
            ((RelativeLayout)findViewById(R.id.mainLayout)).removeView(lastTopBar);
        }

        switch(id){
            case 0:
                    temp = getLayoutInflater().inflate(R.layout.searchbar_layout, null);

                    ((Button)temp.findViewById(R.id.searchButton)).setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                setTopBar(1);
                            }
                            return false;
                        }
                    }); break;
            case 1:
                    temp = getLayoutInflater().inflate(R.layout.searchbar_layout2, null);
                    temp.findViewById(R.id.searchBox).requestFocus();

                    ((EditText)temp.findViewById(R.id.searchBox)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                            if (actionId == EditorInfo.IME_ACTION_DONE) {
                                setTopBar(2);
                            }
                            return false;
                        }
                    });
                    openKeyboard();

                    ((Button)temp.findViewById(R.id.searchExitButton)).setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                setTopBar(0);
                            }
                            return false;
                        }
                    }); break;

            case 2:
                    String startText = "My Location", endText = "";

                    if(lastTopId == 1){
                        endText = ((EditText)lastTopBar.findViewById(R.id.searchBox)).getText().toString();
                    }

                    temp = getLayoutInflater().inflate(R.layout.navigation_layout, null);
                    final EditText etEnd = ((EditText) temp.findViewById(R.id.nav_fieldEnd));
                    final EditText etStart = ((EditText) temp.findViewById(R.id.nav_fieldStart));

                    ((EditText) temp.findViewById(R.id.nav_fieldStart)).setText(startText);
                    ((EditText) temp.findViewById(R.id.nav_fieldStart)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                            if (actionId == EditorInfo.IME_ACTION_DONE) {
                                ((EditText) etEnd.findViewById(R.id.nav_fieldEnd)).requestFocus();
                            }

                            return false;
                        }
                    });

                    ((EditText) temp.findViewById(R.id.nav_fieldEnd)).setText(endText);
                    ((EditText) temp.findViewById(R.id.nav_fieldEnd)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                            if (actionId == EditorInfo.IME_ACTION_DONE) {

                                INode start = null;
                                List<INode> endPoints = LocationService.getInstance().searchNodesByTypes(
                                        etEnd.getText().toString(), NodeType.getAll());

                                if (etStart.getText().toString().compareTo("My Location") == 0) {
                                    start = LocationService.getInstance().getClosestNode(map.getLastLocation());
                                } else {
                                    List<INode> startPoints = LocationService.getInstance().searchNodesByTypes(
                                            etStart.getText().toString(), NodeType.getAll());

                                    if (startPoints.size() == 0) {
                                        etStart.setText("");
                                    } else {
                                        start = startPoints.get(0);
                                    }
                                }

                                if (start != null) {
                                    if (endPoints.size() == 0) {
                                        v.setText("");
                                        Toast.makeText(getApplicationContext(), "Invalid destination", Toast.LENGTH_SHORT).show();
                                        hideBottomBar();
                                    } else {
                                        INode end = endPoints.get(0);
                                        IRoute route = routingService.getRoute(start, end, RouteType.Wheelchair);

                                        map.drawRoute(route);

                                        // show bottom bar
                                        showBottomBar(route.getTimeInMinutes(), end);
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Invalid start point", Toast.LENGTH_SHORT).show();
                                }
                            }
                            return false;
                        }
                    });

                    ((Button) temp.findViewById(R.id.nav_screen_backButton)).setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                map.eraseRoute();

                                // hide bottom bar
                                hideBottomBar();

                                // show default top bar
                                setTopBar(0);
                            }
                            return false;
                        }
                    });
                    break;

            default:
            break;
        }

        if(id==0||id==1)

        {
            ((FloatingActionButton) findViewById(R.id.navButtonBottom)).setVisibility(View.VISIBLE);
            ((FloatingActionButton) findViewById(R.id.navButtonTop)).setVisibility(View.VISIBLE);
        }

        else

        {
            ((FloatingActionButton) findViewById(R.id.navButtonBottom)).setVisibility(View.INVISIBLE);
            ((FloatingActionButton) findViewById(R.id.navButtonTop)).setVisibility(View.INVISIBLE);
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        ((RelativeLayout)

        findViewById(R.id.mainLayout)

        ).

        addView(temp, params);

        lastTopBar=temp;
        lastTopId=id;
    }

    private void openKeyboard(){
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
                InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        findViewById(R.id.action_directory);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
