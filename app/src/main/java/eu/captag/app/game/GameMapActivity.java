package eu.captag.app.game;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import eu.captag.R;
import eu.captag.app.BaseActivity;
import eu.captag.model.Game;
import eu.captag.model.Player;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class GameMapActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {


   private static final LatLng GERMANY = new LatLng(51.099442, 10.371321);
   private static final int ZOOM = 5;


   private MapView mapView;
   private Player player;


   public static void start (Activity activity, Game game) {

      if (activity == null) {
         String message = "activity must be not null";
         throw new IllegalArgumentException(message);
      }

      if (game == null) {
         String message = "game must be not null";
         throw new IllegalArgumentException(message);
      }

      GameHolder.INSTANCE.game = game;
      Intent intent = new Intent(activity, GameMapActivity.class);
      activity.startActivity(intent);
   }


   // region Activity lifecycle


   @Override
   protected void onCreate (Bundle instanceState) {

      super.onCreate(instanceState);
      setContentView(R.layout.activity_game_map);
      // Retrieve the player
      retrievePlayer();
      // Initialize the map view
      initializeMapView(instanceState);
   }


   @Override
   protected void onResume () {

      super.onResume();
      mapView.onResume();
   }


   @Override
   protected void onSaveInstanceState (Bundle outState) {

      super.onSaveInstanceState(outState);
      mapView.onSaveInstanceState(outState);
   }


   @Override
   protected void onPause () {

      super.onPause();
      mapView.onPause();
   }


   @Override
   protected void onDestroy () {

      super.onDestroy();
      mapView.onDestroy();
   }


   @Override
   public void onLowMemory () {

      super.onLowMemory();
      mapView.onLowMemory();
   }


   // endregion
   // region Map callbacks


   @Override
   public void onMapLoaded () {

   }


   @Override
   public void onMapReady (GoogleMap googleMap) {

      // Move the map to the initial position and zoom
      CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(GERMANY)
            .zoom(ZOOM)
            .build();

      // Move the camera to the defined position
      googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
      googleMap.setOnMapLoadedCallback(this);
   }


   // endregion


   private Game getGame () {

      Game game = GameHolder.INSTANCE.game;
      if (game == null) {
         String message = "game not set";
         throw new IllegalStateException(message);
      }

      return game;
   }


   private void retrievePlayer () {

      Game game = getGame();
      ParseUser user = ParseUser.getCurrentUser();

      ParseQuery<Player> playerQuery = ParseQuery.getQuery(Player.class);
      playerQuery.whereEqualTo(Player.RELATION_USER, user);
      playerQuery.whereEqualTo(Player.RELATION_GAME, game);

      playerQuery.getFirstInBackground(new GetCallback<Player>() {
         @Override
         public void done (Player player, ParseException e) {
            if (player != null) {
               setPlayer(player);
               initializePlayerLocationListener();
            }
         }
      });
   }


   private Player getPlayer () {
      return player;
   }


   private void setPlayer (Player player) {
      this.player = player;
   }


   private void initializePlayerLocationListener () {

      Player player = getPlayer();
      PlayerLocationListener playerLocationListener = new PlayerLocationListener(player);
      LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      //noinspection ResourceType
      locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 5000, 1, playerLocationListener
      );
   }


   // region Views


   private void initializeMapView (Bundle instanceState) {

      mapView = (MapView) findViewById(R.id.mapView);
      mapView.onCreate(instanceState);
      mapView.getMapAsync(this);
   }


   // endregion


   private enum GameHolder {


      INSTANCE;


      private Game game;
   }
}
