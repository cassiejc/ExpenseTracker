package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class Search extends AppCompatActivity {
    private ListView lvSearch;
    private SearchListAdapter searchAdapter;
    private ArrayList<String> searchDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        lvSearch = findViewById(R.id.lvSearch);
        searchDataList = new ArrayList<>();
        searchAdapter = new SearchListAdapter(this, searchDataList);
        lvSearch.setAdapter(searchAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_search);

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
        Intent i = new Intent(Search.this, HomePage.class);
        startActivity(i);
    }

    private void openSearchPage() {
        Intent intent = new Intent(Search.this, Search.class);
        startActivity(intent);
    }

    private void openTransactionPage() {
        Intent transactionIntent = new Intent(Search.this, Transaction.class);
        startActivity(transactionIntent);
    }

    private void openSpendingPage() {
        Intent intent1 = new Intent(Search.this, Spending.class);
        startActivity(intent1);
    }

    private void openAccountPage(){
        Intent ai = new Intent(Search.this, Account.class);
        startActivity(ai);
    }

    private void fetchDataFromFirestore(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference searchRef = db.collection("transactions");

        searchRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()){
                    String category = document.getString("category");
                    String type = document.getString("type");
                    String amount = document.getString("amount");
                    String note = document.getString("note");

                    // Create a string representing the data you want to display
                    String searchData = "Category: " + category +
                            "\nType: " + type +
                            "\nAmount: " + amount +
                            "\nNote: " + note;

                    searchDataList.add(searchData);
                }
                searchAdapter.notifyDataSetChanged();
            } else {
                // Handle the error if needed
            }
        });
    }

}