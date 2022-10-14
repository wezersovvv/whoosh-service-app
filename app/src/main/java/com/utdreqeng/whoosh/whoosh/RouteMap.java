package com.utdreqeng.whoosh.whoosh;

import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.utdallas.whoosh.api.GroupType;
import edu.utdallas.whoosh.api.IMapImage;
import edu.utdallas.whoosh.api.INode;
import edu.utdallas.whoosh.api.INodeGroup;
import edu.utdallas.whoosh.api.IRoute;
import edu.utdallas.whoosh.api.NodeType;
import edu.utdallas.whoosh.appservices.LocationService;

/**
 * Created by Dustin on 11/5/2015.
 */

public class RouteMap {

    private MainActivity activity;

    private GoogleMap map;
    private LocationManager locationManager;
    private LatLng loc;

    private LocationService locationService;

    private Map<GroupType, BitmapDescriptor> groupIcons = new HashMap<>();
    private Map<NodeType, BitmapDescriptor> nodeIcons = new HashMap<>();

    private List<Marker> groupMarkers = new ArrayList<>();

    private static final Float floorPlanZoomThreshold = 18f;
    private boolean floorPlansShown = false;
    private Map<INodeGroup, GroundOverlay> buildingOverlays = new HashMap<>();
    private Map<INodeGroup, List<Marker>> buildingMarkers = new HashMap<>();
    private Map<INodeGroup, Integer> buildingFloors = new HashMap<>();

    private List<Marker> routeMarkers = new ArrayList<>();
    private Polyline routeLine;


