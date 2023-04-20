package com.group_1.usege.userInfo.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.group_1.usege.R;
import com.group_1.usege.library.activities.LibraryActivity;
import com.group_1.usege.userInfo.fragments.StoragePlanDetailsFragment;
import com.group_1.usege.userInfo.fragments.StoragePlanListFragment;
import com.group_1.usege.userInfo.model.StoragePlan;
import com.group_1.usege.userInfo.model.StoragePlanAbility;
import com.group_1.usege.userInfo.model.UserPlan;
import com.group_1.usege.userInfo.repository.UserInfoRepository;
import com.group_1.usege.userInfo.services.MasterServiceGenerator;
import com.group_1.usege.utilities.activities.ActivityUtilities;
import com.group_1.usege.utilities.activities.AuthApiCallerActivity;
import com.group_1.usege.utilities.collection.CollectionUtilities;
import com.group_1.usege.utilities.interfaces.BackSignalReceiver;
import com.group_1.usege.utilities.interfaces.SubmitSignalReceiver;
import com.group_1.usege.utilities.interfaces.ViewDetailsSignalReceiver;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserPlanActivity extends AuthApiCallerActivity<StoragePlan[]> implements BackSignalReceiver, ViewDetailsSignalReceiver, SubmitSignalReceiver {
    Context context = this;
    private StoragePlanListFragment fragPlanList;
    private StoragePlanDetailsFragment fragPlanDetails;
    @Inject
    public MasterServiceGenerator masterServiceGenerator;
    @Inject
    public UserInfoRepository userInfoRepository;
    private FragmentManager fm;

    private StoragePlan[] cachePlan;

    public UserPlanActivity()
    {
        super(R.layout.activity_user_plan);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // handle toggle Menu
        DrawerLayout drawerLayout = findViewById(R.id.root_drawer_layout);
        NavigationView rootNavigationView = findViewById(R.id.root_navigation_view);
        rootNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // Do something when a menu item is clicked
                        Intent intentSettings;
                        switch (item.getItemId()) {
                            case R.id.nav_library:
                                // Handle menu item 1 click
                                ActivityUtilities.TransitActivity((Activity) context, LibraryActivity.class);
                                break;
                            case R.id.nav_external_library:
                                // Handle menu item 2 click
//                                intentSettings = new Intent(LibraryActivity.this, OnlineLibraryActivity.class);
//                                startActivity(intentSettings);
                                break;
                            case R.id.nav_plan:
                                // Handle menu item 2 click
                                ActivityUtilities.TransitActivity((Activity) context, UserPlanActivity.class);
                                break;
                            case R.id.nav_statistic:
                                // Handle menu item 2 click
                                ActivityUtilities.TransitActivity((Activity)context, UserStatisticActivity.class);
                                break;
                            // Add more cases for other menu items as needed
                        }
                        return false;
                    }
                });
        ImageView rootMenuImageView = findViewById(R.id.root_menu_image_view);
        rootMenuImageView.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        fm = getSupportFragmentManager();
        fragPlanList = (StoragePlanListFragment)fm.findFragmentById(R.id.frag_plan_list);
        fragPlanDetails = (StoragePlanDetailsFragment)fm.findFragmentById(R.id.frag_plan_details);
        fm.beginTransaction()
                .show(fragPlanList)
                .hide(fragPlanDetails)
                .commit();
    }

    protected void onResume()
    {
        super.onResume();
//        handleCallSuccess(new StoragePlan[]{
//                new StoragePlan("Basic", 0, false, 0, new StoragePlanAbility[]{
//                        new StoragePlanAbility("A", true),
//                        new StoragePlanAbility("B", true),
//                        new StoragePlanAbility("C", false)
//                }),
//                new StoragePlan("Standard", 103, false, 1, new StoragePlanAbility[]{
//                        new StoragePlanAbility("A", true),
//                        new StoragePlanAbility("B", true),
//                        new StoragePlanAbility("C", true)
//                }),
//                new StoragePlan("Premium", 206, true, 2, new StoragePlanAbility[]{
//                        new StoragePlanAbility("A", true),
//                        new StoragePlanAbility("B", true),
//                        new StoragePlanAbility("C", true)
//                })
//        });
        startCallApi(masterServiceGenerator
                .getService(tokenRepository.getToken().getAccessToken())
                .getUserPlan(tokenRepository.getToken().getUserId()));
    }

    @Override
    protected void handleCallSuccess(StoragePlan[] body) {
        cachePlan = body;
        fragPlanList.setUserPlan(new UserPlan(userInfoRepository.getInfo().getPlan(), body));
//        fragPlanList.setUserPlan(new UserPlan("Standard", body));
    }

    @Override
    public void back() {
        fm.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .show(fragPlanList)
                .hide(fragPlanDetails)
                .commit();
    }

    @Override
    public void view(String id) {
        fm.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .show(fragPlanDetails)
                .hide(fragPlanList)
                .commit();
        StoragePlan plan = CollectionUtilities.find(cachePlan, e -> e.getName().equals(id));
        fragPlanDetails.setPlan(plan);
    }

    @Override
    public void submit(String id) {
        StoragePlan plan = CollectionUtilities.find(cachePlan, e -> e.getName().equals(id));
        Bundle bundle = new Bundle();
        bundle.putString(PaymentActivity.PLAN_NAME_KEY, plan.getName());
        bundle.putFloat(PaymentActivity.PRICE_KEY, plan.getPrice());
        ActivityUtilities.TransitActivity(this, PaymentActivity.class, bundle);
    }
}