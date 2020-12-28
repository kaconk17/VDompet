package com.kaconk.vdompet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.kaconk.vdompet.Model.GetDompet;
import com.kaconk.vdompet.Rest.ApiClient;
import com.kaconk.vdompet.Rest.ApiInterface;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements DompetAdapter.OnListListener {
    SessionManager session;
    TextView txtuser, txtemail;
    CircleImageView improfile;
    FloatingActionButton fab;
    View dialogview;
    EditText txt_dompet;
    String nama_dompet;
    DBHelper dbHelper;

    private Users currUser;
    private RecyclerView recyclerView;
    private DompetAdapter adapter;
    private List<Dompet> dompetlist;
    private LinearLayoutManager linearLayoutManager;
    private ApiInterface mApiinterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new SessionManager(getApplicationContext());
        txtuser = findViewById(R.id.txtuser);
        txtemail = findViewById(R.id.txtemail);
        improfile = findViewById(R.id.profile_pic);
        recyclerView = findViewById(R.id.list_dompet);
        mApiinterface = ApiClient.getClient().create(ApiInterface.class);

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
        dompetlist = new ArrayList<>();
        Call<GetDompet> getallDompet = mApiinterface.getalldompet(currUser.token);
        getallDompet.enqueue(new Callback<GetDompet>() {
            @Override
            public void onResponse(Call<GetDompet> call, Response<GetDompet> response) {
                if (response.isSuccessful()){
                    dompetlist = response.body().getListDompet();
                }else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
                        builder.setMessage(jObjError.getString("error"))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder.create();
                        builder.show();

                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetDompet> call, Throwable t) {

            }
        });
        adapter = new DompetAdapter(MainActivity.this, dompetlist, this);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
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
            public void onClick(final DialogInterface dialog, int which) {
                Dompet dp = new Dompet();
                nama_dompet = txt_dompet.getText().toString();
                dp.setNama_dompet(nama_dompet);
                dp.setSaldo(0);

                Call<Dompet> insertDompet = mApiinterface.createDompet(currUser.token,dp);
                insertDompet.enqueue(new Callback<Dompet>() {
                    @Override
                    public void onResponse(Call<Dompet> call, Response<Dompet> response) {
                        if (response.isSuccessful()){
                            Dompet newDompet = new Dompet();
                            newDompet = response.body();
                            dompetlist.add(newDompet);
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
                                builder.setMessage(jObjError.getString("error"))
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                builder.create();
                                builder.show();
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Dompet> call, Throwable t) {

                    }
                });

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
    @Override
    public void onListClick(int position) {
        Dompet domp = new Dompet();
        domp = dompetlist.get(position);
        Intent i = new Intent(this,TransActivity.class);
        i.putExtra("id_domp", domp.getId_dompet());
        startActivity(i);
        //Toast.makeText(MainActivity.this,domp.getId_dompet(),Toast.LENGTH_LONG).show();
    }

    public static void removeDompet(String id_dompet){

    }
}
