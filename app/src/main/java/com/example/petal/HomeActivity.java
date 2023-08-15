package com.example.petal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    Button addButton;
    Button deleteButton;
    Button btn;
    Button returnBTN;
    TextView welcomeTextView;
    UserSessionManager session;
    DBHelper db;
    private LinearLayout plantLayout;

    private void displayPlantItems() {
        ArrayList<Plant> plants = db.getPlants();
        plantLayout.removeAllViews();
        for (Plant plant : plants) {
            View plantItemView = getLayoutInflater().inflate(R.layout.plant_item, null);

            ImageView plantImageView = plantItemView.findViewById(R.id.plantImageView);
            TextView plantNameTextView = plantItemView.findViewById(R.id.plantNameTextView);
            TextView plantDescTextView = plantItemView.findViewById(R.id.plantDescTextView);

            plantImageView.setImageBitmap(plant.getImageBitmap());
            plantNameTextView.setText(plant.getName());
            plantDescTextView.setText(plant.getDescription());
            plantItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, PlantDetailsActivity.class);
                    System.out.println(plant.getId());
                    intent.putExtra("plantId", Integer.toString(plant.getId()));
                    context.startActivity(intent);
                }
            });
            plantLayout.addView(plantItemView);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayPlantItems();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        session = new UserSessionManager(this);
        db = new DBHelper(this);
        setContentView(R.layout.activity_home);
        returnBTN = findViewById(R.id.returnButton);
        plantLayout = findViewById(R.id.plantLayout);
        addButton = findViewById(R.id.buttonAdd);
        btn = findViewById(R.id.logout);
        welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText("Welcome " + session.getUserId());
        displayPlantItems();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddPlant.class);
                startActivity(intent);
            }

        });

        deleteButton = findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlantListActivity.class);
                startActivity(intent);

            }

        });

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Log Out");
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        session.clearSession();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        returnBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReturnActivity.class);
                startActivity(intent);
            }
        });


    }

}