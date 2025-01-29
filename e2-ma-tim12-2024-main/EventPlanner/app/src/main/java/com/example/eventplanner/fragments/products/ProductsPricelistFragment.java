package com.example.eventplanner.fragments.products;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.eventplanner.adapters.PriceListAdapter;
import com.example.eventplanner.databinding.FragmentProductsPricelistBinding;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.pricelist.PriceListItem;
import com.example.eventplanner.repositories.ProductRepository;
import com.example.eventplanner.utils.PdfGenerator;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductsPricelistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsPricelistFragment extends Fragment {
    private ProductRepository productRepository;
    private ArrayList<Product> allProducts = new ArrayList<>();;
    private FragmentProductsPricelistBinding binding;

    private PdfGenerator pdfGenerator;
    private PriceListAdapter adapter;

    public ProductsPricelistFragment() {
    }

    public static ProductsPricelistFragment newInstance() {
        ProductsPricelistFragment fragment = new ProductsPricelistFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductsPricelistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        pdfGenerator = new PdfGenerator();

        binding.exportButton.setOnClickListener( v ->{
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "products_pricelist.pdf";
            ArrayList<PriceListItem> items = new ArrayList<>();
            for (Product product: allProducts){
                PriceListItem item = new PriceListItem(0, product);
                items.add(item);
            }
            pdfGenerator.createPriceListPdf(items, path);
        });
        reloadData();
        return root;
    }

    public void reloadData() {
        productRepository = new ProductRepository();

        productRepository.getAllProducts(new ProductRepository.ProductFetchCallback() {
            @Override
            public void onProductFetch(ArrayList<Product> products) {
                if (products != null) {
                    ArrayList<PriceListItem> priceListItems = new ArrayList<>();
                    int orderNumber = 1;
                    for (Product product : products) {
                        if (!product.isDeleted()) {
                            allProducts.add(product);
                            priceListItems.add(new PriceListItem(orderNumber++, product));
                        }
                    }
                    Log.d("ProductsPricelistFragment", "Number of non-deleted products: " + priceListItems.size());
                    ListView listView = binding.pricelistView;
                    adapter = new PriceListAdapter(requireContext(), priceListItems);
                    listView.setAdapter(adapter);
                }
            }
        });
    }

}