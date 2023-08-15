package com.example.petal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PlantDetailsActivity extends AppCompatActivity {

    DBHelper db;
    Button rentBTN;
    UserSessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);
        db = new DBHelper(this);
        rentBTN = findViewById(R.id.rentButton);
        session = new UserSessionManager(this);
        Intent intent = getIntent();
        String plantId = intent.getStringExtra("plantId");
        Plant plant = getPlantDetailsFromDatabase(plantId);

        // Set plant details to views
        ImageView plantImageView = findViewById(R.id.plantImageView);
        TextView plantNameTextView = findViewById(R.id.plantNameTextView);
        TextView plantDescTextView = findViewById(R.id.plantDescTextView);
        TextView plantSizeTextView = findViewById(R.id.plantSizeTextView);
        TextView plantSunExposureTextView = findViewById(R.id.plantSunExposureTextView);
        TextView plantSoilTypeTextView = findViewById(R.id.plantSoilTypeTextView);
        TextView plantOwnerTextView = findViewById(R.id.plantOwnerTextView);

        plantImageView.setImageBitmap(plant.getImageBitmap());
        plantNameTextView.setText(plant.getName());
        plantDescTextView.setText(plant.getDescription());
        plantSizeTextView.setText("Size: " + plant.getSize());
        plantSunExposureTextView.setText("Sun Exposure: " + plant.getSunExposure());
        plantSoilTypeTextView.setText("Soil Type: " + plant.getSoilType());
        plantOwnerTextView.setText("Owner: " + plant.getUsername());

        rentBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(db.rentPlant(plantId, session.getUserId())>0){
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(PlantDetailsActivity.this, "Rental Order Placed Successfully!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(PlantDetailsActivity.this, "Rental Order Failed.", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private Plant getPlantDetailsFromDatabase(String plantId) {
        return db.getPlantDetails(plantId);
    }

}