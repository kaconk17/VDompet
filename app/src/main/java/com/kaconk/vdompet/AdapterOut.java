package com.kaconk.vdompet;

import android.content.Context;
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

public class AdapterOut extends RecyclerView.Adapter<AdapterOut.OutHolder>{
    private Context context;
    private List<OutTrans> datalist = new ArrayList<>();
    private final LayoutInflater inflater;
    private SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
    OutHolder holder;
    View view;

    public AdapterOut(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public OutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.in_layout, parent, false);
        holder = new OutHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OutHolder holder, int position) {
            OutTrans outTrans = datalist.get(position);
        try{
            Date date = dateFormat.parse(outTrans.getTgl_out());
            holder.tgl_out.setText(dateFormat.format(date));
        }catch (ParseException e){
            Log.d("error : ",e.toString());
        }

        holder.juml_out.setText("Rp "+ NumberFormat.getInstance().format(outTrans.getJumlah()));
        holder.ket_out.setText(outTrans.getKet_out());
    }
    public void setListContent(List<OutTrans> list_out){
        this.datalist = list_out;
        notifyItemRangeChanged(0,datalist.size());
    }
    @Override
    public int getItemCount() {
        return (datalist != null) ? datalist.size() : 0;
    }

    class OutHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tgl_out, ket_out, juml_out;
        public OutHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            tgl_out = itemView.findViewById(R.id.tgl_in);
            ket_out = itemView.findViewById(R.id.ket_in);
            juml_out = itemView.findViewById(R.id.juml_in);
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
