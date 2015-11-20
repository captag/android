package de.captag.app.game;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import de.captag.R;
import de.captag.app.BaseFragment;
import de.captag.model.Game;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class GameFragment extends BaseFragment implements SlidingUpPanelLayout.PanelSlideListener, ScannerFragment.Callback {


   // region Constants


   public static final String TAG = GameFragment.class.getName();
   public static final int LAYOUT_ID = R.layout.fragment_game;


   // endregion
   // region Fields


   private Game game;
   private MapFragment mapFragment;
   private ScannerFragment scannerFragment;
   private boolean panelExpanded = false;


   // endregion


   /**
    * Factory method for GameFragments.
    * @return A new instance of the GameFragment.
    */
   public static GameFragment newInstance (Game game) {

      GameFragment fragment = new GameFragment();
      fragment.setGame(game);
      return fragment;
   }


   // region Fragment lifecycle


   @Override
   public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle inState) {
      return inflater.inflate(LAYOUT_ID, container, false);
   }


   @Override
   public void onViewCreated (View view, Bundle inState) {

      super.onViewCreated(view, inState);

      initializeSlidingUpPanel();
      initializeScannerFragment();
      initializeMapFragment();
   }


   // endregion
   // region UI initialization


   private void initializeSlidingUpPanel () {

      SlidingUpPanelLayout view = getView(R.id.slidingUpPanelLayout);
      view.setPanelSlideListener(this);
   }


   private void initializeMapFragment () {

      if (mapFragment == null) {
         Game game = getGame();
         mapFragment = MapFragment.newInstance(game);
      }

      getChildFragmentManager()
         .beginTransaction()
         .replace(R.id.frameLayout_container_mapFragment, mapFragment, MapFragment.TAG)
         .commit();
   }


   private void initializeScannerFragment () {

      if (scannerFragment == null) {
         scannerFragment = ScannerFragment.newInstance();
         scannerFragment.setCallback(this);
      }

      getChildFragmentManager()
         .beginTransaction()
         .replace(R.id.frameLayout_container_scannerFragment, scannerFragment, ScannerFragment.TAG)
         .commit();
   }


   // endregion
   // region PanelSlideListener implementation


   @Override
   public void onPanelSlide (View panel, float slideOffset) {

      if (panelExpanded) {
         if (scannerFragment.getState() == ScannerFragment.STATE_ON) {
            scannerFragment.turnOff();
         }
      }
   }


   @Override
   public void onPanelCollapsed (View panel) {

      if (scannerFragment == null) {
         String message = "scannerFragment must be not null";
         throw new IllegalStateException(message);
      }

      panelExpanded = false;
      scannerFragment.turnOff();
   }


   @Override
   public void onPanelExpanded (View panel) {

      if (scannerFragment == null) {
         String message = "scannerFragment must be not null";
         throw new IllegalStateException(message);
      }

      panelExpanded = true;
      scannerFragment.turnOn();
   }


   @Override
   public void onPanelAnchored (View panel) {}


   @Override
   public void onPanelHidden (View panel) {}


   // endregion
   // region ScannerFragment.Callback implementation


   @Override
   public void onTagScanned (String tagId) {

      SlidingUpPanelLayout view = getView(R.id.slidingUpPanelLayout);
      view.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
   }


   // endregion


   public Game getGame () {
      return game;
   }


   public void setGame (Game game) {
      this.game = game;
   }
}
