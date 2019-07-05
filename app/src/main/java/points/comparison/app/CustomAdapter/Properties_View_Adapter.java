package points.comparison.app.CustomAdapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import points.comparison.app.R;
import points.comparison.app.models.Properties;


//import com.android.volley.toolbox.ImageLoader;
//import com.android.volley.toolbox.NetworkImageView;


public class Properties_View_Adapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {


	LayoutInflater inflater;
	AppCompatActivity mcontext;
	ArrayList<Properties> items;

	public static String IdOfferSelcted="0";

	String Type;
	public Properties_View_Adapter(AppCompatActivity context, ArrayList<Properties> items) {
		super();
		mcontext =context;
		this.items = items;
		this.Type=Type;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_properties, parent, false);
		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


		final MyViewHolder myViewHolder = (MyViewHolder) holder;
	;




		if(items.get(position).getType().equals("0")){
			myViewHolder.namec_pro.setVisibility(View.VISIBLE);
			myViewHolder.namec_pro.setText(items.get(position).getName());
			myViewHolder.is_seclected.setVisibility(View.GONE);
		}else {
			myViewHolder.namec_pro.setVisibility(View.GONE);
			myViewHolder.is_seclected.setVisibility(View.VISIBLE);


		}


		myViewHolder.is_seclected.setText(items.get(position).getName());

			myViewHolder.is_seclected.setChecked(items.get(position).isSelected());


		Log.d("onCheckedChanged","myViewHolder  "+position +"  "+items.get(position).getName() + "  "+items.get(position).isSelected());


		myViewHolder.is_seclected.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				items.get(position).setSelected(myViewHolder.is_seclected.isChecked());
				Log.d("onCheckedChanged",position +"  "+items.get(position).getName() + "  "+myViewHolder.is_seclected.isChecked());

			}
		});






		}

	@Override
	public int getItemCount() {
		return items.size();
	}





		public class MyViewHolder extends RecyclerView.ViewHolder {
			CheckBox is_seclected;

			TextView namec_pro;
			public MyViewHolder(View vi) {
				super(vi);

				namec_pro = (TextView) vi.findViewById(R.id.namec_pro);

				is_seclected = (CheckBox) vi.findViewById(R.id.is_seclected);


			}
		}



}