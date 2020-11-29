package com.kaconk.vdompet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterIn  extends RecyclerView.Adapter<AdapterIn.InHolder>{
    private Context context;
    private List<InTrans> datalist = new ArrayList<>();
    private final LayoutInflater inflater;
    View view;
    InHolder holder;

    public AdapterIn(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public InHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      view = inflater.inflate(R.layout.in_layout, parent, false);
      holder = new InHolder(view);
      return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull InHolder holder, int position) {
        InTrans inTrans = datalist.get(position);

        holder.tgl_in.setText(inTrans.getTgl_in());
        holder.juml_in.setText("Rp "+ NumberFormat.getInstance().format(inTrans.getJumlah()));
        holder.ket_in.setText(inTrans.getKet_in());
    }

    public void setListContent(List<InTrans> list_in){
        this.datalist = list_in;
        notifyItemRangeChanged(0,datalist.size());
    }

    @Override
    public int getItemCount() {
        return (datalist != null) ? datalist.size() : 0;
    }

    class InHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tgl_in, ket_in, juml_in;
        public InHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            tgl_in= itemView.findViewById(R.id.tgl_in);
            ket_in = itemView.findViewById(R.id.ket_in);
            juml_in = itemView.findViewById(R.id.juml_in);
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
