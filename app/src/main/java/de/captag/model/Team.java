package de.captag.model;


import android.graphics.Color;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * Team domain object.
 * @author Ulrich Raab
 */
@ParseClassName("Team")
public class Team extends ParseObject {


   // region Constants


   public static final String ATTRIBUTE_COLOR = "color";
   public static final String ATTRIBUTE_MAX_TEAM_MEMBERS = "maxTeamMembers";
   public static final String ATTRIBUTE_NAME = "name";

   public static final String POINTER_GAME = "game";


   // endregion
   // region Fields


   private Game game;
   private List<Player> teamMembers;


   // endregion


   /**
    * returns the color of this team as hex value.
    */
   public int getColor () {

      String hexColor = getString(ATTRIBUTE_COLOR);
      return Color.parseColor(hexColor);
   }


   /**
    * Returns the game to which this team is assigned.
    */
   public Game getGame () {
      return game;
   }


   /**
    * Sets the game to which this team is assigned.
    * @param game The game to set.
    */
   void setGame (Game game) {
      this.game = game;
   }


   /**
    * Returns the maximum number of team members in this team.
    */
   public int getMaxTeamMembers () {
      return getInt(ATTRIBUTE_MAX_TEAM_MEMBERS);
   }


   /**
    * Returns the name of this team.
    */
   public String getName () {
      return getString(ATTRIBUTE_NAME);
   }


   /**
    * Returns whether the given user is a member of this team.
    * @return true if the given user is a team member, false otherwise.
    */
   public boolean isTeamMember (ParseUser user) {

      if (user == null) {
         return false;
      }

      String userId = user.getObjectId();
      for (Player teamMember : getTeamMembers()) {
         String teamMemberId = teamMember.getUser().getObjectId();
         if (userId.equals(teamMemberId)) {
            return true;
         }
      }

      return false;
   }


   /**
    * Returns the team members of this team.
    */
   public List<Player> getTeamMembers () {

      if (teamMembers == null) {
         teamMembers = new ArrayList<>();
      }

      return teamMembers;
   }


   /**
    * Sets the team members of this team.
    * @param teamMembers The team members to set.
    */
   void setTeamMembers (List<Player> teamMembers) {
      this.teamMembers = teamMembers;
   }


   /**
    * Retrieves a list of Player that are members of this team.
    * @param callback The callback which handles the result of the request.
    */
   public void retrieveTeamMembersInBackground (final FindCallback<Player> callback) {

      FindCallback<Player> wrapper = new FindCallback<Player>() {
         @Override
         public void done (List<Player> temMembers, ParseException e) {

            if (e == null) {
               setTeamMembers(temMembers);
            }

            if (callback != null) {
               callback.done(temMembers, e);
            }
         }
      };

      // @formatter:off
      ParseQuery
            .getQuery(Player.class)
            .whereEqualTo(Player.POINTER_TEAM, this)
            .include(Player.POINTER_USER)
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

      Team team = (Team) o;
      return getObjectId().equals(team.getObjectId());
   }


   @Override
   public int hashCode () {
      return getObjectId().hashCode();
   }


   @Override
   public String toString () {
      return Team.class.getSimpleName() + "{objectId=" + getObjectId() + "}";
   }
}
