package com.carollim.myrecycleapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class CatalogDetailActivity extends AppCompatActivity {

    ImageView imageViewDetailCategory;
    TextView txtCatalogDetailCategoryName, txtCatalogDetailUnitPrice, txtCatalogDetailDesc;
    public static final String TAG = "CatalogDetailActivity";
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog_detail);
        Log.d(TAG, "onCreate: called.");

        imageViewDetailCategory = findViewById(R.id.imageViewDetailCategory);
        txtCatalogDetailCategoryName = findViewById(R.id.txtCatalogDetailCategoryName);
        txtCatalogDetailUnitPrice = findViewById(R.id.txtCatalogDetailUnitPrice);
        txtCatalogDetailDesc = findViewById(R.id.txtCatalogDetailDesc);

        String category, image, desc;
        Double kgPrice;
        Bundle extras = getIntent().getExtras();

        category = extras.getString("category");
        image = extras.getString("image");
        kgPrice = extras.getDouble("kgPrice");
        desc = extras.getString("desc");


        txtCatalogDetailCategoryName.setText(category);
        Picasso.get().load(image).into(imageViewDetailCategory);
        txtCatalogDetailUnitPrice.setText("RM"+ df.format(kgPrice) +" / kg");
        txtCatalogDetailDesc.setText(desc);



    }


}