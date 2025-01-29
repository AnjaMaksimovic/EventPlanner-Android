package com.example.eventplanner.fragments.packages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.PriceListAdapter;
import com.example.eventplanner.databinding.FragmentPackagesPricelistBinding;
import com.example.eventplanner.databinding.FragmentProductsPricelistBinding;
import com.example.eventplanner.fragments.products.ProductsPricelistFragment;
import com.example.eventplanner.model.Package;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.pricelist.PriceListItem;
import com.example.eventplanner.repositories.PackageRepository;
import com.example.eventplanner.repositories.ProductRepository;
import com.example.eventplanner.utils.PdfGenerator;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PackagesPricelistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PackagesPricelistFragment extends Fragment {

    private PackageRepository packageRepository;
    private ArrayList<Package> allPackages = new ArrayList<>();;
    private FragmentPackagesPricelistBinding binding;
    private PdfGenerator pdfGenerator;
    private PriceListAdapter adapter;

    public PackagesPricelistFragment() {
    }

    public static PackagesPricelistFragment newInstance() {
        PackagesPricelistFragment fragment = new PackagesPricelistFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPackagesPricelistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        pdfGenerator = new PdfGenerator();

        binding.exportButton.setOnClickListener( v ->{
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "packages_pricelist.pdf";
            ArrayList<PriceListItem> items = new ArrayList<>();
            for (Package aPackage: allPackages){
                PriceListItem item = new PriceListItem(0, aPackage);
                items.add(item);
            }
            pdfGenerator.createPriceListPdf(items, path);
        });

        reloadData();

        return root;
    }

    public void reloadData() {
        packageRepository = new PackageRepository();

        packageRepository.getAllPackages(new PackageRepository.PackageFetchCallback() {
            @Override
            public void onPackageFetch(ArrayList<Package> packages) {
                if (packages != null) {
                    ArrayList<PriceListItem> priceListItems = new ArrayList<>();
                    int orderNumber = 1;
                    for (Package package1 : packages) {
                        if (!package1.isDeleted()) {
                            allPackages.add(package1);
                            priceListItems.add(new PriceListItem(orderNumber++, package1));
                        }
                    }
                    Log.d("ProductsPricelistFragment", "Number of non-deleted packages: " + priceListItems.size());
                    ListView listView = binding.pricelistView;
                    adapter = new PriceListAdapter(requireContext(), priceListItems);
                    listView.setAdapter(adapter);
                }
            }
        });
    }
}