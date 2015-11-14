package de.captag.model;


import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Game domain object.
 * @author Ulrich Raab
 */
@ParseClassName("Game")
public class Game extends ParseObject {


   // region Constants


   public static final String ATTRIBUTE_ICON = "icon";
   public static final String ATTRIBUTE_NAME = "name";
   public static final String ATTRIBUTE_START_DATE = "startAt";
   public static final String ATTRIBUTE_STATUS = "status";

   public static final String STATUS_INFORMED = "informed";
   public static final String STATUS_NEW = "new";
   public static final String STATUS_STARTED = "started";
   public static final String STATUS_FINISHED = "finished";


   // endregion
   // region Fields


   private List<Tag> tags;
   private List<Team> teams;


   // endregion


   /**
    * Returns the icon URL of this game.
    */
   public String getIcon () {
      return getString(ATTRIBUTE_ICON);
   }


   /**
    * Returns the name of this game.
    */
   public String getName () {
      return getString(ATTRIBUTE_NAME);
   }


   /**
    * Returns the start date of this game.
    */
   public Date getStartDate () {
      return getDate(ATTRIBUTE_START_DATE);
   }


   /**
    * Returns the status of this game.
    */
   public String getStatus () {
      return getString(ATTRIBUTE_STATUS);
   }


   /**
    * Returns the tags assigned to this game.
    */
   public List<Tag> getTags () {

      if (tags == null) {
         tags = new ArrayList<>();
      }

      return tags;
   }


   /**
    * Sets the tags assigned to this game.
    * @param tags The tags to set.
    */
   void setTags (List<Tag> tags) {
      this.tags = tags;
   }


   /**
    * Returns the teams assigned to this game.
    */
   public List<Team> getTeams () {

      if (teams == null) {
         teams = new ArrayList<>();
      }

      return teams;
   }


   /**
    * Sets the teams assigned to this game.
    * @param teams The teams to set.
    */
   void setTeams (List<Team> teams) {
      this.teams = teams;
   }


   /**
    * Retrieves a list of Tags that are assigned to this game.
    * @param callback The callback which handles the result of the request.
    */
   public void retrieveTagsInBackground (final FindCallback<Tag> callback) {

      FindCallback<Tag> wrapper = new FindCallback<Tag>() {
         @Override
         public void done (List<Tag> tags, ParseException e) {

            if (e == null) {

               setTags(tags);


            } else {
               // TODO log exception with crashlytics
            }

            if (callback != null) {
               callback.done(tags, e);
            }
         }
      };

      // @formatter:off
      ParseQuery
            .getQuery(Tag.class)
            .whereEqualTo(Tag.POINTER_GAME, this)
            .findInBackground(wrapper);
      // @formatter:on
   }


   /**
    * Retrieves a list of Teams that are assigned to this game.
    * @param callback The callback which handles the result of the request.
    */
   public void retrieveTeamsInBackground (final FindCallback<Team> callback) {

      FindCallback<Team> wrapper = new FindCallback<Team>() {
         @Override
         public void done (List<Team> teams, ParseException e) {

            if (e == null) {

               setTeams(teams);
               for (Team team : teams) {
                  team.setGame(Game.this);
               }

            } else {
               // TODO log exception with crashlytics
            }

            if (callback != null) {
               callback.done(teams, e);
            }
         }
      };

      // @formatter:off
      ParseQuery
            .getQuery(Team.class)
            .whereEqualTo(Team.POINTER_GAME, this)
            .findInBackground(wrapper);
      // @formatter:on
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
