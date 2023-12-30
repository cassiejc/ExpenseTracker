package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    private Button AddButton, btnLogout;
    private TextView TvTotal, tvIncome, tvExpense;
    private ListView lvTransaction;
    private ArrayList<String> transactionList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        AddButton = findViewById(R.id.addbutton);
        TvTotal = findViewById(R.id.tvtotal);
        lvTransaction = findViewById(R.id.lvTransaction);
        tvIncome = findViewById(R.id.tvIncome);
        tvExpense = findViewById(R.id.tvExpense);
        btnLogout = findViewById(R.id.btnLogout);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference transactionRef = db.collection("transactions");

        transactionList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, transactionList);
        lvTransaction.setAdapter(adapter);

        final double[] totalIncome = {0};
        final double totalExpense[] = {0};

        transactionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                double totalAmount = 0;

                for (QueryDocumentSnapshot document : task.getResult()){
                    String amount = document.getString("amount");
                    double amountValue = Double.parseDouble(amount);
                    totalAmount += amountValue;

                    String type = document.getString("type");
                    if ("Income".equals(type)) {
                        totalIncome[0] += amountValue;
                    } else if ("Expense".equals(type)) {
                        totalExpense[0] += amountValue;
                    }

                    String category = document.getString("category");
                    String note = document.getString("note");

                    transactionList.add("Category: " + category + " - " + type + " : Rp " + amount + "\nNote: " + note);
                }

                tvIncome.setText("Income : Rp " + (int) totalIncome[0]);
                tvExpense.setText("Expense : Rp " + (int) totalExpense[0]);

                totalAmount = totalIncome[0] - totalExpense[0];
                TvTotal.setText("Rp  " + (int) totalAmount);

                adapter.notifyDataSetChanged();
            } else {
                Log.e("FirestoreError", "Error getting transactions", task.getException());
            }
        });

        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomePage.this, Transaction.class);
                startActivity(i);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent i = new Intent(HomePage.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
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
        Intent i = new Intent(HomePage.this, HomePage.class);
        startActivity(i);
    }

    private void openSearchPage() {
        Intent intent = new Intent(HomePage.this, Search.class);
        startActivity(intent);
    }

    private void openTransactionPage() {
        Intent transactionIntent = new Intent(HomePage.this, Transaction.class);
        startActivity(transactionIntent);
    }

    private void openSpendingPage() {
        Intent intent1 = new Intent(HomePage.this, Spending.class);
        startActivity(intent1);
    }

    private void openAccountPage(){
        Intent ai = new Intent(HomePage.this, Account.class);
        startActivity(ai);
    }
    }