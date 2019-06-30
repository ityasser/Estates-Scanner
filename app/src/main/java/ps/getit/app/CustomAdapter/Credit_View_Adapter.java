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
import ps.getit.app.models.points;


//import com.android.volley.toolbox.ImageLoader;
//import com.android.volley.toolbox.NetworkImageView;


public class Credit_View_Adapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {


	LayoutInflater inflater;
	AppCompatActivity mcontext;
	ArrayList<CreditLog> items;

	String Type;
	public Credit_View_Adapter(AppCompatActivity context, ArrayList<CreditLog> items) {
		super();
		mcontext =context;
		this.items = items;
		this.Type=Type;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_view_credit, parent, false);
		return new Credit_View_Adapter.MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


		final Credit_View_Adapter.MyViewHolder myViewHolder = (Credit_View_Adapter.MyViewHolder) holder;
		final CreditLog item = items.get(position);



		myViewHolder.date.setText(item.getCreated_at());

		if(item.getType().equals("1")) {
			myViewHolder.type.setText("إيداع");
			myViewHolder.img_type.setImageResource(R.drawable.up);
		}else {
			myViewHolder.type.setText("سحب");
			myViewHolder.img_type.setImageResource(R.drawable.down);

		}

		if(item.getOp_type().equals("1"))
			myViewHolder.cateri.setText("استبدال");
		else
			myViewHolder.cateri.setText("بطاقة");


		myViewHolder.price.setText(item.getCredit());






		}

	@Override
	public int getItemCount() {
		return items.size();
	}





		public class MyViewHolder extends RecyclerView.ViewHolder {
			TextView date,type,cateri,price;
			ImageView img_type;

			public MyViewHolder(View vi) {
				super(vi);


				date = (TextView) vi.findViewById(R.id.date);
				type = (TextView) vi.findViewById(R.id.type);
				cateri = (TextView) vi.findViewById(R.id.cateri);
				price = (TextView) vi.findViewById(R.id.price);
				img_type = (ImageView) vi.findViewById(R.id.img_type);



			}
		}



}