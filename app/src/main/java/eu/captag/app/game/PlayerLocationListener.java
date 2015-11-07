package eu.captag.app.game;


import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.parse.ParseGeoPoint;

import eu.captag.model.Player;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class PlayerLocationListener implements LocationListener {


   private Player player;


   public PlayerLocationListener (Player player) {

      if (player == null) {
         String message = "player must be not null";
         throw new IllegalArgumentException(message);
      }

      this.player = player;
   }


   @Override
   public void onLocationChanged (Location location) {

      ParseGeoPoint geoPoint = new ParseGeoPoint();
      geoPoint.setLatitude(location.getLatitude());
      geoPoint.setLongitude(location.getLongitude());

      player.setGeoPoint(geoPoint);
      player.saveInBackground();
   }


   @Override
   public void onStatusChanged (String provider, int status, Bundle extras) {

   }


   @Override
   public void onProviderEnabled (String provider) {

   }


   @Override
   public void onProviderDisabled (String provider) {

   }
}
