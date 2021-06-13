package pl.edu.pwr.i236764.imagerecognition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final int REQUEST_GALLERY = 1;
    static final int REQUEST_IMAGE = 0;
    static final int CAMERA_PERM = 2;
    LinearLayout cameraLayout, detectLayout, detectLabelLayout, galleryLayout;
    ImageView imgView;
    TextView textArea;
    Bitmap galleryImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        detectLayout = findViewById(R.id.detect);
        detectLabelLayout = findViewById(R.id.detectLabel);
        cameraLayout = findViewById(R.id.camera);
        galleryLayout = findViewById(R.id.gallery);

        imgView = findViewById(R.id.image);
        textArea = findViewById(R.id.text);
        textArea.setMovementMethod(new ScrollingMovementMethod());

        detectLabelLayout.setOnClickListener(this);
        detectLayout.setOnClickListener(this);
        cameraLayout.setOnClickListener(this);
        galleryLayout.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            dispatchTakePictureIntent();
        }
        else{
            Toast.makeText(this, "Camera is unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.detect:
                try {
                    run("text");
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "No changes detected", Toast.LENGTH_SHORT).show();
                }
            case R.id.detectLabel:
                try {
                    run("label");
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "Can't identify", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.camera:
                askCameraPermissions();
                break;
            case R.id.gallery:
                galleryAddPic();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_IMAGE:
                if (resultCode == RESULT_OK){
                    Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                    imgView.setImageBitmap(selectedImage);
                    textArea.setText("");
                }
                break;
            case REQUEST_GALLERY:
                try {
                    Uri imageUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    galleryImage = BitmapFactory.decodeStream(imageStream);
                    imgView.setImageBitmap(galleryImage);
                    textArea.setText("");
                }catch (Exception e){
                    Toast.makeText(this, "Image Failed to Retrieve from Gallery",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE);
    }

    private void askCameraPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA
            },CAMERA_PERM);
        }
        else {
            dispatchTakePictureIntent();
        }
    }

    private void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(mediaScanIntent, REQUEST_GALLERY);
    }

    private void run(String action){
        int rotationDegree = 0;
        Bitmap bitmap = ((BitmapDrawable) imgView.getDrawable()).getBitmap();
        InputImage image = InputImage.fromBitmap(bitmap, rotationDegree);
        if ("text".equals(action)) {
            TextRecognizer recognizer = TextRecognition.getClient();

            Task<Text> result =
                    recognizer.process(image)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                @Override
                                public void onSuccess(Text visionText) {
                                    processTextRecognition(visionText);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Failed Transaction", Toast.LENGTH_SHORT).show();
                                }
                            });
        }
        if("label".equals(action)){
            ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);

            labeler.process(image)
                    .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                        @Override
                        public void onSuccess(List<ImageLabel> labels) {
                            proceedImageLabeling(labels);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "No Object Detected in Image",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void processTextRecognition(Text visionText) {
        List<Text.TextBlock> blocks = visionText.getTextBlocks();
        if (blocks.size() == 0){
            Toast.makeText(MainActivity.this, "No Text Detected in Image",Toast.LENGTH_LONG).show();
        }
        textArea.setText("");
        StringBuilder text = new StringBuilder();

        for (int i = 0; i<blocks.size();i++){
            List<Text.Line> lines = blocks.get(i).getLines();
            for (int j = 0; j<lines.size();j++){
                List<Text.Element> elements = lines.get(j).getElements();
                for (int k = 0; k<elements.size();k++){
                    text.append(elements.get(k).getText() + " ");
                }
            }
        }
        textArea.setText(text);
    }

    private void proceedImageLabeling(List<ImageLabel> labels) {
        textArea.setText("");
        StringBuilder textBuilder = new StringBuilder();
        for (ImageLabel label : labels) {
            String text = label.getText();
            float confidence = label.getConfidence();
            float confidence2 = confidence * 100;
            String strConfidence = String.format("%.2f", confidence2);
            textBuilder.append(text + " - %"+strConfidence +"\n");
        }
        textArea.setText(textBuilder);
    }

}