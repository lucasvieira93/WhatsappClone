package com.lucasvieira.whatsappclone.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.lucasvieira.whatsappclone.R;
import com.lucasvieira.whatsappclone.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText campoEmail, campoSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);

    }

    public void esqueceuSenha(View view){
        Intent intent = new Intent(LoginActivity.this, CadastrarActivity.class);
        startActivity(intent);
    }

    public void logarUsuario(View view){

        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();

        if ( !textoEmail.isEmpty() || !textoSenha.isEmpty()) {


        } else {
            Toast.makeText(this, "Preencha os campos!", Toast.LENGTH_SHORT).show();

        }
    }
}