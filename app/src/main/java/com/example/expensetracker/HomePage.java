package com.example.expensetracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null){
//             transactionRef = (CollectionReference) db.collection("transactions")
//                    .whereEqualTo("userUid", currentUser.getUid());
//        }

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
    }
}