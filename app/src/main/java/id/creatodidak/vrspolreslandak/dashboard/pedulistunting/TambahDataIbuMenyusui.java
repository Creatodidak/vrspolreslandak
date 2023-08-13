package id.creatodidak.vrspolreslandak.dashboard.pedulistunting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.ClientStunting;
import id.creatodidak.vrspolreslandak.api.EndpointStunting;
import id.creatodidak.vrspolreslandak.api.models.stunting.ListDes;
import id.creatodidak.vrspolreslandak.api.models.stunting.ListDus;
import id.creatodidak.vrspolreslandak.api.models.stunting.ListKab;
import id.creatodidak.vrspolreslandak.api.models.stunting.ListKec;
import id.creatodidak.vrspolreslandak.api.models.stunting.ListProv;
import id.creatodidak.vrspolreslandak.api.models.stunting.Respstunting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahDataIbuMenyusui extends AppCompatActivity {
    EditText etnama, etnik, ettanggallahir, etpekerjaan, etrt, etrw, etbb, etsistolik, etdiastolik;
    Spinner spprov, spkab, spkec, spdes, spdus, spstatusasi;
    CheckBox cb1, cb2, cb3, cb4;
    LinearLayout loadingRegData;
    ProgressBar pbLoadRegData;
    TextView tvLoadRegData;
    ScrollView formReg;
    Button regis;
    EndpointStunting endpointStunting;

    List<ListProv> listProvinsi = new ArrayList<>();
    List<ListKab> listKabupaten = new ArrayList<>();
    List<ListKec> listKecamatan = new ArrayList<>();
    List<ListDes> listDesa = new ArrayList<>();
    List<ListDus> listDusun = new ArrayList<>();
    List<String> namaProvinsiList = new ArrayList<>();
    List<String> namaKabupatenList = new ArrayList<>();
    List<String> namaKecamatanList = new ArrayList<>();
    List<String> namaDesaList = new ArrayList<>();
    List<String> namaDusunList = new ArrayList<>();

    private List<String> statusAsiList = Arrays.asList("INTESITAS ASI", "NORMAL", "KURANG", "LEBIH");

    private boolean backButtonDisabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data_ibu_menyusui);
        etnama = findViewById(R.id.etRegIbunama);
        etnik = findViewById(R.id.etRegIbuNik);
        ettanggallahir = findViewById(R.id.etRegIbuTanggalLahir);
        etpekerjaan = findViewById(R.id.etRegIbuPekerjaan);
        etrt = findViewById(R.id.etRegIbuRt);
        etrw = findViewById(R.id.etRegIbuRw);
        etbb = findViewById(R.id.etBBIbu);
        etsistolik = findViewById(R.id.etSistolik);
        etdiastolik = findViewById(R.id.etDiastolik);
        spprov = findViewById(R.id.spRegIbuProv);
        spkab = findViewById(R.id.spRegIbuKab);
        spkec = findViewById(R.id.spRegIbuKec);
        spdes = findViewById(R.id.spRegIbuDes);
        spdus = findViewById(R.id.spRegIbuDus);
        cb1 = findViewById(R.id.cbIbu1);
        cb2 = findViewById(R.id.cbIbu2);
        cb3 = findViewById(R.id.cbIbu3);
        cb4 = findViewById(R.id.cbIbu4);
        spstatusasi = findViewById(R.id.spRegStatusAsi);
        ArrayAdapter<String> asiadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusAsiList);
        asiadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spstatusasi.setAdapter(asiadapter);

        loadingRegData = findViewById(R.id.loadingRegIbuData);
        pbLoadRegData = findViewById(R.id.pbLoadRegIbuData);
        tvLoadRegData = findViewById(R.id.tvLoadRegIbuData);
        formReg = findViewById(R.id.formRegIbu);
        regis = findViewById(R.id.btnRegIbu);
        endpointStunting = ClientStunting.getClient().create(EndpointStunting.class);

        ettanggallahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        loadProv();

        spprov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedProvinsi = spprov.getSelectedItem().toString();
                if (!selectedProvinsi.equals("PROVINSI")) {
                    int selectedProvinsiId = listProvinsi.get(position - 1).getId();
                    loadKab(selectedProvinsiId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        spkab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedKabupaten = spkab.getSelectedItem().toString();
                if (!selectedKabupaten.equals("KABUPATEN")) {
                    int selectedKabupatenId = listKabupaten.get(position - 1).getId();
                    loadKec(selectedKabupatenId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        spkec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedKecamatan = spkec.getSelectedItem().toString();
                if (!selectedKecamatan.equals("KECAMATAN")) {
                    int selectedKecamatanId = listKecamatan.get(position - 1).getId();
                    loadDes(selectedKecamatanId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        spdes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedDesa = spdes.getSelectedItem().toString();
                if (!selectedDesa.equals("DESA")) {
                    long selectedDesaId = listDesa.get(position - 1).getId();
                    loadDus(selectedDesaId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDataValid()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataIbuMenyusui.this);
                    builder.setTitle("⚠ Peringatan")
                            .setMessage("Data yang sudah diinput tidak dapat dihapus...")
                            .setPositiveButton("KIRIM DATA", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    loadingRegData.setVisibility(View.VISIBLE);
                                    formReg.setVisibility(View.INVISIBLE);
                                    dialog.dismiss();
                                    kirimdata();
                                    backButtonDisabled = true;
                                }
                            })
                            .setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .setIcon(R.drawable.icon)
                            .setCancelable(false);
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataIbuMenyusui.this);
                    builder.setTitle("Periksa kembali data anda!")
                            .setMessage("ADA DATA KOSONG / BELUM DIPILIH!")
                            .setPositiveButton("PERBAIKI", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .setIcon(R.drawable.icon)
                            .setCancelable(false);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }

    private void kirimdata() {
        SharedPreferences sharedPreferences = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        String satker = sharedPreferences.getString("satker", "POLRES LANDAK");
        String nama = etnama.getText().toString();
        String nik = etnik.getText().toString();
        String tanggallahir = ettanggallahir.getText().toString();
        String pekerjaan = etpekerjaan.getText().toString();
        String rt = etrt.getText().toString();
        String rw = etrw.getText().toString();
        String bb = etbb.getText().toString();
        String sistolik = etsistolik.getText().toString();
        String diastolik = etdiastolik.getText().toString();
        String prov = spprov.getSelectedItem().toString();
        String kab = spkab.getSelectedItem().toString();
        String kec = spkec.getSelectedItem().toString();
        String des = spdes.getSelectedItem().toString();
        String dus = spdus.getSelectedItem().toString();
        String statusasi = spstatusasi.getSelectedItem().toString();
        String valcb1 = getcbval(cb1);
        String valcb2 = getcbval(cb2);
        String valcb3 = getcbval(cb3);
        String valcb4 = getcbval(cb4);

        Call<Respstunting> call = endpointStunting.sendRegistrasiDataibumenyusui(nama, nik, tanggallahir, pekerjaan, rt, rw, bb, statusasi, sistolik, diastolik, prov, kab, kec, des, dus, valcb1, valcb2, valcb3, valcb4, satker);
        call.enqueue(new Callback<Respstunting>() {
            @Override
            public void onResponse(Call<Respstunting> call, Response<Respstunting> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().isOps()){
                        loadingRegData.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataIbuMenyusui.this);
                        builder.setTitle("DATA BERHASIL DIKIRIM")
                                .setMessage(response.body().getMessage())
                                .setPositiveButton("LANJUTKAN", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        TambahDataIbuMenyusui.super.onBackPressed();
                                        finish();
                                    }
                                })
                                .setIcon(R.drawable.icon)
                                .setCancelable(false);
                        AlertDialog alert = builder.create();
                        alert.show();
                    }else{
                        loadingRegData.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataIbuMenyusui.this);
                        builder.setTitle("DATA GAGAL DIKIRIM")
                                .setMessage(response.body().getMessage())
                                .setPositiveButton("LANJUTKAN", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        formReg.setVisibility(View.VISIBLE);
                                    }
                                })
                                .setIcon(R.drawable.icon)
                                .setCancelable(false);
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }else{
                    loadingRegData.setVisibility(View.GONE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataIbuMenyusui.this);
                    builder.setTitle("DATA GAGAL DIKIRIM")
                            .setMessage("Silahkan periksa jaringan anda atau hubungi TIK Polres Landak")
                            .setPositiveButton("LANJUTKAN", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    formReg.setVisibility(View.VISIBLE);
                                }
                            })
                            .setIcon(R.drawable.icon)
                            .setCancelable(false);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<Respstunting> call, Throwable t) {
                loadingRegData.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataIbuMenyusui.this);
                builder.setTitle("DATA GAGAL DIKIRIM")
                        .setMessage("Silahkan periksa jaringan anda atau hubungi TIK Polres Landak")
                        .setPositiveButton("LANJUTKAN", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                formReg.setVisibility(View.VISIBLE);
                            }
                        })
                        .setIcon(R.drawable.icon)
                        .setCancelable(false);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private String getcbval(CheckBox cb) {
        if (cb.isChecked()) {
            return cb.getText().toString();
        } else {
            return "-";
        }
    }

    private void loadProv() {
        Call<List<ListProv>> call = endpointStunting.listProv();
        call.enqueue(new Callback<List<ListProv>>() {
            @Override
            public void onResponse(Call<List<ListProv>> call, Response<List<ListProv>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listProvinsi.clear();
                    listProvinsi.addAll(response.body());

                    namaProvinsiList.add("PROVINSI");
                    for (ListProv provinsi : listProvinsi) {
                        namaProvinsiList.add(provinsi.getNama());
                    }

                    ArrayAdapter<String> provAdapter = new ArrayAdapter<>(TambahDataIbuMenyusui.this, android.R.layout.simple_spinner_item, namaProvinsiList);
                    provAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spprov.setAdapter(provAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<ListProv>> call, Throwable t) {

            }
        });
    }

    private void loadKab(int selectedProvinsiId) {
        Call<List<ListKab>> call = endpointStunting.listKab(selectedProvinsiId);
        call.enqueue(new Callback<List<ListKab>>() {
            @Override
            public void onResponse(Call<List<ListKab>> call, Response<List<ListKab>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listKabupaten.clear();
                    listKabupaten.addAll(response.body());


                    namaKabupatenList.add("KABUPATEN");
                    for (ListKab kabupaten : listKabupaten) {
                        namaKabupatenList.add(kabupaten.getNama());
                    }

                    ArrayAdapter<String> kabAdapter = new ArrayAdapter<>(TambahDataIbuMenyusui.this, android.R.layout.simple_spinner_item, namaKabupatenList);
                    kabAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spkab.setAdapter(kabAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<ListKab>> call, Throwable t) {

            }
        });
    }

    private void loadKec(int selectedKabupatenId) {
        Call<List<ListKec>> call = endpointStunting.listKec(selectedKabupatenId);
        call.enqueue(new Callback<List<ListKec>>() {
            @Override
            public void onResponse(Call<List<ListKec>> call, Response<List<ListKec>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listKecamatan.clear();
                    listKecamatan.addAll(response.body());


                    namaKecamatanList.add("KECAMATAN");
                    for (ListKec kecamatan : listKecamatan) {
                        namaKecamatanList.add(kecamatan.getNama());
                    }

                    ArrayAdapter<String> kecAdapter = new ArrayAdapter<>(TambahDataIbuMenyusui.this, android.R.layout.simple_spinner_item, namaKecamatanList);
                    kecAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spkec.setAdapter(kecAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<ListKec>> call, Throwable t) {

            }
        });
    }

    private void loadDes(int selectedKecamatanId) {
        Call<List<ListDes>> call = endpointStunting.listDes(selectedKecamatanId);
        call.enqueue(new Callback<List<ListDes>>() {
            @Override
            public void onResponse(Call<List<ListDes>> call, Response<List<ListDes>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listDesa.clear();
                    listDesa.addAll(response.body());


                    namaDesaList.add("DESA");
                    for (ListDes desa : listDesa) {
                        namaDesaList.add(desa.getNama());
                    }

                    ArrayAdapter<String> desAdapter = new ArrayAdapter<>(TambahDataIbuMenyusui.this, android.R.layout.simple_spinner_item, namaDesaList);
                    desAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spdes.setAdapter(desAdapter); // Set the adapter for spdes

                    // Log statements for debugging
                    Log.d("Debug", "Desa list size: " + listDesa.size());
                    for (ListDes desa : listDesa) {
                        Log.d("Debug", "Desa name: " + desa.getNama());
                    }
                } else {
                    Log.e("Error", "API response not successful");
                }
            }

            @Override
            public void onFailure(Call<List<ListDes>> call, Throwable t) {
                Log.e("Error", "API call failed: " + t.getMessage());
            }
        });
    }

    private void loadDus(long selectedDesaId) {
        Call<List<ListDus>> call = endpointStunting.listDus(selectedDesaId);
        call.enqueue(new Callback<List<ListDus>>() {
            @Override
            public void onResponse(Call<List<ListDus>> call, Response<List<ListDus>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listDusun.clear();
                    listDusun.addAll(response.body());

                    namaDusunList.add("DUSUN");
                    for (ListDus dusun : listDusun) {
                        namaDusunList.add(dusun.getNama());
                    }

                    ArrayAdapter<String> dusundapter = new ArrayAdapter<>(TambahDataIbuMenyusui.this, android.R.layout.simple_spinner_item, namaDusunList);
                    dusundapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spdus.setAdapter(dusundapter);

                    // Log the size of the loaded listDusun and the number of items in namaDusunList
                    Log.d("loadDus", "listDusun size: " + listDusun.size());
                    Log.d("loadDus", "namaDusunList size: " + namaDusunList.size());
                }
            }

            @Override
            public void onFailure(Call<List<ListDus>> call, Throwable t) {
                Log.e("loadDus", "API call failed: " + t.getMessage());
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        String tanggal = String.format("%04d-%02d-%02d", selectedYear, (selectedMonth + 1), selectedDayOfMonth);
                        ettanggallahir.setText(tanggal);
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); // Set background transparan
        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.getDatePicker().setSpinnersShown(true);
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (!backButtonDisabled) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Yakin ingin keluar?")
                    .setMessage("Data yang sudah diisi mungkin akan hilang.")
                    .setPositiveButton("KELUAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            TambahDataIbuMenyusui.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder.setIcon(R.drawable.icon);
            builder.setCancelable(false);
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private boolean isDataValid() {
        return !TextUtils.isEmpty(etnama.getText()) &&
                !TextUtils.isEmpty(etnik.getText()) &&
                !TextUtils.isEmpty(ettanggallahir.getText()) &&
                !TextUtils.isEmpty(etpekerjaan.getText()) &&
                !TextUtils.isEmpty(etrt.getText()) &&
                !TextUtils.isEmpty(etrw.getText()) &&
                !TextUtils.isEmpty(etbb.getText()) &&
                !TextUtils.isEmpty(etsistolik.getText()) &&
                !TextUtils.isEmpty(etdiastolik.getText()) &&
                spprov.getSelectedItem() != null &&
                spkab.getSelectedItem() != null &&
                spkec.getSelectedItem() != null &&
                spdes.getSelectedItem() != null &&
                spdus.getSelectedItem() != null &&
                spstatusasi.getSelectedItem() != null &&
                !spstatusasi.getSelectedItem().toString().equals("INTESITAS ASI") &&
                !spprov.getSelectedItem().toString().equals("PROVINSI") &&
                !spkab.getSelectedItem().toString().equals("KABUPATEN") &&
                !spkec.getSelectedItem().toString().equals("KECAMATAN") &&
                !spdes.getSelectedItem().toString().equals("DESA") &&
                !spdus.getSelectedItem().toString().equals("DUSUN");
    }
}