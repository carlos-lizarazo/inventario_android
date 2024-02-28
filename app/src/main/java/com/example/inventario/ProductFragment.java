package com.example.inventario;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyTextView;
    private ProductAdapter adapter;

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        emptyTextView = view.findViewById(R.id.emptyTextView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Llamar al método para obtener la lista de productos
        getProducts();

        return view;
    }

    private void getProducts() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Product>> call = apiService.getProducts();
        Log.i("ProductFragment.getProducts", "Ingreso");
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                Log.i("ProductFragment.getProducts", "OnResponse:" + response.message());
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.i("ProductFragment.getProducts", "isSuccessful");
                    List<Product> productList = response.body();
                    Log.i("ProductFragment.getProducts", "" + (productList != null?  productList.size() : "null"));
                    if (productList != null && productList.size() > 0) {
                        Log.i("ProductFragment.getProducts", "adapter");
                        adapter = new ProductAdapter(getContext(), productList);
                        Log.i("ProductFragment.getProducts", "adapter2");
                        recyclerView.setAdapter(adapter);
                        Log.i("ProductFragment.getProducts", "adapter3");
                    } else {
                        Log.i("ProductFragment.getProducts", "visible");
                        emptyTextView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.i("ProductFragment.getProducts", "else");
                    Toast.makeText(getContext(), "Error al obtener la lista de productos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
