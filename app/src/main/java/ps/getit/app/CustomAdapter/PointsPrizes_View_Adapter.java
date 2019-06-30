package ps.getit.app.CustomAdapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import ps.getit.app.utils.Utility;


//import com.android.volley.toolbox.ImageLoader;
//import com.android.volley.toolbox.NetworkImageView;


public class PointsPrizes_View_Adapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {


	LayoutInflater inflater;
	AppCompatActivity mcontext;
	ArrayList<Prizes> items;

	String Type;
	public PointsPrizes_View_Adapter(AppCompatActivity context, ArrayList<Prizes> items) {
		super();
		mcontext =context;
		this.items = items;
		this.Type=Type;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_pointe_exchange, parent, false);
		return new PointsPrizes_View_Adapter.MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


		final PointsPrizes_View_Adapter.MyViewHolder myViewHolder = (PointsPrizes_View_Adapter.MyViewHolder) holder;
		final Prizes item = items.get(position);


		myViewHolder.price.setText(item.getPrize());

		myViewHolder.pints.setText(item.getPoints_num());
		myViewHolder.replace_lay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				new SweetAlertDialog(mcontext, SweetAlertDialog.WARNING_TYPE)
						.setCancelText(mcontext.getString(R.string.cancel))
						.setConfirmText(mcontext.getString(R.string.confirm))
						.setContentText(mcontext.getString(R.string.confirm_replace_msg))
						.setTitleText("")

						.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {

								//  Toast.makeText(getApplication() , "delete"+position , Toast.LENGTH_SHORT).show();
								sweetAlertDialog.dismissWithAnimation();

								replace_points(item.getId(),mcontext);


							}
						}).show();


			}
		});



		}

	@Override
	public int getItemCount() {
		return items.size();
	}



	public void replace_points(final String order_id, final AppCompatActivity mcontext){
		if (Utility.isConnectingToInternet(mcontext)) {

			final ProgressDialog shwo=new ProgressDialog();
			shwo.show(mcontext.getSupportFragmentManager(),"dfdf");

			StringRequest request = new StringRequest(Request.Method.POST, Utility.API_ROOT + "replace_points", new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {

						try {
							JSONObject obj=new JSONObject(response);

							Utility.showSuccessDialog(mcontext,obj.getString("message"));


						} catch (JSONException e) {
							e.printStackTrace();
						}



					shwo.dismiss();
					Log.i("getResturants", response);

				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					shwo.dismiss();
					error.printStackTrace();
					Utility.showToast(R.string.connection_error, false);
				}
			}) {

				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					Map<String, String> params = new HashMap<>();
					params.put("token", Utility.getStringFromPref(mcontext, "token"));
					params.put("prize_id",order_id );
					params.put("lang",Utility.getLangDefult(mcontext));

					return params;

				}
			};

			App.getInstance().addToRequestQueue(request);
		} else {

			Utility.showErrorDialog(mcontext, mcontext.getString(R.string.no_internet));

		}

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