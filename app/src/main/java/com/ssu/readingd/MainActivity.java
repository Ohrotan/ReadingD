package com.ssu.readingd;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //바코드 감지기 구성
        FirebaseVisionBarcodeDetectorOptions options =
                new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(
                                FirebaseVisionBarcode.FORMAT_QR_CODE,
                                FirebaseVisionBarcode.FORMAT_AZTEC)
                        .build();

        //바코드 감지기 실행
         class YourAnalyzer implements ImageAnalysis.Analyzer {

            private int degreesToFirebaseRotation(int degrees) {
                switch (degrees) {
                    case 0:
                        return FirebaseVisionImageMetadata.ROTATION_0;
                    case 90:
                        return FirebaseVisionImageMetadata.ROTATION_90;
                    case 180:
                        return FirebaseVisionImageMetadata.ROTATION_180;
                    case 270:
                        return FirebaseVisionImageMetadata.ROTATION_270;
                    default:
                        throw new IllegalArgumentException(
                                "Rotation must be 0, 90, 180, or 270.");
                }
            }

            @Override
            public void analyze(ImageProxy imageProxy, int degrees) {
                if (imageProxy == null || imageProxy.getImage() == null) {
                    return;
                }
                Image mediaImage = imageProxy.getImage();
                int rotation = degreesToFirebaseRotation(degrees);
                FirebaseVisionImage image =
                        FirebaseVisionImage.fromMediaImage(mediaImage, rotation);
                // Pass image to an ML Kit Vision API
                // ...
                FirebaseVisionImage image2;
                try {
                    Context context =null;
                    Uri uri = null;
                    image2 = FirebaseVisionImage.fromFilePath(context, uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


    }
}
