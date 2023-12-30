package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class Spending extends AppCompatActivity {
    private ListView lvPercentage;
    private Map<String, Double> categoryExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending);

        lvPercentage = findViewById(R.id.lvCategoryPercentages);
        categoryExpenses = new HashMap<>();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_spending);

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
                        }

                        if (selectedFragment != null) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, selectedFragment)
                                    .commit();
                        }

                        return true;
                    }
                });

        fetchDataFromFirestore();
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

    private void fetchDataFromFirestore(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference expensesRef = db.collection("transactions");

        expensesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                double totalExpense = 0;

                for (QueryDocumentSnapshot document : task.getResult()){
                    String category = document.getString("category");
                    String amountString = document.getString("amount");

                    if (amountString != null){
                        double amount = Double.parseDouble(amountString);
                        totalExpense += amount;

                        if (categoryExpenses.containsKey(category)){
                            categoryExpenses.put(category, categoryExpenses.get(category) + amount);
                        } else {
                            categoryExpenses.put(category, amount);
                        }
                    }
                }

                calculateAndDisplayCategoryPercentages(totalExpense);
            } else {

            }
        });
    }

    private void calculateAndDisplayCategoryPercentages(double totalExpense) {
        if (totalExpense == 0 || categoryExpenses.isEmpty()) {
            // Handle the case where no data is available
            return;
        }

        // Create a list of strings to display in ListView
        String[] categoryPercentageStrings = new String[categoryExpenses.size()];
        int index = 0;

        for (Map.Entry<String, Double> entry : categoryExpenses.entrySet()) {
            String category = entry.getKey();
            double categoryExpense = entry.getValue();
            double percentage = (categoryExpense / totalExpense) * 100;

            categoryPercentageStrings[index] = String.format("%s: %.2f%%", category, percentage);
            index++;
        }

        // Use an ArrayAdapter to display the data in ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryPercentageStrings);
        lvPercentage.setAdapter(adapter);
    }}