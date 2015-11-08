package eu.captag.model;


import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
@ParseClassName("Player")
public class Player extends ParseObject {


   // region Constants


   public static final String ATTRIBUTE_GEO_POINT = "geoPoint";

   public static final String RELATION_GAME = "game";
   public static final String RELATION_TEAM = "team";
   public static final String RELATION_USER = "user";


   // endregion


   public ParseGeoPoint getGeoPoint () {
      return getParseGeoPoint(ATTRIBUTE_GEO_POINT);
   }


   public void setGeoPoint (ParseGeoPoint geoPoint) {
      put(ATTRIBUTE_GEO_POINT, geoPoint);
   }


   public void setGame (Game game) {
        this.put("game",game);
   }

   public Team getTeam () {

      ParseRelation<Team> relation = getRelation(RELATION_TEAM);
      ParseQuery<Team> query = relation.getQuery();
      try {
         return query.getFirst();
      } catch (ParseException e) {
         return null;
      }
   }


   public void setTeam (Team team) {
        this.put("team",team);
   }


   public ParseUser getUser () {

      ParseRelation<ParseUser> relation = getRelation(RELATION_USER);
      ParseQuery<ParseUser> query = relation.getQuery();
      try {
         return query.getFirst();
      } catch (ParseException e) {
         return null;
      }
   }


   public void getUserInBackground (GetCallback<ParseUser> getCallback) {

      ParseRelation<ParseUser> relation = getRelation(RELATION_USER);
      ParseQuery<ParseUser> query = relation.getQuery();
      query.getFirstInBackground(getCallback);
   }


   public void setUser (ParseUser user) {
        this.put("user", user);
   }
}
