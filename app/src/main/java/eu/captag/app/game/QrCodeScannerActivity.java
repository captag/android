package eu.captag.app.game;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import eu.captag.R;
import eu.captag.app.BaseActivity;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class QrCodeScannerActivity extends BaseActivity {


   private CompoundBarcodeView barcodeView;
   private CaptureManager captureManager;


   public static void startForResult (Activity activity, int requestCode) {

      Intent intent = new Intent(activity, QrCodeScannerActivity.class);
      activity.startActivityForResult(intent, requestCode);
   }


   @Override
   protected void onCreate (Bundle instanceState) {

      super.onCreate(instanceState);
      setContentView(R.layout.activity_qr_code_scanner);

      barcodeView = getView(R.id.barcodeView);
      barcodeView.setStatusText(getString(R.string.action_scanQrCode));
      initializeCaptureManager(instanceState);
   }


   @Override
   protected void onResume () {

      super.onResume();
      captureManager.onResume();
   }


   @Override
   protected void onSaveInstanceState (Bundle outState) {

      super.onSaveInstanceState(outState);
      captureManager.onSaveInstanceState(outState);
   }


   @Override
   protected void onPause () {

      super.onPause();
      captureManager.onPause();
   }


   @Override
   protected void onDestroy () {

      super.onDestroy();
      captureManager.onDestroy();
   }


   @Override
   public boolean onKeyDown (int keyCode, KeyEvent event) {
      return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
   }


   private void initializeCaptureManager (Bundle instanceState) {

      captureManager = new CaptureManager(this, barcodeView);
      captureManager.initializeFromIntent(getIntent(), instanceState);
      captureManager.decode();
   }
}
