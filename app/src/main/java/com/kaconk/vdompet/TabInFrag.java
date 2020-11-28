package com.kaconk.vdompet;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TabInFrag extends Fragment {
    private Context context;
    private EditText tgl1, tgl2;
    private TextView txttotal;
    private DatePickerDialog picker;
    private SimpleDateFormat dateFormat;
    private View dialogview;
    private EditText txttgl_in, txtjmlh_in, txtket_in;
    private String id_dompet;
    private FloatingActionButton fab;
    private Dompet curdompet;
    private DBHelper dbHelper;
    private RecyclerView recyclerView;
    private AdapterIn adapterIn;
    private List<InTrans> inList;
    private LinearLayoutManager linearLayoutManager;
    private EventListener listener;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        if(activity instanceof EventListener) {
            listener = (EventListener)activity;
        } else {
            // Throw an error!
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_in, container, false);

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        this.context = getContext();
        dbHelper = new DBHelper(context);
        Bundle bund = getArguments();
        if (bund !=null){
            if (bund.containsKey("id_dompet")){
                id_dompet = bund.getString("id_dompet");
                curdompet = new Dompet();
                curdompet = dbHelper.getDompet(id_dompet);
                dbHelper.closeDB();
            }
        }
        tgl1 = view.findViewById(R.id.in_date1);
        tgl2 = view.findViewById(R.id.in_date2);
        fab = view.findViewById(R.id.fab_in);
        txttotal = view.findViewById(R.id.total_in);
        recyclerView = view.findViewById(R.id.list_in);
        adapterIn = new AdapterIn(context);
        inList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH,1);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        populateIn(dateFormat.format(new Date()),dateFormat.format(c.getTime()),curdompet);

        tgl2.setText(dateFormat.format(new Date()));
        tgl1.setText(dateFormat.format(c.getTime()));
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForm();
            }
        });
    }

    private void populateIn(String dt1, String dt2, Dompet domp){
        inList.clear();
        inList = dbHelper.getInTrans(dt1,dt2,domp);
        dbHelper.closeDB();
        updatetotal();
        adapterIn.setListContent(inList);
        recyclerView.setAdapter(adapterIn);
    }
    private void updatetotal(){
        double t_in = 0;
        for (int i =0; i < inList.size(); i++){
            Log.d("IN :", inList.get(i).id_in+", "+inList.get(i).getJumlah());
            t_in = t_in + inList.get(i).getJumlah();
        }
        txttotal.setText("Total : Rp "+String.valueOf(t_in));
        listener.getDompetdata(curdompet.getId_dompet());
    }

    private void dialogForm(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        dialogview = inflater.inflate(R.layout.add_in, null);
        dialog.setView(dialogview);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.plus_icon);
        dialog.setTitle("Tambah Saldo");

        txttgl_in = dialogview.findViewById(R.id.tgl_in);
        txtjmlh_in = dialogview.findViewById(R.id.jumlah_in);
        txtket_in = dialogview.findViewById(R.id.ket_in);
        txttgl_in.setText(dateFormat.format(new Date()));
        txtjmlh_in.setText(null);
        txtket_in.setText(null);
        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String juml = txtjmlh_in.getText().toString();
                if (!juml.isEmpty()){
                    InTrans in = new InTrans();
                    double tambah = 0;
                    try{
                        tambah = Double.parseDouble(juml);
                        in.setTgl_in(txttgl_in.getText().toString());
                        in.setJumlah(tambah);
                        in.setKet_in(txtket_in.getText().toString());
                        String id = dbHelper.createIn(in,curdompet.getId_dompet());
                        dbHelper.closeDB();
                        inList.add(dbHelper.getIn(id));
                        dbHelper.closeDB();
                        curdompet.setSaldo(curdompet.getSaldo()+tambah);
                        dbHelper.updateDompet(curdompet);
                        adapterIn.notifyDataSetChanged();
                        updatetotal();
                        dialog.dismiss();
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                    }


                }

            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}
