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

import de.hdodenhof.circleimageview.CircleImageView;

public class TransActivity extends AppCompatActivity implements EventListener {
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Dompet curdomp;
    private DBHelper dbHelper;


    private TextView NamaDompet, SaldoDompet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        String id_domp = (String) getIntent().getSerializableExtra("id_domp");
        NamaDompet = findViewById(R.id.namedompet);
        SaldoDompet = findViewById(R.id.statSaldo);
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

        dbHelper = new DBHelper(TransActivity.this);
        getDompetdata(id_domp);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);



    }

    public void getDompetdata(String id){
        curdomp = dbHelper.getDompet(id);
        dbHelper.closeDB();
        SaldoDompet.setText("Rp "+String.valueOf(curdomp.getSaldo()));
        NamaDompet.setText(curdomp.getNama_dompet());
    }

}
