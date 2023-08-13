package id.creatodidak.vrspolreslandak.helper;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;

import id.creatodidak.vrspolreslandak.R;

public class CustomDialogForm extends Dialog {

    private EditText namaitem, jumlahitem;
    private Spinner satuanitem;
    private Button btnSubmit, batal;
    private List<String> satuan = Arrays.asList("BUAH", "BUTIR", "BIJI", "KG", "LITER", "POTONG", "PORSI", "KOTAK", "BOTOL", "BUNGKUS", "SET");


    private OnSubmitClickListener onSubmitClickListener;

    public CustomDialogForm(Context context, OnSubmitClickListener listener) {
        super(context);
        this.onSubmitClickListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formdialog);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setCancelable(false);
        batal = findViewById(R.id.btnBatal);
        namaitem = findViewById(R.id.etNamaItem);
        jumlahitem = findViewById(R.id.etJumlahItem);
        btnSubmit = findViewById(R.id.btKirimItem);
        satuanitem = findViewById(R.id.spSatuan);
        ArrayAdapter<String> satuanAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, satuan);
        satuanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        satuanitem.setAdapter(satuanAdapter);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = namaitem.getText().toString().toUpperCase();
                String jumlah = jumlahitem.getText().toString();
                String satuan = satuanitem.getSelectedItem().toString();

                String data = item.toUpperCase() +" @ " +jumlah+" "+satuan;
                onSubmitClickListener.onSubmitClicked(data);
                dismiss();
            }
        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public interface OnSubmitClickListener {
        void onSubmitClicked(String data);
    }
}
