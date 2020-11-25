package com.kaconk.vdompet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DompetAdapter extends RecyclerView.Adapter<DompetAdapter.ViewHolder> {
    private Context context;
    private List<Dompet> datalist;
    private OnListListener mOnlistlistener;
    private int selectedItem;

    public DompetAdapter(Context context, List<Dompet> dompetlist, OnListListener onlist){
        this.context = context;
        this.datalist = dompetlist;
        this.mOnlistlistener = onlist;
        selectedItem = -1;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.dompet_layout, parent, false);

        return new ViewHolder(v, mOnlistlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dompet dompet = datalist.get(position);
        String saldo =String.valueOf(dompet.getSaldo());
        holder.tnama.setText(dompet.getNama_dompet());
        holder.tsaldo.setText("Rp "+saldo);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tnama, tsaldo;
        OnListListener onListListener;
        public ViewHolder(@NonNull View itemView, OnListListener onListListener){
            super(itemView);
            tnama = itemView.findViewById(R.id.lstNamaDompet);
            tsaldo = itemView.findViewById(R.id.lstSaldo);
            this.onListListener = onListListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            onListListener.onListClick(getAdapterPosition());
            int prev = selectedItem;
            selectedItem = getAdapterPosition();
            notifyItemChanged(prev);
            notifyItemChanged(selectedItem);
        }
    }
    public interface OnListListener{
        void onListClick(int position);
    }

    @Override
    public int getItemCount() {
        return (datalist != null) ? datalist.size() : 0;
    }
}
