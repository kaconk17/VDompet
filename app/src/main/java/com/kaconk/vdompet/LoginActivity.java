package com.kaconk.vdompet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    DBHelper dbHelper;
    EditText txt_email, txt_pass;
    Button btn_login;
    Alertdialog alert = new Alertdialog();
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new DBHelper(getApplicationContext());
        session = new SessionManager(LoginActivity.this);
        txt_email = findViewById(R.id.txt_email);
        txt_pass = findViewById(R.id.txt_pass);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txt_email.getText().toString();
                String pass = txt_pass.getText().toString();
                if (email.length() > 0 && pass.length()>0){
                    Users usr = new Users();

                    usr = dbHelper.Authenticate(email,pass);
                    dbHelper.closeDB();


                    if (usr != null){
                        //session.CreateLoginSession(usr.nama,usr.email,usr.id,"token");
                        Intent i = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }else {
                        alert.showAlertDialog(LoginActivity.this,"Login","Email atau Password Salah !",false);
                    }
                }else {
                    alert.showAlertDialog(LoginActivity.this,"Login","Email atau Password Belum diisi !",false);
                }
            }
        });


    }

    public void viewRegisterClicked(View view) {
        Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(i);
    }
}
