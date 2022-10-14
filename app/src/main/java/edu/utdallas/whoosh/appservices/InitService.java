package edu.utdallas.whoosh.appservices;

import android.content.Context;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.utdreqeng.whoosh.whoosh.R;

import edu.utdallas.whoosh.api.INodeGroup;

/**
 * Created by Dustin on 10/8/2015.
 */
public class InitService {

    public static void init(final Context context, final Callback callback){

        DBManager.getInstance().init(new Callback() {
            @Override
            public void call(boolean success) {

                if (success) {
                    LocationService.getInstance().init(context);

                    // FIXME: hard-coding this in the interest of time; should be fetched from Parse
                    INodeGroup ssb = NodeManager.getInstance().getNodeGroup("SSB");
                    LocationService.getInstance().putMapImage(new MapImage(ssb, 1, new LatLng(32.985743, -96.749099), new LatLng(32.986125, -96.748465), BitmapDescriptorFactory.fromResource(R.drawable.floorplan_ssb_1)));
                    LocationService.getInstance().putMapImage(new MapImage(ssb, 2, new LatLng(32.985743, -96.749099), new LatLng(32.986125, -96.748465), BitmapDescriptorFactory.fromResource(R.drawable.floorplan_ssb_2)));
                    LocationService.getInstance().putMapImage(new MapImage(ssb, 3, new LatLng(32.985743, -96.749099), new LatLng(32.986125, -96.748465), BitmapDescriptorFactory.fromResource(R.drawable.floorplan_ssb_3)));
                    LocationService.getInstance().putMapImage(new MapImage(ssb, 4, new LatLng(32.985743, -96.749099), new LatLng(32.986125, -96.748465), BitmapDescriptorFactory.fromResource(R.drawable.floorplan_ssb_4)));
                    INodeGroup atc = NodeManager.getInstance().getNodeGroup("ATC");
                    LocationService.getInstance().putMapImage(new MapImage(atc, 1, new LatLng(32.985638, -96.748335), new LatLng(32.986478, -96.746941), BitmapDescriptorFactory.fromResource(R.drawable.floorplan_atc_1)));
                    LocationService.getInstance().putMapImage(new MapImage(atc, 2, new LatLng(32.985638, -96.748335), new LatLng(32.986478, -96.746941), BitmapDescriptorFactory.fromResource(R.drawable.floorplan_atc_2)));
                    LocationService.getInstance().putMapImage(new MapImage(atc, 3, new LatLng(32.985638, -96.748335), new LatLng(32.986478, -96.746941), BitmapDescriptorFactory.fromResource(R.drawable.floorplan_atc_3)));
                    LocationService.getInstance().putMapImage(new MapImage(atc, 4, new LatLng(32.985638, -96.748335), new LatLng(32.986478, -96.746941), BitmapDescriptorFactory.fromResource(R.drawable.floorplan_atc_4)));
                }

                callback.call(success);
            }
        });
    }

    public static boolean isReady(){
        return DBManager.getInstance().isReady;
    }
}