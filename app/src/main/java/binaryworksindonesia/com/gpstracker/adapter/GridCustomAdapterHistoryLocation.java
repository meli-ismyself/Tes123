package binaryworksindonesia.com.gpstracker.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import binaryworksindonesia.com.gpstracker.HistoryLocationActivity;
import binaryworksindonesia.com.gpstracker.HistoryLocationItem;
import binaryworksindonesia.com.gpstracker.MapHistoryLocationActivity;
import binaryworksindonesia.com.gpstracker.R;

/**
 * Created by Meli Oktavia on 5/10/2016.
 */


public class GridCustomAdapterHistoryLocation extends RecyclerView.Adapter<GridCustomAdapterHistoryLocation.ViewHolder>  {
    List<HistoryLocationItem> mItems ;
    private Context context;

    public GridCustomAdapterHistoryLocation(Context context, ArrayList<String> arrayListWaktu, ArrayList<String> arrayListLokasi, ArrayList<String> arrayListOpenMapUri) {
        super();
        this.context = context;
        mItems = new ArrayList<HistoryLocationItem>();
        HistoryLocationItem endangeredItem = new HistoryLocationItem();
        for(int i=0; i<arrayListLokasi.size(); i++)
        {
            endangeredItem = new HistoryLocationItem();
            endangeredItem.setWaktu(arrayListWaktu.get(i));
            endangeredItem.setLokasi(arrayListLokasi.get(i));
            endangeredItem.setMapUri(arrayListOpenMapUri.get(i));
            mItems.add(endangeredItem);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grid_item_list_history_location, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        HistoryLocationItem endangeredItem = mItems.get(position);
        viewHolder.tvWaktu.setText(endangeredItem.getWaktu());
        viewHolder.tvLokasi.setText(endangeredItem.getLokasi());
       // System.out.println("onBindViewHolder++++++++++++++++++++++++++>> "+endangeredItem.getAlamatRestoran());
        //Picasso.with(context).load(endangeredItem.getImgRestoran()).fit().centerCrop().into(viewHolder.ivRestoran);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public Button btnOpenMap;
        public TextView tvWaktu;
        public TextView tvLokasi;

        public ViewHolder(View itemView) {
            super(itemView);
            btnOpenMap = (Button) itemView.findViewById(R.id.btnOpenMap);
            tvWaktu = (TextView)itemView.findViewById(R.id.tvWaktu);
            tvLokasi = (TextView)itemView.findViewById(R.id.tvLokasi);

            btnOpenMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("btnHistoryLocation +++++++++++++++++++ ");

                    HistoryLocationActivity historyLocationActivity = new HistoryLocationActivity();
                    String waktu = historyLocationActivity.getWaktu();
                        System.out.println(" ++++++++++++++++++++++++++ OpenMap ++++++++++++++++++++++++++waktu >> " + waktu);
                        Intent intent = new Intent(context, MapHistoryLocationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                }
            });
        }
    }
}
