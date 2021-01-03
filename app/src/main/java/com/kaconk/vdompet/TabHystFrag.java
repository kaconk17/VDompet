package com.kaconk.vdompet;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kaconk.vdompet.Model.GetHystory;
import com.kaconk.vdompet.Model.GetIn;
import com.kaconk.vdompet.Model.NewDompet;
import com.kaconk.vdompet.Rest.ApiClient;
import com.kaconk.vdompet.Rest.ApiInterface;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabHystFrag extends Fragment {
    private Context context;
    private List<History> allHist;
    private SimpleDateFormat df;
    private String id_dompet;
    private EditText tgl1, tgl2;
    private ImageButton cari_hyst;
    private DatePickerDialog picker;
    private TextView txtallin, txtallout;
    private AdapterHyst adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration divider;
    private ApiInterface mApiinterface;
    private Users currUser;
    private SessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hyst, container, false);
    }
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        this.context = getContext();
        session = new SessionManager(context);
        currUser =  new Users();
        mApiinterface = ApiClient.getClient().create(ApiInterface.class);
        currUser = session.getUserDetails();


        Bundle bund = getArguments();
        tgl1 = view.findViewById(R.id.hyst_date1);
        tgl2 = view.findViewById(R.id.hyst_date2);
        cari_hyst = view.findViewById(R.id.hyst_cari);
        txtallin = view.findViewById(R.id.allin);
        txtallout = view.findViewById(R.id.allout);
        recyclerView = view.findViewById(R.id.list_hyst);
        adapter = new AdapterHyst(context);
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        divider = new DividerItemDecoration(context,linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(divider);

        if (bund !=null){
            if (bund.containsKey("id_dompet")){
                id_dompet = bund.getString("id_dompet");

            }
        }
        df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c =Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH,1);
        allHist = new ArrayList<>();

        /*
        for (int i=0; i<allHist.size(); i++){
            Log.d("History",allHist.get(i).getTgl()+", "+allHist.get(i).getKet()+", "+String.valueOf(allHist.get(i).getJumlah()));

        }
        */
        tgl1.setText(df.format(c.getTime()));
        tgl2.setText(df.format(new Date()));

        tgl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance(TimeZone.getDefault());
                final int day = cldr.get(Calendar.DAY_OF_MONTH);
                final int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfyear, int dayOfMonth) {
                        String bulan, tanggal;
                        monthOfyear +=1;
                        if(monthOfyear < 10){

                            bulan = "0" + monthOfyear;
                        }else {
                            bulan = String.valueOf(monthOfyear);
                        }
                        if(dayOfMonth < 10){

                            tanggal  = "0" + dayOfMonth ;
                        }else{
                            tanggal = String.valueOf(dayOfMonth);
                        }
                        tgl1.setText(year+"-"+bulan+"-"+tanggal);
                    }
                }, year, month,day);
                picker.show();
            }
        });
        tgl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance(TimeZone.getDefault());
                final int day = cldr.get(Calendar.DAY_OF_MONTH);
                final int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfyear, int dayOfMonth) {
                        String bulan, tanggal;
                        monthOfyear +=1;
                        if(monthOfyear < 10){

                            bulan = "0" + monthOfyear;
                        }else {
                            bulan = String.valueOf(monthOfyear);
                        }
                        if(dayOfMonth < 10){

                            tanggal  = "0" + dayOfMonth ;
                        }else{
                            tanggal = String.valueOf(dayOfMonth);
                        }
                        tgl2.setText(year+"-"+bulan+"-"+tanggal);
                    }
                }, year, month,day);
                picker.show();
            }
        });
        cari_hyst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateHist(tgl1.getText().toString(),tgl2.getText().toString(),id_dompet);
            }
        });
        populateHist(df.format(c.getTime()), df.format(new Date()), id_dompet);
    }
    private void populateHist(String dt1, String dt2, String id){
        List<InTrans> allin = new ArrayList<>();
        List<OutTrans> allout = new ArrayList<>();

        allHist.clear();
        Call<GetHystory> allhyst = mApiinterface.getHist(currUser.token,id,dt1,dt2);
        allhyst.enqueue(new Callback<GetHystory>() {
            @Override
            public void onResponse(Call<GetHystory> call, Response<GetHystory> response) {
                if (response.isSuccessful()){
                    allHist = response.body().getListHyst();
                    double totin =0;
                    double totout = 0;
                    for (int i=0; i<allHist.size();i++){
                        if (allHist.get(i).getJenis().equals("IN")){
                            totin = totin + allHist.get(i).getJumlah();
                        }else {
                            totout = totout + allHist.get(i).getJumlah();
                        }

                    }

                    adapter.setListContent(allHist);
                    recyclerView.setAdapter(adapter);
                    txtallin.setText("Total IN : Rp "+ NumberFormat.getInstance().format(totin));
                    txtallout.setText("Total OUT : Rp "+NumberFormat.getInstance().format(totout));
                }
            }

            @Override
            public void onFailure(Call<GetHystory> call, Throwable t) {

            }
        });


    }

}
