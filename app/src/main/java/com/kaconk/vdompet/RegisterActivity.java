package com.kaconk.vdompet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class RegisterActivity extends AppCompatActivity {
    Button btn_reg;
    EditText txt_nama, txt_email, txt_pass;
    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbHelper = new DBHelper(getApplicationContext());
        btn_reg = findViewById(R.id.btn_register);
        txt_nama = findViewById(R.id.txt_nama);
        txt_email = findViewById(R.id.txt_email);
        txt_pass = findViewById(R.id.txt_pass);


        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()){
                    String nama = txt_nama.getText().toString();
                    String email = txt_email.getText().toString();
                    String pass = txt_pass.getText().toString();

                    if (!dbHelper.checkEmail(email)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        Users user = new Users();
                        user.setNama(nama);
                        user.setEmail(email);
                        user.setPassword(pass);

                        dbHelper.createUser(user);

                       builder.setMessage("Register Berhasil silahkan Login!")
                               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       finish();
                                   }
                               });
                       builder.create();
                       builder.show();


                        //Toast.makeText(RegisterActivity.this,"Register Berhasil !",Toast.LENGTH_LONG).show();


                    }else{
                        Toast.makeText(RegisterActivity.this,"Email sudah terdaftar !",Toast.LENGTH_LONG).show();
                    }
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
