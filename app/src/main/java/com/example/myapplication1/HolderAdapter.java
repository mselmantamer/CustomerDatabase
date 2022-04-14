package com.example.myapplication1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.myapplication1.Model.Customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HolderAdapter extends ArrayAdapter<Customer> implements Filterable {

    private final int listItemLayout;
    Context context;
    Database database = new Database(getContext());
    private final OnClickInMyAdapterListener myActivityInterface;
    private ArrayList<Customer> itemList;
    private ArrayList<Customer> itemListFull;

    public HolderAdapter(Context context, int layoutId, ArrayList<Customer> itemList, OnClickInMyAdapterListener myActivityInterface) {
        super(context, layoutId, itemList);
        listItemLayout = layoutId;
        this.context = context;
        this.myActivityInterface = myActivityInterface;
        this.itemList = itemList;
        itemListFull = new ArrayList<>(itemList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Customer item = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(listItemLayout, parent, false);
            viewHolder.tvName = convertView.findViewById(R.id.tvName);
            viewHolder.tvAddress = convertView.findViewById(R.id.tvAddress);
            viewHolder.tvDescription = convertView.findViewById(R.id.tvDescription);
            viewHolder.tvOptionDigit = convertView.findViewById(R.id.tvOptionDigit);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(item.getName());
        viewHolder.tvAddress.setText(item.getAdress());
        viewHolder.tvDescription.setText(item.getDescription());
        viewHolder.tvOptionDigit.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, viewHolder.tvOptionDigit);
            popupMenu.inflate(R.menu.option_menu);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.menu_item_delete:
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setMessage("Do you want to delete this?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", (dialog, which) -> {
                                    final String deleteId = item.getId();
                                    database.DeleteData(deleteId);
                                    this.myActivityInterface.onItemList();
                                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                })
                                .setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                        this.myActivityInterface.customButton(alertDialog);
                        break;
                    case R.id.menu_item_edit:
                        LayoutInflater li = LayoutInflater.from(context);
                        View view = li.inflate(R.layout.dialog_add, null);

                        final EditText etName = view.findViewById(R.id.etName);
                        etName.setText(item.getName());
                        final EditText etAddress = view.findViewById(R.id.etAddress);
                        etAddress.setText(item.getAdress());
                        final EditText etDescription = view.findViewById(R.id.etDescription);
                        etDescription.setText(item.getDescription());

                        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(context);
                        alertDialog2.setView(view);
                        alertDialog2.setMessage("Edit Customer")
                                .setCancelable(false)
                                .setPositiveButton("EDIT", (dialog, which) -> {
                                    String textName = etName.getText().toString();
                                    String textAddress = etAddress.getText().toString();
                                    String textDescription = etDescription.getText().toString();
                                    Customer cst = new Customer(item.getId(), textName, textAddress, textDescription);
                                    database.EditData(cst);
                                    this.myActivityInterface.onItemList();
                                })
                                .setNegativeButton("IGNORE", (dialog, which) -> dialog.dismiss());
                        this.myActivityInterface.customButton(alertDialog2);
                        break;
                }
                return false;
            });
            popupMenu.show();
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView tvName, tvAddress, tvDescription, tvOptionDigit;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private final Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Customer> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(itemListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Customer c : itemListFull) {
                    if (c.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(c);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            itemList.clear();
            itemList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
