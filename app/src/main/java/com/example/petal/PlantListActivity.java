package com.example.petal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PlantListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlantAdapter plantAdapter;
    private List<Plant> plantList;
    private DBHelper db;
    private String plantName;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_list);

        recyclerView = findViewById(R.id.recyclerViewPlantList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        plantList = new ArrayList<>();
        db = new DBHelper(this);
        session = new UserSessionManager(this);


        // Get the plants for the current user
        String owner = session.getUserId(); // Replace with the actual owner username
        plantList = db.getPlantsByOwner(owner);

        // Set up the adapter and attach it to the RecyclerView
        plantAdapter = new PlantAdapter(plantList);
        recyclerView.setAdapter(plantAdapter);

        plantAdapter.setOnItemClickListener(new PlantAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle plant item click event
                Plant plant = plantList.get(position);
                plantName = plant.getName();
                deletePlant(plant.getId());
            }
        });


    }

    private void deletePlant(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Plant");
        builder.setMessage("Are you sure you want to delete " + plantName + "?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean deleted = db.deletePlant(id);
                if (deleted) {
                    plantList.removeIf(p -> p.getId() == id);
                    plantAdapter.notifyDataSetChanged();
                    Toast.makeText(PlantListActivity.this, "Plant deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PlantListActivity.this, "Failed to delete plant", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
