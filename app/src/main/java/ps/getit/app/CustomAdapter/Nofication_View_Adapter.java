package ps.getit.app.CustomAdapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ps.getit.app.App;
import ps.getit.app.R;
import ps.getit.app.models.Notifications;
import ps.getit.app.utils.Utility;


//import com.android.volley.toolbox.ImageLoader;
//import com.android.volley.toolbox.NetworkImageView;


public class Nofication_View_Adapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {


	LayoutInflater inflater;
Context mcontext;
	ArrayList<Notifications> items;

	Notifications item;
	String Type;
	public Nofication_View_Adapter(Activity context, ArrayList<Notifications> items) {
		super();
		mcontext =context;
		this.items = items;
		this.Type=Type;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_notfication, parent, false);
		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


		final MyViewHolder myViewHolder = (MyViewHolder) holder;
		final Notifications item = items.get(position);






		myViewHolder.imageView.setImageResource(R.drawable.profile);
		Glide.with(App.getInstance())
				.load(Utility.SITE_ROOT + "uploads/admin/" +  item.getLogo())
				.error(R.drawable.logo)
				.diskCacheStrategy(DiskCacheStrategy.ALL)

				.into(myViewHolder.imageView);

		myViewHolder.namec.setText(item.getTitle());

		myViewHolder.msg.setText(item.getDetails());

		myViewHolder.time.setText(item.getCreated_at());



		}

	@Override
	public int getItemCount() {
		return items.size();
	}




		public class MyViewHolder extends RecyclerView.ViewHolder {
			CircleImageView imageView;
			TextView namec,msg,time;

			public MyViewHolder(View vi) {
				super(vi);
				 imageView = (CircleImageView) vi.findViewById(R.id.imageprff);

				 namec = (TextView) vi.findViewById(R.id.namec);
				 msg = (TextView) vi.findViewById(R.id.msg);


				 time = (TextView) vi.findViewById(R.id.time);

			}
		}



}