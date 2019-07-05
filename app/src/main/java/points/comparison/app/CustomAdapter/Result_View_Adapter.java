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
import points.comparison.app.models.Result;


//import com.android.volley.toolbox.ImageLoader;
//import com.android.volley.toolbox.NetworkImageView;


public class Result_View_Adapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {


	private LayoutInflater inflater;
	private	AppCompatActivity mcontext;
	private ArrayList<Properties> items;
	private ArrayList<Result> listReslut;


	private String region1="",region2="";

	public Result_View_Adapter(AppCompatActivity context, ArrayList<Properties> items,String region1,String region2,ArrayList<Result> listReslut
	) {
		super();
		this.mcontext =context;
		this.items = items;
		this.region1=region1;
		this.region2=region2;

		this.listReslut=listReslut;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_result, parent, false);
		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


		final MyViewHolder myViewHolder = (MyViewHolder) holder;
		Properties item = items.get(position);




		if(item.getType().equals("0")){
			myViewHolder.namec_pro.setVisibility(View.VISIBLE);
			myViewHolder.namec_pro.setText(item.getName());
			myViewHolder.Criteria.setVisibility(View.GONE);
			myViewHolder.scor1.setVisibility(View.GONE);
			myViewHolder.scor2.setVisibility(View.GONE);

		}else {
			myViewHolder.namec_pro.setVisibility(View.GONE);
			myViewHolder.Criteria.setVisibility(View.VISIBLE);
			myViewHolder.scor1.setVisibility(View.VISIBLE);
			myViewHolder.scor2.setVisibility(View.VISIBLE);
		}


		myViewHolder.Criteria.setText(item.getName());

		myViewHolder.scor1.setText(getScor(item.getName(),region1)+"%");

		myViewHolder.scor2.setText(getScor(item.getName(),region2)+"%");



		Log.d("ssssssssssssss",items.size()+"");







		}

	@Override
	public int getItemCount() {
		return items.size();
	}


		public class MyViewHolder extends RecyclerView.ViewHolder {

			TextView namec_pro,Criteria,scor1,scor2;
			public MyViewHolder(View vi) {
				super(vi);

				namec_pro = (TextView) vi.findViewById(R.id.namec_pro);
				Criteria = (TextView) vi.findViewById(R.id.Criteria);
				scor1 = (TextView) vi.findViewById(R.id.scor1);
				scor2 = (TextView) vi.findViewById(R.id.scor2);



			}
		}



		public String getScor(String Criteria,String region){

			String result="0";
			for (int i = 0; i < listReslut.size(); i++) {

				if(listReslut.get(i).getRegion().equals(region) && listReslut.get(i).getName().equals(Criteria)){

					result= listReslut.get(i).getScore();

					break;
				}
			}

			return result;
		}

}