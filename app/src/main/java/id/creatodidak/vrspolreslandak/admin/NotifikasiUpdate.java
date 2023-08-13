package id.creatodidak.vrspolreslandak.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.admin.model.ResponseNotif;
import id.creatodidak.vrspolreslandak.api.Client;
import id.creatodidak.vrspolreslandak.api.Endpoint;
import id.creatodidak.vrspolreslandak.dashboard.pedulistunting.TambahDataAnak;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifikasiUpdate extends AppCompatActivity {
    EditText etTittle, etBody;
    Button btkirim;
    Endpoint endpoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi_update);
        etTittle = findViewById(R.id.etTittle);
        etBody = findViewById(R.id.etBody);
        btkirim = findViewById(R.id.btKirimNotif);
        endpoint = Client.getClient().create(Endpoint.class);

        btkirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTittle.getText().toString();
                String body = etBody.getText().toString();
                String topic = "UpdateVRS";

                Call<ResponseNotif> call = endpoint.kirimUpdateNotif(title, body, topic);
                call.enqueue(new Callback<ResponseNotif>() {
                    @Override
                    public void onResponse(Call<ResponseNotif> call, Response<ResponseNotif> response) {
                        if(response.isSuccessful() && response.body() != null){
                            AlertDialog.Builder builder = new AlertDialog.Builder(NotifikasiUpdate.this);
                            builder.setTitle("SUKSES!")
                                    .setMessage("SUKSES: "+String.valueOf(response.body().getSuccess())+"\nGAGAL: "+String.valueOf(response.body().getSuccess()))
                                    .setPositiveButton("LANJUTKAN", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setIcon(R.drawable.icon)
                                    .setCancelable(false);
                            AlertDialog alert = builder.create();
                            alert.show();
                        }else{
                            Toast.makeText(NotifikasiUpdate.this, "GAGAL KIRIM NOTIF!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseNotif> call, Throwable t) {
                        Toast.makeText(NotifikasiUpdate.this, "GAGAL KIRIM NOTIF!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}