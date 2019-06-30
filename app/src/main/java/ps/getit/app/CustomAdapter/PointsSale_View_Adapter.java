package ps.getit.app.CustomAdapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ps.getit.app.App;
import ps.getit.app.Dialogs.ProgressDialog;
import ps.getit.app.R;
import ps.getit.app.models.Prizes;
import ps.getit.app.models.points;
import ps.getit.app.utils.Utility;


//import com.android.volley.toolbox.ImageLoader;
//import com.android.volley.toolbox.NetworkImageView;


public class PointsSale_View_Adapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {


	LayoutInflater inflater;
	AppCompatActivity mcontext;
	ArrayList<points> items;

	String Type;
	public PointsSale_View_Adapter(AppCompatActivity context, ArrayList<points> items) {
		super();
		mcontext =context;
		this.items = items;
		this.Type=Type;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_pointe_sale, parent, false);
		return new PointsSale_View_Adapter.MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


		final PointsSale_View_Adapter.MyViewHolder myViewHolder = (PointsSale_View_Adapter.MyViewHolder) holder;
		final points item = items.get(position);


		myViewHolder.price.setText(item.getAddress());

		myViewHolder.pints.setText(item.getName());




		}

	@Override
	public int getItemCount() {
		return items.size();
	}





		public class MyViewHolder extends RecyclerView.ViewHolder {
			TextView pints,price;
			TextView replace_lay;

			public MyViewHolder(View vi) {
				super(vi);


				pints = (TextView) vi.findViewById(R.id.point);
				price = (TextView) vi.findViewById(R.id.price);

				replace_lay = (TextView) vi.findViewById(R.id.exchnge);


			}
		}



}