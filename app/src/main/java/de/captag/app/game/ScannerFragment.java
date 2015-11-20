package de.captag.app.game;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.List;

import de.captag.R;
import de.captag.app.BaseFragment;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class ScannerFragment extends BaseFragment implements BarcodeCallback {


   // region Constants


   public static final String TAG = ScannerFragment.class.getName();
   public static final int LAYOUT_ID = R.layout.fragment_scanner;
   public static final int SOUND_ID_SCAN_SUCCESS = R.raw.scan_success;
   public static final int STATE_ON = 0;
   public static final int STATE_OFF = 1;


   // endregion
   // region Fields


   private CompoundBarcodeView barcodeView;
   private Callback callback;
   private int state = STATE_OFF;


   // endregion


   /**
    * Factory method for ScannerFragments.
    * @return A new instance of the ScannerFragment.
    */
   public static ScannerFragment newInstance () {
      return new ScannerFragment();
   }


   // region Fragment lifecycle


   @Override
   public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle inState) {
      return inflater.inflate(LAYOUT_ID, container, false);
   }


   @Override
   public void onViewCreated (View view, Bundle inState) {

      super.onViewCreated(view, inState);
      initializeBarcodeView();
   }


   @Override
   public void onPause () {

      super.onPause();
      turnOff();
   }


   // endregion
   // region UI initialization


   private void initializeBarcodeView () {

      // Create the barcode view configuration
      Intent configuration = new Intent();
      configuration.putExtra(Intents.Scan.FORMATS, BarcodeFormat.QR_CODE.name());
      // Initialize the barcode view
      barcodeView = getView(R.id.barcodeView);
      barcodeView.initializeFromIntent(configuration);
      barcodeView.decodeSingle(this);
      barcodeView.setStatusText("");
   }


   // endregion
   // region BarcodeCallback implementation


   @Override
   public void barcodeResult (BarcodeResult result) {

      playScanSuccessSound();
      if (callback != null) {
         String id = result.getText();
         callback.onTagScanned(id);
      }
   }


   @Override
   public void possibleResultPoints (List<ResultPoint> resultPoints) {}


   // endregion


   public void setCallback (Callback callback) {
      this.callback = callback;
   }


   public int getState () {
      return state;
   }


   public void turnOn () {

      if (state == STATE_ON) {
         return;
      }

      state = STATE_ON;
      barcodeView.postOnAnimationDelayed(new Runnable() {
         @Override
         public void run () {
            barcodeView.setVisibility(View.VISIBLE);
            barcodeView.resume();
         }
      }, 250);
   }


   public void turnOff () {

      if (state == STATE_OFF) {
         return;
      }

      state = STATE_OFF;
      barcodeView.postOnAnimation(new Runnable() {
         @Override
         public void run () {
            barcodeView.setVisibility(View.GONE);
            barcodeView.pause();
         }
      });
   }


   private void playScanSuccessSound () {

      Context context = getActivity();
      MediaPlayer player = MediaPlayer.create(context, SOUND_ID_SCAN_SUCCESS);
      player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
         @Override
         public void onCompletion (MediaPlayer mp) {
            mp.release();
         }
      });

      player.start();
   }


   /**
    * TODO Write documentation
    */
   public interface Callback {


      /**
       * This method is called as soon as the user has scanned a tag QR code.
       * @param id The id of the scanned tag.
       */
      void onTagScanned (String id);
   }
}
