package com.kaconk.vdompet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.Edits;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kaconk.vdompet.Model.GetUser;
import com.kaconk.vdompet.Rest.ApiClient;
import com.kaconk.vdompet.Rest.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    Button btn_reg;
    EditText txt_nama, txt_email, txt_pass;
    DBHelper dbHelper;
    private String BASE_URL = "https://vdompet.herokuapp.com/api/auth/signup/";
    private ProgressDialog pDialog;
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private ApiInterface mApiinterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbHelper = new DBHelper(getApplicationContext());
        btn_reg = findViewById(R.id.btn_register);
        txt_nama = findViewById(R.id.txt_nama);
        txt_email = findViewById(R.id.txt_email);
        txt_pass = findViewById(R.id.txt_pass);
        mApiinterface = ApiClient.getClient().create(ApiInterface.class);

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()){
                    final String nama = txt_nama.getText().toString();
                    final String email = txt_email.getText().toString();
                    final String pass = txt_pass.getText().toString();
                    Users usr =  new Users();
                    usr.setNama(nama);
                    usr.setEmail(email);
                    usr.setPassword(pass);

                    pDialog = new ProgressDialog(RegisterActivity.this);
                    pDialog.setMessage("Sending....");
                    pDialog.show();
                    Call<GetUser> createUser = mApiinterface.createUser(usr);
                    createUser.enqueue(new Callback<GetUser>() {
                        @Override
                        public void onResponse(Call<GetUser> call, Response<GetUser> response) {
                            pDialog.hide();
                            if (response.isSuccessful()){
                                final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("Register Berhasil silahkan Login!")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                finish();
                                            }
                                        });
                                builder.create();
                                builder.show();
                            }else {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage(jObjError.getString("error"))
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                    builder.create();
                                    builder.show();
                                    //Toast.makeText(RegisterActivity.this, jObjError.getString("error"), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GetUser> call, Throwable t) {
                            Toast.makeText(RegisterActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                        }
                    });

                }else{
                    Toast.makeText(RegisterActivity.this,"Data belum lengkap !",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean validation(){
        boolean valid = false;

        String nama = txt_nama.getText().toString();
        String emial = txt_email.getText().toString();
        String pass = txt_pass.getText().toString();

        if (nama.isEmpty()){
            valid = false;
        }else{
            if (nama.length() > 3){
                valid = true;
            }else{
                valid = false;
            }
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emial).matches()){
            valid = false;
        }else{
            valid = true;
        }

        if (pass.isEmpty()){
            valid = false;
        }else {
            if (pass.length()>4){
                valid = true;
            }else {
                valid = false;
            }
        }
        return valid;
    }

    public void viewloginclick(View view) {
        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
    }
}
