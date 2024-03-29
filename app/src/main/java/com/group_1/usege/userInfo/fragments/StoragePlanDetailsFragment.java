package com.group_1.usege.userInfo.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.group_1.usege.R;
import com.group_1.usege.userInfo.model.StoragePlan;
import com.group_1.usege.userInfo.model.StoragePlanAbility;
import com.group_1.usege.utilities.interfaces.BackSignalReceiver;
import com.group_1.usege.utilities.interfaces.SubmitSignalReceiver;

public class StoragePlanDetailsFragment extends Fragment {

    private ListView listAbilities;
    private TextView txtName;
    private StoragePlanAbilityAdapter adapter;
    private BackSignalReceiver backSignalReceiver;
    private SubmitSignalReceiver submitSignalReceiver;
    private Button btnPurchase;
    private StoragePlan currentPlan;

    public StoragePlanDetailsFragment() {
        super(R.layout.fragment_storage_plan_details);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BackSignalReceiver)
            this.backSignalReceiver = (BackSignalReceiver)context;
        if (context instanceof SubmitSignalReceiver)
            this.submitSignalReceiver = (SubmitSignalReceiver)context;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listAbilities = view.findViewById(R.id.list_abilities);
        adapter = new StoragePlanAbilityAdapter(getContext());
        listAbilities.setAdapter(adapter);
        txtName = view.findViewById(R.id.txt_name);
        ImageButton btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            if (backSignalReceiver != null)
                backSignalReceiver.back();
        });
        btnPurchase = view.findViewById(R.id.btn_purchase);
        btnPurchase.setOnClickListener(v -> {
            if (submitSignalReceiver != null)
                submitSignalReceiver.submit(currentPlan.getName());
        });

    }

    private static class StoragePlanAbilityAdapter extends ArrayAdapter<StoragePlanAbility> {

        public StoragePlanAbilityAdapter(@NonNull Context context) {
            super(context, R.layout.item_plan_ability);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView createdView = (TextView)super.getView(position, convertView, parent);
            StoragePlanAbility plan = getItem(position);
            createdView.setText(plan.getDescription());
            int icon = plan.isCovered() ? R.drawable.ic_check : R.drawable.ic_uncheck;
            createdView.setCompoundDrawablesRelativeWithIntrinsicBounds(icon, 0, 0, 0);
            return createdView;
        }
    }

    public void setPlan(StoragePlan plan)
    {
        if (plan == null)
            return;
        currentPlan = plan;
        txtName.setText(plan.getName());
        btnPurchase.setEnabled(plan.isCanPurchased());
        adapter.clear();
        adapter.addAll(plan.getAbilities());
    }
}