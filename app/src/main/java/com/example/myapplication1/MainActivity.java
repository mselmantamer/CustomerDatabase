package com.example.myapplication1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication1.Model.Customer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnClickInMyAdapterListener {

    Toolbar mToolbar;

    FloatingActionButton fab;
    ListView listView;
    Context context = this;
    Database database = new Database(context);
    ArrayList<Customer> itemList;
    HolderAdapter itemArrayAdapter;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        //Color of Toolbar Title
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorThird));

        listView = findViewById(R.id.listView);
        imageView = findViewById(R.id.imageView);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Customer cst = itemList.get(i);

            LayoutInflater li = LayoutInflater.from(context);
            View view2 = li.inflate(R.layout.dialog_show, null);
            final TextView tvName = view2.findViewById(R.id.tvName);
            final TextView tvAddress = view2.findViewById(R.id.tvAddress);
            final TextView tvDescription = view2.findViewById(R.id.tvDescription);

            tvName.setText(cst.getName());
            tvAddress.setText(cst.getAdress());
            tvDescription.setText(cst.getDescription());

            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(context);
            alertDialog2.setView(view2);
            alertDialog2.setMessage("Customer Info")
                    .setCancelable(false)
                    .setPositiveButton("Back", (dialog, which) -> {
                        dialog.dismiss();
                    });

            CustomButtonDialog(alertDialog2);
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            LayoutInflater li = LayoutInflater.from(context);
            View view = li.inflate(R.layout.dialog_add, null);
            final EditText etName = view.findViewById(R.id.etName);
            final EditText etAddress = view.findViewById(R.id.etAddress);
            final EditText etDescription = view.findViewById(R.id.etDescription);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setView(view);
            alertDialog.setMessage("Add Customer")
                    .setCancelable(false)
                    .setPositiveButton("Add", (dialog, which) -> {
                        String textName = etName.getText().toString();
                        String textAddress = etAddress.getText().toString();
                        String textDescripition = etDescription.getText().toString();
                        Customer cst = new Customer(textName, textAddress, textDescripition);
                        database.AddData(cst);
                        List();
                    })
                    .setNegativeButton("Ignore", (dialog, which) -> dialog.dismiss());

            CustomButtonDialog(alertDialog);
        });

        List();
    }

    public void CustomButtonDialog(AlertDialog.Builder alertDialog) {
        AlertDialog dialog = alertDialog.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorThird));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorThird));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Here");

        searchView.setDrawingCacheBackgroundColor(getResources().getColor(android.R.color.white));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                itemArrayAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_help:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setMessage("This is an example help dialog")
                        .setCancelable(false)
                        .setPositiveButton("Back", (dialog, which) -> {
                            dialog.dismiss();
                        });
                CustomButtonDialog(alertDialog);
                break;
            case R.id.app_bar_aboutus:
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(context);
                alertDialog2.setMessage("Selman ® 2021")
                        .setCancelable(false)
                        .setPositiveButton("Back", (dialog, which) -> {
                            dialog.dismiss();
                        });
                CustomButtonDialog(alertDialog2);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void List() {
        itemList = database.ListData();
        itemArrayAdapter = new HolderAdapter(context, R.layout.card_row, itemList, this);
        listView.setAdapter(itemArrayAdapter);

        if (itemArrayAdapter.getCount() == 0) imageView.setVisibility(View.VISIBLE);
        else imageView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemList() {
        List();
    }

    @Override
    public void customButton(AlertDialog.Builder alertDialog) {
        CustomButtonDialog(alertDialog);
    }

}