package com.kaconk.vdompet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.kaconk.vdompet.Model.NewDompet;
import com.kaconk.vdompet.Rest.ApiClient;
import com.kaconk.vdompet.Rest.ApiInterface;

import java.text.NumberFormat;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransActivity extends AppCompatActivity implements EventListener {
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Dompet curdomp;
    private ApiInterface mApiinterface;
    private SessionManager session;
    private Users currUser;


    private TextView NamaDompet, SaldoDompet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);
        session = new SessionManager(getApplicationContext());
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        String id_domp = (String) getIntent().getSerializableExtra("id_domp");
        NamaDompet = findViewById(R.id.namedompet);
        SaldoDompet = findViewById(R.id.statSaldo);
        mApiinterface = ApiClient.getClient().create(ApiInterface.class);
        currUser =  new Users();
        currUser = session.getUserDetails();
        Bundle dt = new Bundle();
        dt.putString("id_dompet",id_domp);
        Fragment fone = new TabInFrag();
        fone.setArguments(dt);
        Fragment ftwo = new TabOutFrag();
        ftwo.setArguments(dt);
        Fragment fthr = new TabHystFrag();
        fthr.setArguments(dt);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(fone,"IN");
        adapter.addFragment(ftwo,"OUT");
        adapter.addFragment(fthr,"History");

        curdomp = new Dompet();
        getDompetdata(id_domp);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);



    }

    public void getDompetdata(String id){
        Call<NewDompet> getdomp = mApiinterface.getdompet(currUser.token,id);
        getdomp.enqueue(new Callback<NewDompet>() {
            @Override
            public void onResponse(Call<NewDompet> call, Response<NewDompet> response) {
                if (response.isSuccessful()){
                    curdomp = response.body().getDompet();
                    SaldoDompet.setText("Saldo : Rp "+NumberFormat.getInstance().format(curdomp.getSaldo()));
                    NamaDompet.setText(curdomp.getNama_dompet());
                }
            }

            @Override
            public void onFailure(Call<NewDompet> call, Throwable t) {

            }
        });

    }

}
