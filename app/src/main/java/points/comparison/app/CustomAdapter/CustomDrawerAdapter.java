package points.comparison.app.CustomAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import points.comparison.app.R;
import points.comparison.app.models.DrawerItem;

/**
 * Created by yasser on 16/12/2017.
 */

public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem> {

    Context context;
    List<DrawerItem> drawerItemList;
    int layoutResID;

    public CustomDrawerAdapter(Context context, int layoutResourceID,
                               List<DrawerItem> listItems) {
        super(context, layoutResourceID, listItems);
        this.context = context;
        this.drawerItemList = listItems;
        this.layoutResID = layoutResourceID;

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub

        final DrawerItemHolder drawerHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            drawerHolder = new DrawerItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            drawerHolder.ItemName = (TextView) view .findViewById(R.id.drawer_itemName);

            drawerHolder. countitem  = (TextView) view.findViewById(R.id.textView18);

            drawerHolder.icon = (ImageView) view.findViewById(R.id.drawer_icon);

            drawerHolder.row = (LinearLayout) view.findViewById(R.id.itemLayout);

         /*   drawerHolder.row.setBackgroundColor(Color.TRANSPARENT);
            drawerHolder.row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    drawerHolder.row .setBackgroundColor(Color.parseColor("#f2f2f2"));
                }
            });*/
            view.setTag(drawerHolder);

        } else {
            drawerHolder = (DrawerItemHolder) view.getTag();

        }



            view.setBackgroundColor(Color.TRANSPARENT);

            drawerHolder.icon.setImageDrawable(view.getResources().getDrawable( drawerItemList.get(position).getImgResID()));
            drawerHolder.ItemName.setText( drawerItemList.get(position).getItemName());
            drawerHolder.ItemName.setTextColor(Color.parseColor("#000000"));


    //    drawerHolder.ItemName.setTypeface(Utils.typeface(context));



        return view;
    }

    private static class DrawerItemHolder {
        TextView ItemName,countitem;
        ImageView icon;
        LinearLayout row;
    }
}