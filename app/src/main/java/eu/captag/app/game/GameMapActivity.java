package eu.captag.app.game;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import eu.captag.R;
import eu.captag.app.BaseActivity;
import eu.captag.model.Game;
import eu.captag.model.Player;
import eu.captag.model.Tag;
import eu.captag.model.Team;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class GameMapActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, LocationListener {


   // region Constants


   private static final LatLng GERMANY = new LatLng(51.099442, 10.371321);
   private static final int ZOOM = 5;


   // endregion
   // region Fields


   private MapView mapView;
   private Marker ownMarker;
   private BitmapDescriptor bitmapDescriptorOwnMarker;
   private Player player;
   private BitmapDrawable bitmapDrawableTagCaptured;
   private BitmapDrawable bitmapDrawableTagFree;
   private List<Marker> tagMarkers;
   private Handler tagMarkersUpdateHandler;


   // endregion


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

      if (tagMarkersUpdateHandler == null) {
         tagMarkersUpdateHandler = new Handler();
      }

      tagMarkersUpdateHandler.postDelayed(new Runnable() {
         @Override
         public void run () {
            Game game = getGame();
            game.getTagsInBackground(new FindCallback<Tag>() {
               @Override
               public void done (List<Tag> tags, ParseException e) {
                  if (tags != null) {
                     updateTagMarkers(tags);
                  }
               }
            });
         }
      }, 5000);
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
   // region LocationListener implementation


   @Override
   public void onLocationChanged (Location location) {

      ParseGeoPoint geoPoint = new ParseGeoPoint();
      geoPoint.setLatitude(location.getLatitude());
      geoPoint.setLongitude(location.getLongitude());

      Player player = getPlayer();
      if (player != null) {
         player.setGeoPoint(geoPoint);
         player.saveInBackground();
         // Update the player marker
         LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude());
         updateOwnMarker(coordinate);
         moveCamera(coordinate);
      }
   }


   @Override
   public void onStatusChanged (String provider, int status, Bundle extras) {}


   @Override
   public void onProviderEnabled (String provider) {}


   @Override
   public void onProviderDisabled (String provider) {}


   // endregion


   private Game getGame () {

      Game game = GameHolder.INSTANCE.game;
      if (game == null) {
         String message = "game not set";
         throw new IllegalStateException(message);
      }

      return game;
   }


   private GoogleMap getGoogleMap () {
      return mapView.getMap();
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


   public List<Marker> getTagMarkers () {

      if (tagMarkers == null) {
         tagMarkers = new ArrayList<>();
      }

      return tagMarkers;
   }


   private void initializePlayerLocationListener () {

      LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, this);

      // Define the criteria how to select the location provider -> use default
      Criteria criteria = new Criteria();
      String provider = locationManager.getBestProvider(criteria, false);
      Location location = locationManager.getLastKnownLocation(provider);
      onLocationChanged(location);
   }


   // region Views


   private void initializeMapView (Bundle instanceState) {

      mapView = (MapView) findViewById(R.id.mapView);
      mapView.onCreate(instanceState);
      mapView.getMapAsync(this);
   }


   private void updateOwnMarker (LatLng coordinate) {

      GoogleMap googleMap = getGoogleMap();
      // Remove the old player marker
      if (ownMarker != null) {
         ownMarker.remove();
      }

      if (bitmapDescriptorOwnMarker == null) {
         bitmapDescriptorOwnMarker = BitmapDescriptorFactory.fromResource(R.mipmap.ic_own_location_red_48dp);
      }

      // Add a new player marker
      ownMarker = googleMap.addMarker(
            new MarkerOptions()
                  .position(coordinate)
                  .icon(bitmapDescriptorOwnMarker)
      );
   }


   private void updateTagMarkers (List<Tag> tags) {

      GoogleMap googleMap = getGoogleMap();
      List<Marker> tagMarkers = getTagMarkers();
      // Remove the old tag markers
      for (Marker marker : tagMarkers) {
         marker.remove();
      }
      // Clear the tag marker list
      tagMarkers.clear();

      Resources resources = getResources();
      if (bitmapDrawableTagFree == null) {
         Bitmap bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_tag_captured_white_48dp);
         bitmapDrawableTagCaptured = new BitmapDrawable(resources, bitmap);
      }

      if (bitmapDrawableTagFree == null) {
         Bitmap bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_tag_free_red_48dp);
         bitmapDrawableTagFree = new BitmapDrawable(resources, bitmap);
      }

      // Add new tag markers
      for (Tag tag : tags) {

         BitmapDescriptor markerIcon;
         Team team = tag.getTeam();
         if (team != null) {
            bitmapDrawableTagCaptured.setTint(team.getColor());
            markerIcon = BitmapDescriptorFactory.fromBitmap(bitmapDrawableTagCaptured.getBitmap());
         } else {
            markerIcon = BitmapDescriptorFactory.fromBitmap(bitmapDrawableTagFree.getBitmap());
         }

         ParseGeoPoint geoPoint = tag.getGeoPoint();
         LatLng coordinate = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
         Marker tagMarker = googleMap.addMarker(
               new MarkerOptions()
                     .position(coordinate)
                     .icon(markerIcon)
         );

         // Add the new tag marker to the tag marker list
         tagMarkers.add(tagMarker);
      }
   }


   private void moveCamera (LatLng coordinate) {

      GoogleMap googleMap = getGoogleMap();
      googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
   }


   // endregion


   private enum GameHolder {


      INSTANCE;


      private Game game;
   }
}
