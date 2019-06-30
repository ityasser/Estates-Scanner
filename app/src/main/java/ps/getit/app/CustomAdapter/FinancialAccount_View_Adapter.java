package ps.getit.app.CustomAdapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ps.getit.app.R;
import ps.getit.app.models.CreditLog;
import ps.getit.app.models.FinancialAccount;


//import com.android.volley.toolbox.ImageLoader;
//import com.android.volley.toolbox.NetworkImageView;


public class FinancialAccount_View_Adapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {



	LayoutInflater inflater;
	AppCompatActivity mcontext;
	ArrayList<FinancialAccount> items;

	String Type,is_admin;
	public FinancialAccount_View_Adapter(AppCompatActivity context, ArrayList<FinancialAccount> items,String is_admin) {
		super();
		mcontext =context;
		this.items = items;
		this.Type=Type;
		this.is_admin=is_admin;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_financial_account, parent, false);
		return new FinancialAccount_View_Adapter.MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


		final FinancialAccount_View_Adapter.MyViewHolder myViewHolder = (FinancialAccount_View_Adapter.MyViewHolder) holder;
		final FinancialAccount item = items.get(position);



		myViewHolder.name.setText(item.getName());

		if(item.getPayment_type().equals("1")) {
			myViewHolder.type.setText("محفظة");
		}else {
			myViewHolder.type.setText("نقداً");

		}


		if(is_admin.equals("0")){
			myViewHolder.on_you.setVisibility(View.GONE);

		}else {
			myViewHolder.on_you.setVisibility(View.VISIBLE);
			myViewHolder.on_you.setText(item.getOn_you());

		}

		myViewHolder.order_id.setText(item.getOrder_id());
		myViewHolder.paid.setText(item.getPaid());






		}

	@Override
	public int getItemCount() {
		return items.size();
	}





		public class MyViewHolder extends RecyclerView.ViewHolder {
			TextView name,type,order_id,paid,on_you;

			public MyViewHolder(View vi) {
				super(vi);

				on_you= (TextView) vi.findViewById(R.id.on_you);
				name = (TextView) vi.findViewById(R.id.name);
				type = (TextView) vi.findViewById(R.id.type);
				order_id = (TextView) vi.findViewById(R.id.order_id);
				paid = (TextView) vi.findViewById(R.id.paid);



			}
		}



}