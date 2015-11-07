package eu.captag.model;


import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
@ParseClassName("Tag")
public class Tag extends ParseObject {


   // region Constants


   public static final String ATTRIBUTE_GEO_POINT = "geoPoint";
   public static final String ATTRIBUTE_LABEL = "label";

   public static final String RELATION_GAME = "game";
   public static final String RELATION_PLAYER = "player";
   public static final String RELATION_TEAM = "team";


   // endregion


   public Game getGame () {

      ParseRelation<Game> gameRelation = getRelation(RELATION_GAME);
      ParseQuery<Game> gameQuery = gameRelation.getQuery();
      try {
         return gameQuery.getFirst();
      } catch (ParseException e) {
         return null;
      }
   }


   public ParseGeoPoint getGeoPoint () {
      return getParseGeoPoint(ATTRIBUTE_GEO_POINT);
   }


   public String getLable () {
      return getString(ATTRIBUTE_LABEL);
   }


   public Player getPlayer () {

      ParseRelation<Player> gameRelation = getRelation(RELATION_PLAYER);
      ParseQuery<Player> gameQuery = gameRelation.getQuery();
      try {
         return gameQuery.getFirst();
      } catch (ParseException e) {
         return null;
      }
   }


   public Team getTeam () {

      ParseRelation<Team> gameRelation = getRelation(RELATION_TEAM);
      ParseQuery<Team> gameQuery = gameRelation.getQuery();
      try {
         return gameQuery.getFirst();
      } catch (ParseException e) {
         return null;
      }
   }
}
