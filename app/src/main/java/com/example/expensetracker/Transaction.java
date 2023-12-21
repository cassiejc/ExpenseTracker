package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Transaction extends AppCompatActivity {

    private Spinner SpinnerCategories, SpinnerExpense;
    private Button CancelButton, SaveButton;
    private EditText etTransaction, etNote;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        SpinnerCategories = findViewById(R.id.spinner_categories);
        SpinnerExpense = findViewById(R.id.expense_spinner);
        CancelButton = findViewById(R.id.cancel_button);
        SaveButton = findViewById(R.id.save_button);
        etTransaction = findViewById(R.id.ettransaction);
        etNote = findViewById(R.id.note_edittext);

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        String[] categories = {"food", "pet", "shopping", "others"};
        String[] expense = {"Expense", "Income"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<String> expenseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, expense);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerCategories.setAdapter(adapter);
        SpinnerExpense.setAdapter(expenseAdapter);

        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Transaction.this, HomePage.class);
                startActivity(i);
            }
        });

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredNumber = etTransaction.getText().toString();
                String selectedExpense = SpinnerExpense.getSelectedItem().toString();
                String selectedCategory = SpinnerCategories.getSelectedItem().toString();
                String note = etNote.getText().toString();

                db.collection("transactions")
                        .add(createTransactionData(enteredNumber, selectedCategory, selectedExpense, note))
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(Transaction.this, "Transaction saved successfully!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Transaction.this, HomePage.class);
                            i.putExtra("enteredNumber", enteredNumber);
                            i.putExtra("selectedCategory", selectedCategory);
                            i.putExtra("expenseType", selectedExpense);
                            i.putExtra("note", note);

                            startActivity(i);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(Transaction.this, "Error saving transaction: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("FirestoreError", "Error saving transaction", e);
                        });
            }
        });
    }

    private Map<String, Object> createTransactionData(String amount, String category, String type, String note) {
        Map<String, Object> data = new HashMap<>();
        data.put("amount", amount);
        data.put("category", category);
        data.put("type", type);
        data.put("note", note);

//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null){
//            data.put("userUid", currentUser.getUid());
//        }
//
        return data;
    }
}