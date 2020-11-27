package com.kaconk.vdompet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterDompet extends RecyclerView.Adapter<AdapterDompet.MyViewHolder>{
    private Context context;
    private List<Dompet> datalist = new ArrayList<>();
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;

    public AdapterDompet(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view=inflater.inflate(R.layout.dompet_layout,parent,false);
        holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Dompet listDompet = datalist.get(position);
        String saldo =String.valueOf(listDompet.getSaldo());
        holder.tnama.setText(listDompet.getNama_dompet());
        holder.tsaldo.setText("Rp "+saldo);
    }

    public void setListContent(List<Dompet> list_dompet){
        this.datalist = list_dompet;
        notifyItemRangeChanged(0,datalist.size());
    }

    @Override
    public int getItemCount() {
        return (datalist != null) ? datalist.size() : 0;
    }
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tnama, tsaldo;
        public MyViewHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            tnama = itemView.findViewById(R.id.lstNamaDompet);
            tsaldo = itemView.findViewById(R.id.lstSaldo);
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
