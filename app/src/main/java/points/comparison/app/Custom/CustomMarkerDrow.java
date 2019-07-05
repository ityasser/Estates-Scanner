package points.comparison.app.Custom;

/**
 * Created by yasser on 7/7/2018.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CustomMarkerDrow {


    public static CustomMarkerDrow INSTANCE;
    private Context context;

    public static CustomMarkerDrow getInstance(Context context) {
        INSTANCE = new CustomMarkerDrow(context);
        return INSTANCE;
    }

    CustomMarkerDrow(Context context) {
        this.context = context;
    }

    public void draw(GoogleMap googleMap, LatLng location, Drawable circleDrawable, String title) {
      //  Drawable circleDrawable = ContextCompat.getDrawable(this.context, resDrawable);
        BitmapDescriptor markerIcon = this.getMarkerIconFromDrawable(circleDrawable);
        googleMap.addMarker((new MarkerOptions()).position(location).title(title).icon(markerIcon));
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}

