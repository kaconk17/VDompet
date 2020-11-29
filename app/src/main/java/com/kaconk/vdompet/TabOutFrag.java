package com.kaconk.vdompet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TabOutFrag extends Fragment {
    private Context context;
    private EventListener listener;
    private List<OutTrans> outList;
    private DBHelper dbHelper;
    private String id_dompet;
    private Dompet curdompet;
    private EditText tgl1, tgl2;
    private TextView txttotal;
    private AdapterOut adapterOut;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration divider;
    private SimpleDateFormat dateFormat;
    private DatePickerDialog picker;
    private FloatingActionButton fab;
    private ImageButton cari_out;
    private EditText txttgl_out, txtjmlh_out, txtket_out;
    Alertdialog alert = new Alertdialog();
    private View dialogview;

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
        return inflater.inflate(R.layout.fragment_out, container, false);
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
        tgl1 = view.findViewById(R.id.out_date1);
        tgl2 = view.findViewById(R.id.out_date2);
        txttotal = view.findViewById(R.id.total_out);
        fab = view.findViewById(R.id.fab_out);
        cari_out = view.findViewById(R.id.out_cari);
        adapterOut = new AdapterOut(context);
        recyclerView = view.findViewById(R.id.list_out);
        outList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        divider = new DividerItemDecoration(context,linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(divider);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH,1);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, final int position) {
                PopupMenu popup = new PopupMenu(context,view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.popup_dompet, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.deleteDompet:
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:
                                                updatetotal();
                                                double jm = outList.get(position).getJumlah();
                                                double awal = curdompet.getSaldo();

                                                    dbHelper.deleteOut(outList.get(position).getId_out());
                                                    dbHelper.closeDB();
                                                    curdompet.setSaldo(awal+jm);
                                                    dbHelper.updateDompet(curdompet);
                                                    dbHelper.closeDB();
                                                    adapterOut.removeAt(position);
                                                    updatetotal();

                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                break;
                                        }
                                    }
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Delete").setMessage("Apakah anda akan menghapus "+String.valueOf(outList.get(position).getJumlah())+" ?").setPositiveButton("YES", dialogClickListener).setNegativeButton("NO", dialogClickListener).show();
                                return true;
                            case R.id.editDompet:
                                editOut(outList.get(position));

                                return true;

                            default:
                                return false;
                        }

                    }
                });
                popup.show();
            }
        }));
        populateOut(dateFormat.format(c.getTime()),dateFormat.format(new Date()),curdompet);
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
        cari_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateOut(tgl1.getText().toString(),tgl2.getText().toString(),curdompet);
            }
        });
    }

    private void populateOut(String dt1, String dt2, Dompet domp){
        outList.clear();
       outList = dbHelper.getOutTrans(dt1,dt2,domp);
        dbHelper.closeDB();
        updatetotal();
       adapterOut.setListContent(outList);
        recyclerView.setAdapter(adapterOut);
    }
    private void updatetotal(){
        double t_in = 0;
        for (int i =0; i < outList.size(); i++){
            t_in = t_in + outList.get(i).getJumlah();
        }
        curdompet = dbHelper.getDompet(id_dompet);
        dbHelper.closeDB();
        txttotal.setText("Total : Rp "+ NumberFormat.getInstance().format(t_in));
        listener.getDompetdata(curdompet.getId_dompet());
    }
    private void dialogForm(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        dialogview = inflater.inflate(R.layout.add_in, null);
        dialog.setView(dialogview);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.plus_icon);
        dialog.setTitle("Tambah Pengeluaran");

        txttgl_out = dialogview.findViewById(R.id.tgl_in);
        txtjmlh_out = dialogview.findViewById(R.id.jumlah_in);
        txtket_out = dialogview.findViewById(R.id.ket_in);
        txttgl_out.setText(dateFormat.format(new Date()));
        txtjmlh_out.setText(null);
        txtket_out.setText(null);
        txttgl_out.setOnClickListener(new View.OnClickListener() {
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
                        txttgl_out.setText(year+"-"+bulan+"-"+tanggal);
                    }
                }, year, month,day);
                picker.show();
            }
        });
        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String juml = txtjmlh_out.getText().toString();
                if (!juml.isEmpty()){
                    OutTrans out = new OutTrans();
                    double tambah = 0;
                    try{
                        updatetotal();
                        tambah = Double.parseDouble(juml);
                        out.setTgl_out(txttgl_out.getText().toString());
                        out.setJumlah(tambah);
                        out.setKet_out(txtket_out.getText().toString());
                        if (curdompet.getSaldo()-tambah <0){
                            dialog.dismiss();
                            alert.showAlertDialog(context,"Transaction","Saldo tidak mencukupi !",false);
                        }else {
                            String id = dbHelper.createOut(out,curdompet.getId_dompet());
                            dbHelper.closeDB();
                            outList.add(dbHelper.getOut(id));
                            dbHelper.closeDB();
                            curdompet.setSaldo(curdompet.getSaldo()-tambah);
                            dbHelper.updateDompet(curdompet);
                            dbHelper.closeDB();
                            adapterOut.notifyDataSetChanged();
                            updatetotal();
                            dialog.dismiss();
                        }

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
    public void editOut(final OutTrans curout){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        dialogview = inflater.inflate(R.layout.add_in, null);
        dialog.setView(dialogview);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.plus_icon);
        dialog.setTitle("Edit");

        txttgl_out = dialogview.findViewById(R.id.tgl_in);
        txtjmlh_out = dialogview.findViewById(R.id.jumlah_in);
        txtket_out = dialogview.findViewById(R.id.ket_in);
        txttgl_out.setText(curout.getTgl_out());
        txtjmlh_out.setText(String.valueOf(curout.getJumlah()));
        txtket_out.setText(curout.getKet_out());
        txttgl_out.setOnClickListener(new View.OnClickListener() {
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
                        txttgl_out.setText(year+"-"+bulan+"-"+tanggal);
                    }
                }, year, month,day);
                picker.show();
            }
        });
        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String juml = txtjmlh_out.getText().toString();
                if (!juml.isEmpty()){
                    double tambah = 0;
                    try{
                        updatetotal();
                        tambah = Double.parseDouble(juml);
                        double awal = curout.getJumlah();
                        double saldo = curdompet.getSaldo();
                        if ((saldo+awal)- tambah < 0){
                            dialog.dismiss();
                            alert.showAlertDialog(context,"Transaction","Saldo tidak mencukupi !",false);

                        }else {
                            curout.setTgl_out(txttgl_out.getText().toString());
                            curout.setJumlah(tambah);
                            curout.setKet_out(txtket_out.getText().toString());
                            dbHelper.updateOut(curout);
                            dbHelper.closeDB();
                            curdompet.setSaldo((saldo+awal)-tambah);
                            dbHelper.updateDompet(curdompet);
                            dbHelper.closeDB();

                            populateOut(tgl1.getText().toString(),tgl2.getText().toString(),curdompet);

                            dialog.dismiss();
                        }

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
