package com.king.flixa.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.king.flixa.Adapters.Model.HorizontalModel;
import com.king.flixa.Adapters.Model.VerticalModel;
import com.king.flixa.R;

import java.util.ArrayList;

public class VerticalRecyclerViewAdapter extends RecyclerView.Adapter<VerticalRecyclerViewAdapter.VerticalRVViewHolder> {

    Context context;
    ArrayList<VerticalModel> arrayList;


    public VerticalRecyclerViewAdapter(Context context, ArrayList<VerticalModel> arrayList){
        this.context=context;
        this.arrayList=arrayList;
    }


    @Override
    public VerticalRVViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ver,viewGroup,false);
        return new VerticalRVViewHolder(view);

    }


    @Override
    public void onBindViewHolder(VerticalRVViewHolder verticalRVViewHolder, int i) {
        VerticalModel verticalModel=arrayList.get(i);
        String title=verticalModel.getTitle();
        ArrayList<HorizontalModel> singleitem=verticalModel.getArrayList();

        verticalRVViewHolder.textViewTitle.setText(title);
        HorizontalRecycleViewAdapter horizontalRecycleViewAdapter=new HorizontalRecycleViewAdapter(context,singleitem);
        verticalRVViewHolder.recyclerView.setHasFixedSize(true);
        verticalRVViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));

        verticalRVViewHolder.recyclerView.setAdapter(horizontalRecycleViewAdapter);

        verticalRVViewHolder.viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(view.getContext(),HotstarActivity.class);
                intent.putExtra("apiUri", verticalModel.getApiUri());
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public  class VerticalRVViewHolder extends RecyclerView.ViewHolder
    {

        RecyclerView recyclerView;
        TextView textViewTitle;
        CardView viewMore;
        public VerticalRVViewHolder(View itemView) {
            super(itemView);
            recyclerView=(RecyclerView)itemView.findViewById(R.id.recyclerview1);
            textViewTitle=(TextView)itemView.findViewById(R.id.textTitle1);
            viewMore=(CardView) itemView.findViewById(R.id.viewMore);
        }
    }
}