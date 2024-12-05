package com.example.androidapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pizzeria_package.Topping;

public class ToppingAdapter extends RecyclerView.Adapter<ToppingAdapter.ToppingViewHolder> {
    private final Context context;
    private List<Topping> toppings;
    private final int maxToppings;
    private final ToppingSelectionListener listener;

    private final Set<Topping> selectedToppings = new HashSet<>();
    private boolean isEditable = true;

    public ToppingAdapter(Context context, List<Topping> toppings, int maxToppings, ToppingSelectionListener listener) {
        this.context = context;
        this.toppings = toppings;
        this.maxToppings = maxToppings;
        this.listener = listener;
    }

    /**
     * Sets the toppings and whether they are editable.
     *
     * @param toppings   The list of toppings to display.
     * @param isEditable True if the toppings can be customized; false otherwise.
     */
    public void setToppings(List<Topping> toppings, boolean isEditable) {
        this.toppings = toppings;
        this.isEditable = isEditable;

        if (!isEditable) {
            selectedToppings.clear();
            selectedToppings.addAll(toppings);
        } else {
            selectedToppings.clear();
        }

        notifyDataSetChanged();
    }

    /**
     * Retrieves the currently selected toppings.
     *
     * @return A set of selected toppings.
     */
    public Set<Topping> getSelectedToppings() {
        return new HashSet<>(selectedToppings);
    }

    @NonNull
    @Override
    public ToppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.topping_item, parent, false);
        return new ToppingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToppingViewHolder holder, int position) {
        Topping topping = toppings.get(position);

        holder.toppingName.setText(topping.toString());
        holder.toppingImage.setImageResource(topping.getImageResourceId());

        holder.toppingCheckBox.setOnCheckedChangeListener(null);

        boolean isSelected = selectedToppings.contains(topping);
        holder.toppingCheckBox.setChecked(isSelected);

        holder.toppingCheckBox.setEnabled(isEditable && (isSelected || selectedToppings.size() < maxToppings));

        holder.toppingCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isEditable) {
                if (isChecked) {
                    if (selectedToppings.size() < maxToppings) {
                        selectedToppings.add(topping);
                    } else {
                        buttonView.setChecked(false);
                        Toast.makeText(context, "You can only select up to " + maxToppings + " toppings.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    selectedToppings.remove(topping);
                }

                listener.onToppingSelected(selectedToppings.size());
                notifyDataSetChanged();
            } else {
                buttonView.setChecked(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return toppings.size();
    }

    /**
     * Resets all topping selections.
     */
    public void resetSelection() {
        selectedToppings.clear();
        notifyDataSetChanged();
    }

    static class ToppingViewHolder extends RecyclerView.ViewHolder {
        TextView toppingName;
        ImageView toppingImage;
        CheckBox toppingCheckBox;

        public ToppingViewHolder(@NonNull View itemView) {
            super(itemView);
            toppingName = itemView.findViewById(R.id.toppingName);
            toppingImage = itemView.findViewById(R.id.toppingImage);
            toppingCheckBox = itemView.findViewById(R.id.toppingCheckBox);
        }
    }

    /**
     * Interface for topping selection listener.
     */
    public interface ToppingSelectionListener {
        void onToppingSelected(int count);
    }
}
