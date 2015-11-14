package de.captag.model;


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

   public static final String POINTER_GAME = "game";
   public static final String RELATION_PLAYER = "player";
   public static final String RELATION_TEAM = "team";


   // endregion
   // region Fields


   private Game game;


   // endregion


   /**
    * Returns the game to which this tag is assigned.
    */
   public Game getGame () {
      return game;
   }


   /**
    * Sets the game to which this tag is assigned.
    * @param game The game to set.
    */
   void setGame (Game game) {
      this.game = game;
   }


   /**
    * Returns the geo point of this tag.
    */
   public ParseGeoPoint getGeoPoint () {
      return getParseGeoPoint(ATTRIBUTE_GEO_POINT);
   }


   /**
    * Returns the label of this tag.
    */
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
