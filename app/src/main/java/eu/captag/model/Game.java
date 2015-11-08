package eu.captag.model;


import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
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
   public static final String ATTRIBUTE_STATUS = "status";

   public static final String STATUS_INFORMED = "informed";
   public static final String STATUS_NEW = "new";
   public static final String STATUS_STARTED = "started";
   public static final String STATUS_FINISHED = "finished";


   // endregion


   public String getName () {
      return getString(ATTRIBUTE_NAME);
   }


   public Date getStartDate () {
      return getDate(ATTRIBUTE_START_DATE);
   }


   public String getStatus () {
      return getString(ATTRIBUTE_STATUS);
   }


   public List<Tag> getTags () {

      try {

         ParseQuery<Tag> query = ParseQuery.getQuery(Tag.class);
         query.whereEqualTo(Tag.POINTER_GAME, this);
         return query.find();

      } catch (ParseException e) {
         // Return a empty list if there was a error
         return new ArrayList<>();
      }
   }


   public void getTagsInBackground (FindCallback<Tag> findCallback) {

      ParseQuery<Tag> query = ParseQuery.getQuery(Tag.class);
      query.whereEqualTo(Tag.POINTER_GAME, this);
      query.findInBackground(findCallback);
   }


   public List<Team> getTeams () {

      try {

         ParseQuery<Team> query = ParseQuery.getQuery(Team.class);
         query.whereEqualTo(Team.POINTER_GAME, this);
         return query.find();

      } catch (ParseException e) {
         // Return a empty list if there was a error
         return new ArrayList<>();
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
