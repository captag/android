package de.captag.model;


import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
@ParseClassName("Player")
public class Player extends ParseObject {


   // region Constants


   public static final String ATTRIBUTE_GEO_POINT = "geoPoint";

   public static final String POINTER_GAME = "game";
   public static final String POINTER_TEAM = "team";
   public static final String POINTER_USER = "user";


   // endregion


   public ParseGeoPoint getGeoPoint () {
      return getParseGeoPoint(ATTRIBUTE_GEO_POINT);
   }


   public void setGeoPoint (ParseGeoPoint geoPoint) {
      put(ATTRIBUTE_GEO_POINT, geoPoint);
   }


   public void setGame (Game game) {
      this.put("game", game);
   }


   public Team getTeam () {
      return getPointer(POINTER_TEAM);
   }


   public void setTeam (Team team) {
      this.put("team", team);
   }


   public boolean isUser (ParseUser user) {

      //noinspection SimplifiableIfStatement
      if (user == null) {
         return false;
      }

      return getUser().getObjectId().equals(user.getObjectId());
   }


   public ParseUser getUser () {
      return getPointer(POINTER_USER);
   }


   public void setUser (ParseUser user) {
      this.put("user", user);
   }


   private <T> T getPointer (String pointer) {
      //noinspection unchecked
      return (T) get(pointer);
   }
}