    public RouteMap(final MainActivity activity) {

        this.activity = activity;



        // set up map
        map = ((MapFragment) activity.getFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true);
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (cameraPosition.zoom >= floorPlanZoomThreshold && floorPlansShown != true) {
                    showFloorPlans();
                } else if (cameraPosition.zoom < floorPlanZoomThreshold && floorPlansShown == true) {
                    hideFloorPlans();
                }
            }
        });

        // disable the toolbar that pops up in the bottom right when a marker is selected
        map.getUiSettings().setMapToolbarEnabled(false);

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (floorPlansShown) {
                    final INodeGroup group = locationService.getSpanningGroup(latLng);
                    if (group != null) {
                        // show floor picker
                        final View floorPicker = activity.getLayoutInflater().inflate(R.layout.floor_picker_layout, null);

                        TextView tvGroupName = (TextView) floorPicker.findViewById(R.id.tvGroupName);
                        tvGroupName.setText(group.getName());

                        LinearLayout llFloors = (LinearLayout) floorPicker.findViewById(R.id.llFloors);

                        for (final Integer floor : group.getFloors()) {
                            Button button = new Button(activity);
                            String label = floor.equals(buildingFloors.get(group)) ? ("[" + floor + "]") : floor.toString();
                            button.setText(label);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ((RelativeLayout) activity.findViewById(R.id.mainLayout)).removeView(floorPicker);
                                    RouteMap.this.drawFloorPlan(group, floor);
                                }
                            });
                            llFloors.addView(button);
                        }

                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.addRule(RelativeLayout.CENTER_VERTICAL);
                        ((RelativeLayout) activity.findViewById(R.id.mainLayout)).addView(floorPicker, params);
                    }
                }
            }
        });



        locationManager = (LocationManager) activity.getSystemService(activity.getApplicationContext().LOCATION_SERVICE);
        locationService = LocationService.getInstance();



        // load NodeGroup icons
        groupIcons.put(GroupType.Building, BitmapDescriptorFactory.fromResource(R.drawable.grouptype_building));
        for (GroupType type : GroupType.values()) {
            if (groupIcons.containsKey(type) != true) {
                groupIcons.put(type, BitmapDescriptorFactory.defaultMarker());
            }
        }

        // load Node icons
        nodeIcons.put(NodeType.Ramp, BitmapDescriptorFactory.fromResource(R.drawable.nodetype_ramp));
        nodeIcons.put(NodeType.Elevator, BitmapDescriptorFactory.fromResource(R.drawable.nodetype_elevator));
        nodeIcons.put(NodeType.Door, BitmapDescriptorFactory.fromResource(R.drawable.nodetype_door));
        for (NodeType type : NodeType.values()) {
            if (nodeIcons.containsKey(type) != true) {
                nodeIcons.put(type, BitmapDescriptorFactory.defaultMarker());
            }
        }
    }

    public IMapImage drawFloorPlan(INodeGroup group, Integer floor) {

        IMapImage image = LocationService.getInstance().getGroupMap(group, floor);

        if (image != null) {
            // remove old overlay (if any)
            GroundOverlay overlay = this.buildingOverlays.get(group);
            if (overlay != null) {
                overlay.remove();
            }

            // draw new overlay
            LatLngBounds bounds = new LatLngBounds(image.getBottomLeftCoordinates(), image.getTopRightCoordinates());
            overlay = map.addGroundOverlay(new GroundOverlayOptions()
                            .image(image.getImage())
                            .positionFromBounds(bounds)
            );

            // register new overlay
            this.buildingOverlays.put(group, overlay);
            this.buildingFloors.put(group, floor);

            // remove old markers
            List<Marker> markers = this.buildingMarkers.get(group);
            if (markers != null) {
                for (Marker marker : this.buildingMarkers.get(group)) {
                    marker.remove();
                }
                markers.clear();
            } else {
                markers = new ArrayList<>();
                this.buildingMarkers.put(group, markers);
            }

            // draw handicap-relevant nodes
            for (INode node : this.locationService.getNodesByTypesAndGroupAndFloor(NodeType.getHandicapRelevantTypes(), group, floor)) {
                Marker marker = map.addMarker(new MarkerOptions()
                                .position(node.getCoordinates())
                                .title(node.getType().name())
                                .snippet("[" + node.getId() + "]" + " " + node.getName())
                                .icon(nodeIcons.get(node.getType()))
                                .anchor(0.5f, 0.5f)
                );
                markers.add(marker);
            }
        }

        return image;
    }

    public void initMap() {
        // init floor plan graphics and markers
        LatLngBounds.Builder boundsBuilder = LatLngBounds.builder();
        for (INodeGroup group : this.locationService.getGroupsByType(GroupType.Building)) {
            IMapImage image = this.drawFloorPlan(group, group.getDefaultFloor());
            if (image != null) {
                boundsBuilder.include(image.getBottomLeftCoordinates());
                boundsBuilder.include(image.getTopRightCoordinates());
            }
        }

        // init building markers
        for (INodeGroup group : this.locationService.getGroupsByType(GroupType.Building)) {
            Marker marker = map.addMarker(new MarkerOptions()
                            .position(group.getCenterCoordinates())
                            .title(group.getId())
                            .snippet(group.getName())
                            .icon(groupIcons.get(group.getType()))
                            .anchor(0.5f, 0.5f)
            );
            groupMarkers.add(marker);
        }

        this.hideFloorPlans();

        // zoom map
        map.moveCamera(
                CameraUpdateFactory.newLatLngBounds(
                        boundsBuilder.build(),
                        200
                )
        );
    }

    /**
     * listener to show floor plans when we're zoomed in far enough
     */
    public void showFloorPlans() {
        this.toggleFloorPlans(true);
    }

    /**
     * listener to hide floor plans when we're zoomed out far enough
     */
    public void hideFloorPlans() {
        this.toggleFloorPlans(false);
    }

    private void toggleFloorPlans(boolean showFloorPlans) {
        // hide floor plan overlays
        for (GroundOverlay overlay : this.buildingOverlays.values()) {
            overlay.setVisible(showFloorPlans);
        }

        // hide handicap markers
        for (List<Marker> markers : this.buildingMarkers.values()) {
            for (Marker marker : markers) {
                marker.setVisible(showFloorPlans);
            }
        }

        // show building markers
        for (Marker marker : groupMarkers) {
            marker.setVisible(!showFloorPlans);
        }

        this.floorPlansShown = showFloorPlans;
    }

    public void locateUser() {

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));

        if(location!=null){
            loc = new LatLng(location.getLatitude(), location.getLongitude());

            // drop pin for current position
            map.addMarker(new MarkerOptions().position(loc));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
        }
    }

    public LatLng getLastLocation(){
        return loc;
    }

    public void drawRoute(IRoute route) {

        // erase any current route
        eraseRoute();

        // draw origin/destination
        Marker marker = this.map.addMarker(new MarkerOptions()
                        .position(route.getOrigin().getCoordinates())
                        .title(route.getOrigin().getType().name())
                        .snippet("[" + route.getOrigin().getId() + "]" + " " + route.getOrigin().getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.route_start))
                        .anchor(0.5f, 0.5f)
        );
        this.routeMarkers.add(marker);
        marker = this.map.addMarker(new MarkerOptions()
                        .position(route.getDestination().getCoordinates())
                        .title(route.getDestination().getType().name())
                        .snippet("[" + route.getDestination().getId() + "]" + " " + route.getDestination().getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.route_finish))
                        .anchor(0.5f, 0.5f)
        );
        this.routeMarkers.add(marker);

        // draw polyline
        LatLngBounds.Builder boundsBuilder = LatLngBounds.builder();
        List<LatLng> points = new ArrayList<>();
        for (INode node : route.getPath()) {
            points.add(node.getCoordinates());
            boundsBuilder.include(node.getCoordinates());
            Log.d("RouteMap", "Path: " + node.getId());
        }
        this.routeLine = this.map.addPolyline(new PolylineOptions()
                        .addAll(points)
                        .width(20)
                        .color(Color.BLUE)
        );

        // zoom map on route
        map.moveCamera(
                CameraUpdateFactory.newLatLngBounds(
                        boundsBuilder.build(),
                        200
                )
        );
    }

    public void eraseRoute() {

        // remove origin/destination markers
        for (Marker marker : this.routeMarkers) {
            marker.remove();
        }
        this.routeMarkers.clear();

        // remove polyline
        if (this.routeLine != null) {
            this.routeLine.remove();
        }
        this.routeLine = null;
    }
}