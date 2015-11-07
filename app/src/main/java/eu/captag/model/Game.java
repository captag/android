package eu.captag.model;


import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.List;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
@ParseClassName("Game")
public class Game extends ParseObject {


   // region Constants


   public static final String ATTRIBUTE_NAME = "name";
   public static final String ATTRIBUTE_START_DATE = "startAt";

   public static final String RELATION_TAGS = "tags";


   // endregion


   public String getName () {
      return getString(ATTRIBUTE_NAME);
   }


   public Date getStartDate () {
      return getDate(ATTRIBUTE_START_DATE);
   }


   public List<Tag> getTags () {

      ParseQuery<Tag> tagQuery = ParseQuery.getQuery(Tag.class);
      tagQuery.whereEqualTo(Tag.RELATION_GAME, this);
      try {
         return tagQuery.find();
      } catch (ParseException e) {
         return null;
      }
   }


   public void getTagsInBackground (FindCallback<Tag> findCallback) {

      ParseQuery<Tag> tagQuery = ParseQuery.getQuery(Tag.class);
      tagQuery.whereEqualTo(Tag.RELATION_GAME, this);
      tagQuery.findInBackground(findCallback);
   }


   public List<Team> getTeams () {

      ParseQuery<Team> teamQuery = ParseQuery.getQuery(Team.class);
      teamQuery.whereEqualTo(Team.RELATION_GAME, this);
      try {
         return teamQuery.find();
      } catch (ParseException e) {
         return null;
      }
   }


   @Override
   public boolean equals (Object o) {

      if (this == o) {
         return true;
      }

      if (o == null || getClass() != o.getClass()) {
         return false;
      }

      Game game = (Game) o;
      return getObjectId().equals(game.getObjectId());
   }


   @Override
   public int hashCode () {
      return getObjectId().hashCode();
   }


   @Override
   public String toString () {
      return Game.class.getSimpleName() + "{objectId=" + getObjectId() + "}";
   }
}
