package com.kaconk.vdompet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
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
import com.kaconk.vdompet.Model.NewDompet;
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

public class MainActivity extends AppCompatActivity {
    SessionManager session;
    TextView txtuser, txtemail;
    CircleImageView improfile;
    FloatingActionButton fab;
    View dialogview;
    EditText txt_dompet;
    String nama_dompet;


    private Users currUser;
    private RecyclerView recyclerView;
    private AdapterDompet adapter;
    private List<Dompet> dompetlist;
    private LinearLayoutManager linearLayoutManager;
    private ApiInterface mApiinterface;
    private ProgressDialog pDialog;

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
        dompetlist = new ArrayList<>();


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

        adapter = new AdapterDompet(MainActivity.this);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(MainActivity.this, recyclerView, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, final int position) {
                Dompet domp = new Dompet();
                domp = dompetlist.get(position);
                Intent i = new Intent(MainActivity.this, TransActivity.class);
                i.putExtra("id_domp", domp.getId_dompet());
                 startActivity(i);
            }

            @Override
            public void onLongClick(View view, final int position) {
                PopupMenu popup = new PopupMenu(MainActivity.this,view);
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
                                                Call<NewDompet> delDomp = mApiinterface.delDompet(currUser.token,dompetlist.get(position).getId_dompet());
                                                delDomp.enqueue(new Callback<NewDompet>() {
                                                    @Override
                                                    public void onResponse(Call<NewDompet> call, Response<NewDompet> response) {
                                                        if (response.isSuccessful()){
                                                            adapter.removeAt(position);
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
                                                    public void onFailure(Call<NewDompet> call, Throwable t) {

                                                    }
                                                });


                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                break;
                                        }
                                    }
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Delete").setMessage("Apakah anda akan menghapus "+dompetlist.get(position).getNama_dompet()+" ?").setPositiveButton("YES", dialogClickListener).setNegativeButton("NO", dialogClickListener).show();
                                return true;
                            case R.id.editDompet:
                                dialogEdit(dompetlist.get(position));

                                return true;

                            default:
                                return false;
                        }

                    }
                });
                popup.show();
            }
        }));
        populateData();


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
                Call<NewDompet> insertDompet = mApiinterface.createDompet(currUser.token,dp);
                insertDompet.enqueue(new Callback<NewDompet>() {
                    @Override
                    public void onResponse(Call<NewDompet> call, Response<NewDompet> response) {
                        if (response.isSuccessful()){
                            Dompet domp = new Dompet();
                            domp = response.body().getDompet();

                            dompetlist.add(domp);
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
                    public void onFailure(Call<NewDompet> call, Throwable t) {

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
    private void dialogEdit(final Dompet dp){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        dialogview = inflater.inflate(R.layout.add_dompet, null);
        dialog.setView(dialogview);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.plus_icon);
        dialog.setTitle("Edit Dompet");

        txt_dompet = dialogview.findViewById(R.id.nama_dompet);
        txt_dompet.setText(dp.getNama_dompet());

        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                dp.setNama_dompet(txt_dompet.getText().toString());
                Call<NewDompet> editdompet = mApiinterface.updatedompet(currUser.token,dp.getId_dompet(),dp);
                editdompet.enqueue(new Callback<NewDompet>() {
                    @Override
                    public void onResponse(Call<NewDompet> call, Response<NewDompet> response) {
                        if (response.isSuccessful()){
                            populateData();
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
                    public void onFailure(Call<NewDompet> call, Throwable t) {

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
   private void populateData(){

       dompetlist.clear();

       Call<GetDompet> getallDompet = mApiinterface.getalldompet(currUser.token);
       getallDompet.enqueue(new Callback<GetDompet>() {
           @Override
           public void onResponse(Call<GetDompet> call, Response<GetDompet> response) {
              
               if (response.isSuccessful()){

                   dompetlist = response.body().getListDompet();
                   adapter.setListContent(dompetlist);
                   recyclerView.setAdapter(adapter);
               }else {
                   try {
                       JSONObject jObjError = new JSONObject(response.errorBody().string());
                       Toast.makeText(MainActivity.this, jObjError.getString("error"), Toast.LENGTH_LONG).show();
                   } catch (Exception e) {
                       Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                   }
               }
           }

           @Override
           public void onFailure(Call<GetDompet> call, Throwable t) {

           }
       });

   }

    @Override
    protected void onResume() {
        super.onResume();
        populateData();
    }
}
