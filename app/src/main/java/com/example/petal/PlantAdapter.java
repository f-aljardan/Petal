package com.example.petal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {

    private List<Plant> plantList;
    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public PlantAdapter(List<Plant> plantList) {
        this.plantList = plantList;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plant, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        // Bind the data to the views
        Plant plant = plantList.get(position);
        holder.bind(plant);
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    public class PlantViewHolder extends RecyclerView.ViewHolder {

        private ImageView tvPlantImage;
        private TextView tvPlantName;
        private TextView tvPlantDescription;
        private TextView tvPlantSize;
        private TextView tvPlantSunExposure;
        private TextView tvPlantSoilType;


        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPlantImage = itemView.findViewById(R.id.dplantImageView);
            tvPlantName = itemView.findViewById(R.id.dplantNameTextView);
            tvPlantDescription = itemView.findViewById(R.id.dplantDescTextView);
            tvPlantSize = itemView.findViewById(R.id.dplantSizeTextView);
            tvPlantSunExposure = itemView.findViewById(R.id.dplantSunTextView);
            tvPlantSoilType = itemView.findViewById(R.id.dplantSoilTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            clickListener.onItemClick(position);
                        }
                    }
                }
            });
        }

        public void bind(Plant plant) {
            // Bind the plant data to the views
            tvPlantImage.setImageBitmap(plant.getImageBitmap());
            tvPlantName.setText(plant.getName());
            tvPlantDescription.setText(plant.getDescription());
            tvPlantSize.setText(plant.getSize());
            tvPlantSunExposure.setText(plant.getSunExposure());
            tvPlantSoilType.setText(plant.getSoilType());

        }
    }
}
