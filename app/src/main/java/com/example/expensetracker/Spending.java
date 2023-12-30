package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Spending extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_spending);

//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;

                        if (item.getItemId() == R.id.menu_home) {
                            openHomePage();
                            return true;
                        } else if (item.getItemId() == R.id.menu_search) {
                            openSearchPage();
                            return true;
                        } else if (item.getItemId() == R.id.menu_add) {
                            openTransactionPage();
                            return true;
                        } else if (item.getItemId() == R.id.menu_spending) {
                            openSpendingPage();
                            return true;
                        } else if (item.getItemId() == R.id.menu_account) {
                            openAccountPage();
//                            selectedFragment = new Account();
                        }

                        if (selectedFragment != null) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, selectedFragment)
                                    .commit();
                        }

                        return true;
                    }
                });
    }

    private void openHomePage() {
        Intent i = new Intent(Spending.this, HomePage.class);
        startActivity(i);
    }

    private void openSearchPage() {
        Intent intent = new Intent(Spending.this, Search.class);
        startActivity(intent);
    }

    private void openTransactionPage() {
        Intent transactionIntent = new Intent(Spending.this, Transaction.class);
        startActivity(transactionIntent);
    }

    private void openSpendingPage() {
        Intent intent1 = new Intent(Spending.this, Spending.class);
        startActivity(intent1);
    }

    private void openAccountPage(){
        Intent ai = new Intent(Spending.this, Account.class);
        startActivity(ai);
    }
}