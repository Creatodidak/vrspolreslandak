package id.creatodidak.vrspolreslandak.dashboard.pedulistunting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class TambahDataAnak extends AppCompatActivity {

    EditText tanggallahir, nik, rt, rw, nama, namaibu, bb, tb, lk;
    Spinner spprov, spkab, spkec, spdes, spdus, spjk;
    private List<String> jenkel = Arrays.asList("JENIS KELAMIN", "LAKI - LAKI", "PEREMPUAN");
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
    EndpointStunting endpointStunting;

    LinearLayout loadingRegData;
    ProgressBar pbLoadRegData;
    TextView tvLoadRegData;
    ScrollView formReg;
    Button regis;

    private boolean backButtonDisabled = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data_anak);
        tanggallahir = findViewById(R.id.etRegTanggalLahir);
        spprov = findViewById(R.id.spRegProv);
        spkab = findViewById(R.id.spRegKab);
        spkec = findViewById(R.id.spRegKec);
        spdes = findViewById(R.id.spRegDes);
        spdus = findViewById(R.id.spRegDus);
        spjk = findViewById(R.id.spRegJenis);
        nik  = findViewById(R.id.etRegNik);
        rt = findViewById(R.id.etRegRt);
        rw = findViewById(R.id.etRegRw);
        nama = findViewById(R.id.etRegnama);
        namaibu = findViewById(R.id.etRegNamaIbu);
        bb = findViewById(R.id.etBB);
        tb = findViewById(R.id.etTB);
        lk = findViewById(R.id.etLK);

        regis = findViewById(R.id.btnReg);
        endpointStunting = ClientStunting.getClient().create(EndpointStunting.class);

        loadingRegData = findViewById(R.id.loadingRegData);
        pbLoadRegData = findViewById(R.id.pbLoadRegData);
        tvLoadRegData = findViewById(R.id.tvLoadRegData);
        formReg = findViewById(R.id.formReg);
        tanggallahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        ArrayAdapter<String> jkAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jenkel);
        jkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spjk.setAdapter(jkAdapter);

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
                if(Double.parseDouble(tb.getText().toString()) >= 45) {
                    if (isDataValid()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataAnak.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataAnak.this);
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
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataAnak.this);
                    builder.setTitle("DATA DITOLAK")
                            .setMessage("Panjang badan minimum yang dapat diinput pada aplikasi ini adalah 45 CM sesuai dengan PERMENKES RI NOMOR 2 TAHUN 2020 ttg STANDAR ANTROPOMETRI ANAK")
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
        String dnik = nik.getText().toString();
        String dnama = nama.getText().toString();
        String dtanggallahir = tanggallahir.getText().toString();
        String djeniskelamin = getJenisKelaminShortForm(spjk.getSelectedItem().toString());
        String dnamaibu = namaibu.getText().toString();
        String drt = rt.getText().toString();
        String drw = rw.getText().toString();
        String ddusun = spdus.getSelectedItem().toString();
        String ddesa = spdes.getSelectedItem().toString();
        String dkecamatan = spkec.getSelectedItem().toString();
        String dkabupaten = spkab.getSelectedItem().toString();
        String dprovinsi = spprov.getSelectedItem().toString();
        String dbb = bb.getText().toString();
        String dtb = tb.getText().toString();
        String dlk = lk.getText().toString();

        Call<Respstunting> call = endpointStunting.sendRegistrasiData(dnik, dnama, dtanggallahir, djeniskelamin, dnamaibu, drt, drw, ddusun, ddesa, dkecamatan, dkabupaten, dprovinsi, dbb, dtb, dlk);

        call.enqueue(new Callback<Respstunting>() {
            @Override
            public void onResponse(Call<Respstunting> call, Response<Respstunting> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().isOps()){
                        loadingRegData.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataAnak.this);
                        builder.setTitle("DATA BERHASIL DIKIRIM")
                                .setMessage(response.body().getMessage())
                                .setPositiveButton("LANJUTKAN", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        TambahDataAnak.super.onBackPressed();
                                        finish();
                                    }
                                })
                                .setIcon(R.drawable.icon)
                                .setCancelable(false);
                        AlertDialog alert = builder.create();
                        alert.show();
                    }else{
                        loadingRegData.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataAnak.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataAnak.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataAnak.this);
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

    private String getJenisKelaminShortForm(String jenisKelamin) {
        if (jenisKelamin.equals("LAKI - LAKI")) {
            return "L";
        } else{
            return "P";
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

                    ArrayAdapter<String> provAdapter = new ArrayAdapter<>(TambahDataAnak.this, android.R.layout.simple_spinner_item, namaProvinsiList);
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

                    ArrayAdapter<String> kabAdapter = new ArrayAdapter<>(TambahDataAnak.this, android.R.layout.simple_spinner_item, namaKabupatenList);
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

                    ArrayAdapter<String> kecAdapter = new ArrayAdapter<>(TambahDataAnak.this, android.R.layout.simple_spinner_item, namaKecamatanList);
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

                    ArrayAdapter<String> desAdapter = new ArrayAdapter<>(TambahDataAnak.this, android.R.layout.simple_spinner_item, namaDesaList);
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

                    ArrayAdapter<String> dusundapter = new ArrayAdapter<>(TambahDataAnak.this, android.R.layout.simple_spinner_item, namaDusunList);
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
                        tanggallahir.setText(tanggal);
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
                            TambahDataAnak.super.onBackPressed();
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
        return !TextUtils.isEmpty(tanggallahir.getText()) &&
                !TextUtils.isEmpty(nik.getText()) &&
                !TextUtils.isEmpty(rt.getText()) &&
                !TextUtils.isEmpty(rw.getText()) &&
                !TextUtils.isEmpty(nama.getText()) &&
                !TextUtils.isEmpty(namaibu.getText()) &&
                !TextUtils.isEmpty(bb.getText()) &&
                !TextUtils.isEmpty(tb.getText()) &&
                !TextUtils.isEmpty(lk.getText()) &&
                spprov.getSelectedItem() != null &&
                spkab.getSelectedItem() != null &&
                spkec.getSelectedItem() != null &&
                spdes.getSelectedItem() != null &&
                spdus.getSelectedItem() != null &&
                spjk.getSelectedItem() != null &&
                !spprov.getSelectedItem().toString().equals("PROVINSI") &&
                !spkab.getSelectedItem().toString().equals("KABUPATEN") &&
                !spkec.getSelectedItem().toString().equals("KECAMATAN") &&
                !spdes.getSelectedItem().toString().equals("DESA") &&
                !spdus.getSelectedItem().toString().equals("DUSUN") &&
                !spjk.getSelectedItem().toString().equals("JENIS KELAMIN");
    }




}