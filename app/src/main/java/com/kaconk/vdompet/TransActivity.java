package com.kaconk.vdompet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class TransActivity extends AppCompatActivity {
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Dompet curdomp;
    private DBHelper dbHelper;
    private CircleImageView seting;
    private View dialogview;
    private EditText txt_dompet;
    private TextView NamaDompet, SaldoDompet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        //seting = findViewById(R.id.);
        NamaDompet = findViewById(R.id.namedompet);
        SaldoDompet = findViewById(R.id.statSaldo);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabInFrag(),"IN");
        adapter.addFragment(new TabOutFrag(),"OUT");
        adapter.addFragment(new TabHystFrag(),"History");
        curdomp = new Dompet();
        String id_domp = (String) getIntent().getSerializableExtra("id_domp");
        dbHelper = new DBHelper(TransActivity.this);
        curdomp = dbHelper.getDompet(id_domp);
        dbHelper.closeDB();
        SaldoDompet.setText("Rp "+String.valueOf(curdomp.getSaldo()));
        NamaDompet.setText(curdomp.getNama_dompet());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
/*
        seting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(TransActivity.this, seting);
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
                                                dbHelper.deleteDompet(curdomp.getId_dompet());
                                                dbHelper.closeDB();
                                                finish();
                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                break;
                                        }
                                    }
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(TransActivity.this);
                                builder.setTitle("Delete").setMessage("Apakah anda akan menghapus "+curdomp.getNama_dompet()+" ?").setPositiveButton("YES", dialogClickListener).setNegativeButton("NO", dialogClickListener).show();
                                return true;
                            case R.id.editDompet:
                                dialogForm();
                                return true;

                            default:
                                return false;
                        }

                    }
                });
                popup.show();
            }
        });
*/
    }
    private void dialogForm(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(TransActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        dialogview = inflater.inflate(R.layout.add_dompet, null);
        dialog.setView(dialogview);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.plus_icon);
        dialog.setTitle("Edit Dompet");

        txt_dompet = dialogview.findViewById(R.id.nama_dompet);
        txt_dompet.setText(curdomp.getNama_dompet());

        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                curdomp.setNama_dompet(txt_dompet.getText().toString());
                dbHelper.updateDompet(curdomp);
                dbHelper.closeDB();
                NamaDompet.setText(curdomp.getNama_dompet());
                dialog.dismiss();
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
