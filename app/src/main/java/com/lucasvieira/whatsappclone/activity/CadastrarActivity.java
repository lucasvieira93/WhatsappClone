package com.lucasvieira.whatsappclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.lucasvieira.whatsappclone.helper.Base64Custom;
import com.lucasvieira.whatsappclone.R;
import com.lucasvieira.whatsappclone.config.ConfiguracaoFirebase;
import com.lucasvieira.whatsappclone.model.Usuario;

public class CadastrarActivity extends AppCompatActivity {

    private TextInputEditText campoNome, campoEmail, campoSenha;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        campoNome = findViewById(R.id.campoNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);

    }

    public void cadastrarUsuario(Usuario usuario) {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                        Toast.makeText(CadastrarActivity.this, "Cadastrado com Sucesso!", Toast.LENGTH_SHORT).show();
                    finish();

                    try {

                        String identificadorUsuario = Base64Custom.codificarBase64( usuario.getEmail());
                        usuario.setId(identificadorUsuario);
                        usuario.salvar();

                    } catch (Exception e){
                        e.getMessage();
                    }

                } else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        excecao = "Digite uma senha mais forte!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Por favor, digite um e-mail válido!";
                    } catch (FirebaseAuthUserCollisionException e) {
                        excecao = "Esta conta já foi cadastrada!";
                    } catch (Exception e) {
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastrarActivity.this, excecao, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void validarUsuario(View view) {

        String textoNome = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();

        if (!textoNome.isEmpty() || !textoEmail.isEmpty() || !textoSenha.isEmpty()) {
            Usuario usuario = new Usuario();
            usuario.setNome(textoNome);
            usuario.setEmail(textoEmail);
            usuario.setSenha(textoSenha);

            cadastrarUsuario(usuario);

        } else {
            Toast.makeText(this, "Preencha os campos!", Toast.LENGTH_SHORT).show();

        }
    }
}