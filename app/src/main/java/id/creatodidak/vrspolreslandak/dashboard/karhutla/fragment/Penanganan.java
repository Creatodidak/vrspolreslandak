package id.creatodidak.vrspolreslandak.dashboard.karhutla.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.adapter.AnimalAdapter;
import id.creatodidak.vrspolreslandak.api.models.Animal;

public class Penanganan extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_penanganan, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rvPenangananKarhutla); // Corrected this line
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Corrected this line

        List<Animal> animalList = new ArrayList<>();
        animalList.add(new Animal("Kucing"));
        animalList.add(new Animal("Anjing"));
        animalList.add(new Animal("Burung"));
        animalList.add(new Animal("Kuda"));
        animalList.add(new Animal("Gajah"));
        animalList.add(new Animal("Kucing"));
        animalList.add(new Animal("Anjing"));
        animalList.add(new Animal("Burung"));
        animalList.add(new Animal("Kuda"));
        animalList.add(new Animal("Gajah"));
        animalList.add(new Animal("Kucing"));
        animalList.add(new Animal("Anjing"));
        animalList.add(new Animal("Burung"));
        animalList.add(new Animal("Kuda"));
        animalList.add(new Animal("Gajah"));

        AnimalAdapter animalAdapter = new AnimalAdapter(animalList);
        recyclerView.setAdapter(animalAdapter);
        return view;
    }
}
