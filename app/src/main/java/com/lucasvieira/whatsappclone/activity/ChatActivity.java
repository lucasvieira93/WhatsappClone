package com.lucasvieira.whatsappclone.activity;

import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.lucasvieira.whatsappclone.R;
import com.lucasvieira.whatsappclone.config.ConfiguracaoFirebase;
import com.lucasvieira.whatsappclone.helper.Base64Custom;
import com.lucasvieira.whatsappclone.helper.UsuarioFirebase;
import com.lucasvieira.whatsappclone.model.Mensagem;
import com.lucasvieira.whatsappclone.model.Usuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewNome;
    private CircleImageView circleImageViewFoto;
    private Usuario usuarioDestinatario;
    private EditText editMensagem;
    private Usuario UsuarioDestinatario;

    //identificador usuarios
    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Configurações Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configurações iniciais
        textViewNome = findViewById(R.id.textViewNomeChat);
        circleImageViewFoto = findViewById(R.id.circleImageFotoChat);
        editMensagem = findViewById(R.id.editMensagem);

        //Recupera dados do usuario remetente
        idUsuarioRemetente = UsuarioFirebase.getIdentificadorUsuario();

        //Recuperar dados do usuario
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            usuarioDestinatario = (Usuario) bundle.getSerializable("chatContato");
            textViewNome.setText(usuarioDestinatario.getNome());

            String foto = usuarioDestinatario.getFoto();
            if (foto != null){
                Uri url = Uri.parse(usuarioDestinatario.getFoto());
                Glide.with(ChatActivity.this)
                        .load(circleImageViewFoto);
            } else {
                circleImageViewFoto.setImageResource(R.drawable.padrao);
            }

            //recuuperar dados usuario destinatario
            idUsuarioDestinatario = Base64Custom.codificarBase64(usuarioDestinatario.getEmail());
        }

    }
    
    public void enviarMensagem(View view){
        String textoMensagem = editMensagem.getText().toString();
        
        if (!textoMensagem.isEmpty()){

            Mensagem mensagem = new Mensagem();
            mensagem.setIdUsuario(idUsuarioRemetente);
            mensagem.setMensagem(textoMensagem);

            salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);

        } else {
            Toast.makeText(this, "Digite uma mensagem para enviar!", Toast.LENGTH_SHORT).show();
        }
    }

    private void salvarMensagem(String idRemetente, String idDestinatario, Mensagem msg){

        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference mensagemRef = database.child("mensagens");
        
        mensagemRef.child(idRemetente)
                .child(idDestinatario)
                .push()
                .setValue(msg);

        //Limpar texto
        editMensagem.setText("");
    }
}