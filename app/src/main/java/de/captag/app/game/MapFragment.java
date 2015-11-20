package de.captag.app.game;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;

import java.util.List;

import de.captag.R;
import de.captag.app.BaseFragment;
import de.captag.model.Game;
import de.captag.model.Tag;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class MapFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {


   // region Constants


   public static final String TAG = MapFragment.class.getName();
   public static final int LAYOUT_ID = R.layout.fragment_map;

   private static final LatLng LOCATION_GERMANY = new LatLng(51.099442, 10.371321);
   private static final int DEFAULT_MAP_ZOOM = 5;


   // endregion
   // region Fields


   private Game game;
   private MapView mapView;
   private GoogleMap googleMap;


   // endregion


   /**
    * Factory method for MapFragments.
    * @return A new instance of the MapFragment.
    */
   public static MapFragment newInstance (Game game) {

      MapFragment fragment = new MapFragment();
      fragment.setGame(game);
      return fragment;
   }


   // region Fragment lifecycle


   @Override
   public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle inState) {

      View view = inflater.inflate(LAYOUT_ID, container, false);
      // Find the map view and initialize it
      mapView = (MapView) view.findViewById(R.id.mapView);
      mapView.onCreate(inState);
      // Return the inflated view
      return view;
   }


   @Override
   public void onViewCreated (View view, Bundle inState) {

      super.onViewCreated(view, inState);
      // Get the map in background
      mapView.getMapAsync(this);
   }


   @Override
   public void onResume () {

      super.onResume();
      mapView.onResume();
   }


   @Override
   public void onSaveInstanceState (Bundle outState) {

      super.onSaveInstanceState(outState);
      mapView.onSaveInstanceState(outState);
   }


   @Override
   public void onPause () {

      super.onPause();
      mapView.onPause();
   }


   @Override
   public void onLowMemory () {

      super.onLowMemory();
      mapView.onLowMemory();
   }


   @Override
   public void onDestroy () {

      super.onDestroy();
      mapView.onDestroy();
   }


   // endregion
   // region Map callbacks


   @Override
   public void onMapReady (GoogleMap googleMap) {

      // Set the google map
      this.googleMap = googleMap;

      // Move the map to the initial position and zoom
      CameraPosition cameraPosition = new CameraPosition.Builder()
         .target(LOCATION_GERMANY)
         .zoom(DEFAULT_MAP_ZOOM)
         .build();

      // Move the camera to the defined position
      googleMap.setMyLocationEnabled(true);
      googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
      googleMap.setOnMapLoadedCallback(this);
   }


   @Override
   public void onMapLoaded () {

      // Request the tags of this game
      retrieveTags();
   }


   // endregion


   private void onLoadingTagsDone (List<Tag> tags) {

      LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

      // Add new tag markers
      for (Tag tag : tags) {

         ParseGeoPoint geoPoint = tag.getGeoPoint();
         LatLng point = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
         boundsBuilder.include(point);

         Marker tagMarker = googleMap.addMarker(
            new MarkerOptions()
               .position(point)
               .title(tag.getLabel())
               .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_tag_free_red_48dp))
         );
      }

      // Update the camera position
      LatLngBounds cameraBounds = boundsBuilder.build();
      CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(cameraBounds, 150);
      googleMap.animateCamera(cameraUpdate);
   }


   public Game getGame () {
      return game;
   }


   public void setGame (Game game) {

      if (game == null) {
         String message = "game must be not null";
         throw new IllegalArgumentException(message);
      }

      this.game = game;
   }


   // region Communication with Captag Api


   private void retrieveTags () {

      Game game = getGame();
      if (game == null) {
         return;
      }

      FindCallback<Tag> callback = new FindCallback<Tag>() {
         @Override
         public void done (List<Tag> tags, ParseException e) {

            if (e == null) {
               onLoadingTagsDone(tags);
               return;
            }

            String message = getString(R.string.error_loadingTeamsFailed);
            Snackbar snackbar = createErrorSnackbar(message, Snackbar.LENGTH_INDEFINITE, null);
            snackbar.setAction(R.string.action_retry, new View.OnClickListener() {
               @Override
               public void onClick (View v) {
                  retrieveTags();
               }
            });
            snackbar.show();
         }
      };
      // Request the tags in background
      game.retrieveTagsInBackground(callback);
   }


   // endregion
}
