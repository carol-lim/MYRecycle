package com.carollim.myrecycleapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CollectionActivity extends AppCompatActivity {

    TextView txtCollectionCategory;
    ImageView imageViewCollection;
    TextInputLayout inputWeight;
    Button submitCollect, cameraBtn;
    String rId, category, status, date, time, addr, collector, user,price, unitPrice, weight, image, pay;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private ArrayList<History> list;
    Double kgPrice;
    private FirebaseAuth fAuth;
    private FirebaseUser firebaseUser;

    /*private static final int REQUEST_TAKE_PHOTO = 1;
    public static final  int GALLERY_REQUEST_CODE = 105;
    public static final  int CAMERA_REQUEST_CODE = 102;
    public static final  int CAMERA_PERM_CODE = 100;
    String currentPhotoPath;
    int imageSize = 224;
    private Uri mImageUri;*/
    private Uri photoURI;
    private Uri contentUri;
    private StorageTask storageTask;
    private StorageReference storageReference;
    public static final String TAG = "CollectionActivity";
    String imageFileName;

    // take photos
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath, downloadedUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        fAuth = FirebaseAuth.getInstance();
        firebaseUser = fAuth.getCurrentUser();


        txtCollectionCategory = findViewById(R.id.txtCollectionCategory);
        imageViewCollection= findViewById(R.id.imageViewCollection);
        inputWeight= findViewById(R.id.inputWeight);
        submitCollect= findViewById(R.id.submitCollect);
        cameraBtn= findViewById(R.id.cameraBtn);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        category = extras.getString("category");
        status = extras.getString("status");
        date = extras.getString("date");
        time = extras.getString("time");
        collector = extras.getString("collector");
        user = extras.getString("user");
        rId = extras.getString("rId");
        addr = extras.getString("address");

        // category textView
        txtCollectionCategory.setText(category);

        switch (category){
            case "Aluminium":
                kgPrice = 3.5;
                break;

            case "Cardboard":
                kgPrice = 0.4;
                break;

            case "Egg Carton":
                kgPrice = 0.63;
                break;

            case "Electronic":
                kgPrice = 5.0;
                break;

            case "Glass":
                kgPrice = 0.1;
                break;

            case "HDPE Plastic":
                kgPrice = 0.8;
                break;

            case "Mix Paper":
                kgPrice = 0.2;
                break;

            case "Newspaper":
                kgPrice = 0.25;
                break;

            case "PET Plastic":
                kgPrice = 0.6;
                break;

            case "Tin Can":
                kgPrice = 0.65;
                break;

        }

        // cloud storage ref
//        storageReference = FirebaseStorage.getInstance().getReference().child("picture");
//        storageReference = FirebaseStorage.getInstance().getReference().child("wasteCollected/"+ UUID.randomUUID().toString());

        // 1
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                // Launch camera if we have permission
                if (ActivityCompat.checkSelfPermission(CollectionActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    /*Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);*/
                    dispatchTakePictureIntent();

                } else {
                    //Request camera permission if we don't have it.
                    ActivityCompat.requestPermissions(CollectionActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });

        // pay


        // 6
        submitCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storageReference = FirebaseStorage.getInstance().getReference().child("wasteCollected").child(imageFileName);

                // validate weight input
                if  (!validateWeight()) {
                    return;
                } else {
                    // upload image
                    if(contentUri != null){
                        storageReference.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                                // get image link
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        downloadedUrl =  uri.toString();
                                        Log.d(TAG, "onSuccess: Uploaded image URL: " +downloadedUrl);

                                        // Image uploaded successfully
                                        Toast.makeText(CollectionActivity.this,"Image Uploaded.",Toast.LENGTH_SHORT).show();

                                        // calculate price based on weight
                                        weight = inputWeight.getEditText().getText().toString();
                                        Double dPrice = kgPrice * Double.parseDouble(weight);
                                        price = df.format(dPrice);
                                        unitPrice = kgPrice.toString();

                                        // store data to history node
                                        History history = new History(rId, category, "Collected", date, time, addr, collector, user, price, unitPrice, weight, downloadedUrl, "TNG");
                                        //saveChart01(history);
                                        saveHistory(history);

                                    }
                                });
                                /*storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        downloadedUrl = task.getResult().toString();
                                        Log.d(TAG, "onComplete: Uploaded image URL: " +downloadedUrl);                                    }
                                });*/
                                /*downloadedUrl = storageReference.getDownloadUrl().toString();
                                Log.d(TAG, "onSuccess: Uploaded image URL: " +downloadedUrl);*/



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e){
                                // Error, Image not uploaded
                                Toast.makeText(CollectionActivity.this,"Image Upload Failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(CollectionActivity.this,"Image is required ",Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }

    // 6.1
    private Boolean validateWeight() {
        String val = inputWeight.getEditText().getText().toString();

        if (val.isEmpty()) {
            inputWeight.setError("Field cannot be empty");
            inputWeight.requestFocus();
            return false;
        } else {
            inputWeight.setError(null);
            return true;
        }
    }

    private void saveHistory(History history){
        FirebaseDatabase database = FirebaseDatabase.getInstance(getResources().getString(R.string.db));
        DatabaseReference ref1 = database.getReference("history");

        ref1.push().setValue(history).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    DatabaseReference ref2 = database.getReference("reservation");
                    ref2.child(rId).child("status").setValue("Collected");

                    Toast.makeText(CollectionActivity.this, "Submitted successfully.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CollectionActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        Toast.makeText(CollectionActivity.this, "Submit Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    /*private void saveChart01(History history){
        FirebaseDatabase database = FirebaseDatabase.getInstance(getResources().getString(R.string.db));
        DatabaseReference ref1 = database.getReference("chart01");
        //String year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        String year = history.getDate().substring(6, 10);
        String month = history.getDate().substring(3, 5);
        //DatabaseReference ref1 = database.getReference("chart01").child(firebaseUser.getUid()).child(year).child(month);

        ChartBar bar = new ChartBar(month, history.price);
        ChartBar bar0 = new ChartBar("0", "0");

        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChild(firebaseUser.getUid())){

                    ref1.child(firebaseUser.getUid()).child(year).child(month).setValue(bar0).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "onComplete: added new path");
                            } else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    Log.d(TAG, "onComplete: fail add new path");
                                }
                            }
                        }
                    });
                }
                ref1.child(firebaseUser.getUid()).child(year).child(month).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Double y1, y2, y;
                        ChartBar eBar = snapshot.getValue(ChartBar.class);
                        y1 = Double.parseDouble(eBar.y);
                        y2 = Double.parseDouble(history.price);

                        y = y1+y2;

                        bar.setY(y.toString());

                        ref1.child(firebaseUser.getUid()).child(year).child(month).setValue(bar).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(CollectionActivity.this, "Bar Chart success.", Toast.LENGTH_SHORT).show();

                                } else {
                                    try {
                                        throw task.getException();
                                    } catch (Exception e) {
                                        Toast.makeText(CollectionActivity.this, "Bar Chart Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }*/

    // 2
    // Take a photo with a camera app
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                // 3
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                // 4
                Uri photoURI = FileProvider.getUriForFile(this,"com.carollim.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE); // open camera
            }
        }
    }

    // 5
    // Get the thumbnail
    // from Doc
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            /*Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageViewCollection.setImageBitmap(imageBitmap);
            */

            File f = new File(currentPhotoPath);
            contentUri = Uri.fromFile(f);
            imageViewCollection.setImageURI(contentUri);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent); // show the image
        }
    }

    // from GfG
    /*private final int PICK_IMAGE_REQUEST = 22;
    //private Uri filePath;
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        super.onActivityResult(requestCode,resultCode,data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            // Get the Uri of data
            photoURI = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),photoURI);
                imageViewCollection.setImageBitmap(bitmap);
            }catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }*/

    // 3
    // Save the full-size photo
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_"; // the name of the image
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",        /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image; // the image File
    }

  /*  private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }*/



}
