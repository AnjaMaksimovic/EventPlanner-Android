package com.example.eventplanner.fragments.services;

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
import com.example.eventplanner.databinding.FragmentServicesPricelistBinding;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.pricelist.PriceListItem;
import com.example.eventplanner.repositories.ServiceRepository;
import com.example.eventplanner.utils.PdfGenerator;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServicesPricelistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServicesPricelistFragment extends Fragment {
    private ServiceRepository serviceRepository;
    private ArrayList<Service> allServices = new ArrayList<>();;
    private FragmentServicesPricelistBinding binding;

    private PdfGenerator pdfGenerator;

    public ServicesPricelistFragment() {
    }

    public static ServicesPricelistFragment newInstance() {
        ServicesPricelistFragment fragment = new ServicesPricelistFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentServicesPricelistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        pdfGenerator = new PdfGenerator();

        binding.exportButton.setOnClickListener( v ->{
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "services_pricelist.pdf";
            ArrayList<PriceListItem> items = new ArrayList<>();
            for (Service service: allServices){
                PriceListItem item = new PriceListItem(0, service);
                items.add(item);
            }
            pdfGenerator.createPriceListPdf(items, path);
        });
        reloadData();

        return root;
    }

    public void reloadData() {
        serviceRepository = new ServiceRepository();

        serviceRepository.getAllServices(new ServiceRepository.ServiceFetchCallback() {
            @Override
            public void onServiceFetch(ArrayList<Service> services) {
                if (services != null) {
                    ArrayList<PriceListItem> priceListItems = new ArrayList<>();
                    int orderNumber = 1;
                    for (Service service : services) {
                        if (!service.isDeleted()) {
                            allServices.add(service);
                            priceListItems.add(new PriceListItem(orderNumber++, service));
                        }
                    }
                    Log.d("ServicesPricelistFragment", "Number of non-deleted service: " + priceListItems.size());
                    ListView listView = binding.pricelistView;
                    PriceListAdapter adapter = new PriceListAdapter(requireContext(), priceListItems);
                    listView.setAdapter(adapter);
                }
            }
        });
    }

}