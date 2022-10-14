package edu.utdallas.whoosh.appservices;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

import edu.utdallas.whoosh.api.IMapImage;
import edu.utdallas.whoosh.api.INodeGroup;

/**
 * Created by Dustin on 10/7/2015.
 */
public class MapImage implements IMapImage
{
    private INodeGroup group;
    private Integer floor;
    private LatLng bottomLeft;
    private LatLng topRight;

    private BitmapDescriptor image;



    public MapImage(INodeGroup group, Integer floor, LatLng bottomLeft, LatLng topRight, BitmapDescriptor image) {
        this.group = group;
        this.floor = floor;
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;

        this.image = image;
    }


    @Override
    public BitmapDescriptor getImage() {
        return this.image;
    }

    @Override
    public LatLng getBottomLeftCoordinates() {
        return this.bottomLeft;
    }

    @Override
    public LatLng getTopRightCoordinates() {
        return this.topRight;
    }

    @Override
    public INodeGroup getGroup() {
        return this.group;
    }

    @Override
    public Integer getFloor() {
        return this.floor;
    }
}