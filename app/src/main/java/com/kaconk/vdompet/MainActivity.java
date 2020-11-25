package com.kaconk.vdompet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements DompetAdapter.OnListListener {
    SessionManager session;
    TextView txtuser, txtemail;
    CircleImageView improfile;
    FloatingActionButton fab;
    View dialogview;
    EditText txt_dompet;
    String nama_dompet;
    DBHelper dbHelper;
    Users currUser;

    private RecyclerView recyclerView;
    private DompetAdapter adapter;
    private List<Dompet> dompetlist;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new SessionManager(getApplicationContext());
        txtuser = findViewById(R.id.txtuser);
        txtemail = findViewById(R.id.txtemail);
        improfile = findViewById(R.id.profile_pic);
        recyclerView = findViewById(R.id.list_dompet);


        dbHelper = new DBHelper(MainActivity.this);
        fab = findViewById(R.id.fab);
        if (!session.isLoggedIn()){
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            i.addFlags(i.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
        currUser =  new Users();
        currUser = session.getUserDetails();
        txtuser.setText(currUser.nama);
        txtemail.setText(currUser.email);
        List<String> ls = new ArrayList<>();
        ls = dbHelper.getalltable();
        for (int i=0; i< ls.size(); i++){
            Log.d("Table :",ls.get(i));
        }
        List<Dompet> dm = new ArrayList<>();
        dm = dbHelper.getAllDompet(1);
        for (Dompet dom : dm){
            Log.d("Dompet :",dom.getNama_dompet()+", "+ dom.getId_dompet());
        }
        dompetlist = new ArrayList<>();
        dompetlist = dbHelper.getAllDompet(currUser.id);
        dbHelper.closeDB();
        setuplist();
        adapter.notifyDataSetChanged();

        improfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, improfile);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.popup_profile, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.logout:
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:
                                                session.logoutUser();
                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                break;
                                        }
                                    }
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("logout").setMessage("Apakah anda akan Logout ?").setPositiveButton("YES", dialogClickListener).setNegativeButton("NO", dialogClickListener).show();
                                return true;
                            case R.id.profile:

                                return true;

                            default:
                                return false;
                        }

                    }
                });
                popup.show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForm();
            }
        });
    }
    private void dialogForm(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        dialogview = inflater.inflate(R.layout.add_dompet, null);
        dialog.setView(dialogview);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.plus_icon);
        dialog.setTitle("Tambah Dompet");

        txt_dompet = dialogview.findViewById(R.id.nama_dompet);
        txt_dompet.setText(null);

        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dompet dp = new Dompet();
                nama_dompet = txt_dompet.getText().toString();
                dp.setNama_dompet(nama_dompet);
                dbHelper.createDompet(dp,currUser);
                dbHelper.closeDB();
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

    public void setuplist(){
        adapter = new DompetAdapter(MainActivity.this, dompetlist, this);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onListClick(int position) {
        Dompet domp = new Dompet();
        domp = dompetlist.get(position);
        Toast.makeText(MainActivity.this,domp.getId_dompet(),Toast.LENGTH_LONG).show();
    }
}
