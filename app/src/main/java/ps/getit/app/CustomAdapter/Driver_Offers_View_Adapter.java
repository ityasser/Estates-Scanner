package ps.getit.app.CustomAdapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ps.getit.app.App;
import ps.getit.app.R;
import ps.getit.app.activities.MainActivity;
import ps.getit.app.models.OffersDelivery;
import ps.getit.app.models.Prizes;
import ps.getit.app.utils.Utility;


//import com.android.volley.toolbox.ImageLoader;
//import com.android.volley.toolbox.NetworkImageView;


public class Driver_Offers_View_Adapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {


	LayoutInflater inflater;
	AppCompatActivity mcontext;
	ArrayList<OffersDelivery> items;

	public static String IdOfferSelcted="0";

	String Type;
	public Driver_Offers_View_Adapter(AppCompatActivity context, ArrayList<OffersDelivery> items) {
		super();
		mcontext =context;
		this.items = items;
		this.Type=Type;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_driver_responses, parent, false);
		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


		final MyViewHolder myViewHolder = (MyViewHolder) holder;
		final OffersDelivery item = items.get(position);






		myViewHolder.name_driver.setText(item.getDelivery_name());

		if(item.getId().equals(IdOfferSelcted)) {
			myViewHolder.is_seclected.setChecked(true);
			myViewHolder.itemView.setBackgroundResource(R.color.backFraem);
		}else {
			myViewHolder.is_seclected.setChecked(false);
			myViewHolder.itemView.setBackgroundResource(R.color.white);
		}


		if(item.getTime()!= null) {
			myViewHolder.time.setText(Utility.formatHoursAndMinutes(Integer.parseInt(item.getTime())));
		}else {
			myViewHolder.time.setText("");
		}

		myViewHolder.price.setText(item.getPrice()+" "+Utility.getStringFromPref(mcontext,"currency","₪"));
		myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				IdOfferSelcted=item.getId();
				notifyDataSetChanged();

			}
		});

		myViewHolder.is_seclected.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				IdOfferSelcted=item.getId();
				notifyDataSetChanged();

			}
		});

		}

	@Override
	public int getItemCount() {
		return items.size();
	}





		public class MyViewHolder extends RecyclerView.ViewHolder {
			TextView time,name_driver,price;
			CheckBox is_seclected;

			public MyViewHolder(View vi) {
				super(vi);


				name_driver = (TextView) vi.findViewById(R.id.name_driver);
				time = (TextView) vi.findViewById(R.id.time);

				price = (TextView) vi.findViewById(R.id.price);
				is_seclected = (CheckBox) vi.findViewById(R.id.is_seclected);


			}
		}



}