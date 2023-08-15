package com.example.petal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ReturnActivity extends AppCompatActivity {
    DBHelper db;
    List<Plant> rentedPlants;
    LinearLayout linearLayout;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        db = new DBHelper(this);
        session = new UserSessionManager(this);
        linearLayout = findViewById(R.id.linearLayout);
        displayRentedPlants();
    }

    private void displayRentedPlants() {
        rentedPlants = db.getPlantsByRenter(session.getUserId());
        linearLayout.removeAllViews();
        for (Plant plant : rentedPlants) {
            View plantItemView = getLayoutInflater().inflate(R.layout.rented_plant, null);

            ImageView plantImage = plantItemView.findViewById(R.id.rentedPlantImg);
            TextView plantName = plantItemView.findViewById(R.id.rentedPlantName);
            TextView plantDesc = plantItemView.findViewById(R.id.rentedPlantDesc);
            TextView plantSize = plantItemView.findViewById(R.id.rentedPlantSize);
            TextView plantSun = plantItemView.findViewById(R.id.rentedPlantSun);
            TextView plantSoil = plantItemView.findViewById(R.id.rentedPlantSoil);
            TextView plantOwner = plantItemView.findViewById(R.id.rentedPlantOwner);
            TextView rentalDate = plantItemView.findViewById(R.id.rentedPlantDate);

            plantImage.setImageBitmap(plant.getImageBitmap());
            plantName.setText(plant.getName());
            plantDesc.setText(plant.getDescription());
            plantSize.setText(plant.getSize());
            plantSun.setText(plant.getSunExposure());
            plantSoil.setText(plant.getSoilType());
            plantOwner.setText("Owner: " + plant.getUsername());
            rentalDate.setText("rented since " + plant.getRentalDate());
            plantItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReturnActivity.this);
                    builder.setTitle("Return Plant");
                    builder.setMessage("You are returning Plant: " + plantName.getText() + ". \nPlease Confirm.");
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean returned = db.returnPlant(plant.getId());
                            if (returned) {
                                Toast.makeText(ReturnActivity.this, "Plant returned successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(ReturnActivity.this, "Failed to return plant.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    builder.show();
                }
            });
            linearLayout.addView(plantItemView);
        }
    }
}