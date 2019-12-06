package com.ssu.readingd.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ssu.readingd.BookManualRegisterActivity;
import com.ssu.readingd.common.FrameMetadata;
import com.ssu.readingd.common.GraphicOverlay;
import com.ssu.readingd.dto.BookSimpleDTO;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Barcode Detector Demo.
 */
public class BarcodeScanningProcessor extends VisionProcessorBase<List<FirebaseVisionBarcode>> {
    private final String SEARCH_URL = "http://seoji.nl.go.kr/landingPage/SearchApi.do";
    private final String API_KEY = "?cert_key=c594fa83326be40164ae013ab0a14ad8";
    private final String RESULT_STYLE = "&result_style=json";
    private String PAGE_NO = "&page_no=1";
    private String PAGE_SIZE = "&page_size=100";
    private String ISBN = "&isbn=";
    private String TITLE = "&title=";
    private String PUBLISHER = "&publisher=";
    private String AUTHOR = "&author=";
    private String SORT = "&sort=";
    private String REQUEST_URL;

    private static final String TAG = "BarcodeScanProc";

    private final FirebaseVisionBarcodeDetector detector;
    StillImageActivity activity;

    public BarcodeScanningProcessor() {
        // Note that if you know which format of barcode your app is dealing with, detection will be
        // faster to specify the supported barcode formats one by one, e.g.
        // new FirebaseVisionBarcodeDetectorOptions.Builder()
        //     .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
        //     .build();
        detector = FirebaseVision.getInstance().getVisionBarcodeDetector();
    }

    public BarcodeScanningProcessor(StillImageActivity activity) {
        // Note that if you know which format of barcode your app is dealing with, detection will be
        // faster to specify the supported barcode formats one by one, e.g.
        // new FirebaseVisionBarcodeDetectorOptions.Builder()
        //     .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
        //     .build();
        this.activity = activity;
        detector = FirebaseVision.getInstance().getVisionBarcodeDetector();
    }


    @Override
    public void stop() {
        try {
            detector.close();
        } catch (IOException e) {
            Log.e(TAG, "Exception thrown while trying to close Barcode Detector: " + e);
        }
    }

    @Override
    protected Task<List<FirebaseVisionBarcode>> detectInImage(FirebaseVisionImage image) {
        return detector.detectInImage(image);
    }


    @Override
    protected void onSuccess(
            @Nullable Bitmap originalCameraImage,
            @NonNull List<FirebaseVisionBarcode> barcodes,
            @NonNull FrameMetadata frameMetadata,
            @NonNull GraphicOverlay graphicOverlay) {
        /*graphicOverlay.clear();
        if (originalCameraImage != null) {
            CameraImageGraphic imageGraphic = new CameraImageGraphic(graphicOverlay, originalCameraImage);
            graphicOverlay.add(imageGraphic);
        }*/
        for (int i = 0; i < barcodes.size(); ++i) {
            FirebaseVisionBarcode barcode = barcodes.get(i);
            Log.v("barcode", barcode.getRawValue());

            REQUEST_URL = SEARCH_URL + API_KEY + RESULT_STYLE + PAGE_NO + PAGE_SIZE + ISBN
                    + barcode.getRawValue() + TITLE + PUBLISHER + AUTHOR + SORT;
            BookAPITask a = new BookAPITask(REQUEST_URL);
            JsonObject jo = a.doInBackground();
            Log.v("barcode", jo.toString());
            JsonArray ja = jo.get("docs").getAsJsonArray();
            if (ja.size() > 0) {
                BookSimpleDTO book = BookSimpleDTO.parse(ja.get(0).getAsJsonObject());

                Intent intent = new Intent(activity, BookManualRegisterActivity.class);
                intent.putExtra("book", book);

                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
                activity.finish();
            } else {
                activity.showToast("검색결과가 없습니다. 책 제목을 직접 입력해주세요.");
            }
            // BarcodeGraphic barcodeGraphic = new BarcodeGraphic(graphicOverlay, barcode);
            //  graphicOverlay.add(barcodeGraphic);
        }
        graphicOverlay.postInvalidate();
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        activity.showToast("바코드 인식이 되지 않습니다.");
        Log.e(TAG, "Barcode detection failed " + e);
    }
}