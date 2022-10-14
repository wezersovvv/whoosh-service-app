package edu.utdallas.whoosh.api;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

/**
 * Data interface that aggregates a graphical asset with its metadata (right now, just the latitude/longitude
 * bounds).
 *
 * Created by sasha on 9/27/15.
 */
public interface IMapImage {

    /**
     * the graphical asset of this map
     */
    BitmapDescriptor getImage();

    LatLng getBottomLeftCoordinates();
    LatLng getTopRightCoordinates();

    INodeGroup getGroup();
    Integer getFloor();
}