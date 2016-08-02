package binaryworksindonesia.com.gpstracker;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import binaryworksindonesia.com.gpstracker.adapter.GridCustomAdapterHistoryLocation;
import binaryworksindonesia.com.gpstracker.rvlistener.RecyclerItemClickListener;

public class HistoryLocationActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    ArrayList<String> listWaktu = new ArrayList<String>();
    ArrayList<String> listLokasi = new ArrayList<String>();
    ArrayList<String> listOpenMapUri = new ArrayList<String>();
    String hajjName;
    Context context;
    String waktu, lokasi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_location);
        SetUpRecyclerView();
        hajjName = getIntent().getStringExtra("hajjNameIntent");
        System.out.println("HistoryLocationActivity hajjName " + hajjName);

        getSupportActionBar().setTitle(hajjName);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case android.R.id.home:
                System.out.println("back");
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void SetUpRecyclerView(){
        AddListHardCode();
        // Calling the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_history);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // add list into adapter
        mAdapter = new GridCustomAdapterHistoryLocation(getApplicationContext(), listWaktu, listLokasi, listOpenMapUri);
        mRecyclerView.setAdapter(mAdapter);

        //Listener
        //RecyclerView Listener
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener(){
                    @Override
                    public void onItemClick(View view, int position) {
                        System.out.println("RecyclerView Listener " + listWaktu.get(position));
                        setWaktu(listWaktu.get(position));


                    }
                })
        );
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
        System.out.println("setWaktu ===>> " +waktu);
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    private void AddListHardCode(){
        for (int i=0; i<10; i++){
            listWaktu.add("2016-06-21 15:33:58 " + i);
            listLokasi.add("188 Salemba Tengah Gg. VIII, Daerah Khusus Ibukota Jakarta, ID " + i);
            listOpenMapUri.add("Uri ke ; " + i);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
