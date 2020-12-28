package com.kaconk.vdompet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kaconk.vdompet.Model.GetUser;
import com.kaconk.vdompet.Rest.ApiClient;
import com.kaconk.vdompet.Rest.ApiInterface;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText txt_email, txt_pass;
    Button btn_login;
    Alertdialog alert = new Alertdialog();
    SessionManager session;
    private ApiInterface mApiinterface;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(LoginActivity.this);
        txt_email = findViewById(R.id.txt_email);
        txt_pass = findViewById(R.id.txt_pass);
        btn_login = findViewById(R.id.btn_login);
        mApiinterface = ApiClient.getClient().create(ApiInterface.class);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txt_email.getText().toString();
                String pass = txt_pass.getText().toString();
                if (email.length() > 0 && pass.length()>0){
                    Users usr = new Users();
                    usr.setEmail(email);
                    usr.setPassword(pass);
                    pDialog = new ProgressDialog(LoginActivity.this);
                    pDialog.setMessage("Login....");
                    pDialog.show();

                    Call<GetUser> signIn = mApiinterface.signIn(usr);
                    signIn.enqueue(new Callback<GetUser>() {
                        @Override
                        public void onResponse(Call<GetUser> call, Response<GetUser> response) {
                            pDialog.hide();
                            if (response.isSuccessful()){
                                Users usr = new Users();
                                usr = response.body().getUser();
                                session.CreateLoginSession(usr.getNama(),usr.getEmail(),usr.getId(),usr.getToken());
                                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(i);
                                finish();
                            }else {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage(jObjError.getString("error"))
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                    builder.create();
                                    builder.show();

                                } catch (Exception e) {
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GetUser> call, Throwable t) {
                            Toast.makeText(LoginActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                        }
                    });

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
