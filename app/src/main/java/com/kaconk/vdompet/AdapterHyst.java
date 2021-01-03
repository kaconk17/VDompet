package com.kaconk.vdompet;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdapterHyst extends RecyclerView.Adapter<AdapterHyst.HystHolder> {
    private Context context;
    private final LayoutInflater inflater;
    private List<History> datalist = new ArrayList<>();
    private SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
    HystHolder holder;
    View view;

    public AdapterHyst(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HystHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.in_layout, parent, false);
        holder = new HystHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HystHolder holder, int position) {
        History history = datalist.get(position);
        try{
            Date date = dateFormat.parse(history.getTgl());
            holder.tgl_hyst.setText(dateFormat.format(date));
        }catch (ParseException e){
            Log.d("error : ",e.toString());
        }
        if (history.getJenis().equals("IN")){

            holder.jumlah.setText("+ Rp "+ NumberFormat.getInstance().format(history.getJumlah()));
            holder.jumlah.setTextColor(Color.GREEN);

        }else {

            holder.jumlah.setText("- Rp "+ NumberFormat.getInstance().format(history.getJumlah()));
            holder.jumlah.setTextColor(Color.RED);

        }
        holder.ket.setText(history.getKet());
    }
    public void setListContent(List<History> list_hyst){
        this.datalist = list_hyst;
        notifyItemRangeChanged(0,datalist.size());
    }

    @Override
    public int getItemCount() {
        return (datalist != null) ? datalist.size() : 0;
    }

    class HystHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tgl_hyst, ket, jumlah;
        public HystHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            tgl_hyst = itemView.findViewById(R.id.tgl_in);
            ket = itemView.findViewById(R.id.ket_in);
            jumlah = itemView.findViewById(R.id.juml_in);
        }
        @Override
        public void onClick(View v) {

        }
    }
    public void removeAt(int position){
        datalist.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, datalist.size());
    }
}
