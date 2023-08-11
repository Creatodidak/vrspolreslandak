package id.creatodidak.vrspolreslandak.dashboard.karhutla;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.adapter.KarhutlaPagerAdapter;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.fragment.Kampanye;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.fragment.Penanganan;

public class LaporanKarhutla extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_karhutla);
        ViewPager viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        KarhutlaPagerAdapter adapter = new KarhutlaPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Penanganan(), "PENANGANAN KARHUTLA");
        adapter.addFragment(new Kampanye(), "KAMPANYE CEGAH KARHUTLA");
        viewPager.setAdapter(adapter);
    }
}