package de.captag.model;


import android.graphics.Color;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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

      for (Player teamMember : getTeamMembers()) {
         if (teamMember.isUser(user)) {
            return true;
         }
      }

      return false;
   }


   /**
    * Returns the team member representing the given user.
    * @param user The user.
    * @return The team member if the given user is a member of this team, null otherwise.
    */
   public Player getTeamMember (ParseUser user) {

      for (Player teamMember : getTeamMembers()) {
         if (teamMember.isUser(user)) {
            return teamMember;
         }
      }

      return null;
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
    * Added the given user to this team.
    * @param user The user to add.
    * @param callback The callback which handles the result of the request.
    */
   public void joinInBackground (ParseUser user, final SaveCallback callback) {

      if (user == null) {
         String message = "user must be not null";
         throw new IllegalArgumentException(message);
      }

      Game game = getGame();

      // Create a new player object
      final Player player = new Player();
      player.setGame(game);
      player.setTeam(this);
      player.setUser(user);

      // Create a new SaveCallback
      SaveCallback wrapper = new SaveCallback() {
         @Override
         public void done (ParseException e) {

            if (e == null) {
               // Subscribe for notifications
               Game game = getGame();
               ParsePush.subscribeInBackground(game.getObjectId());
               // Add the saved player to the team members list
               List<Player> teamMembers = getTeamMembers();
               teamMembers.add(player);
            }

            if (callback != null) {
               callback.done(e);
            }
         }
      };

      // Save the player
      player.saveInBackground(wrapper);
   }


   /**
    * Removes the given user from this team.
    * @param user The user to remove.
    * @param callback The callback which handles the result of the request.
    */
   public void leaveInBackground (ParseUser user, final DeleteCallback callback) {

      if (user == null) {
         String message = "user must be not null";
         throw new IllegalArgumentException(message);
      }

      final Player teamMember = getTeamMember(user);
      if (teamMember == null) {
         return;
      }

      // Create a new DeleteCallback
      DeleteCallback wrapper = new DeleteCallback() {
         @Override
         public void done (ParseException e) {

            if (e == null) {
               // Subscribe for notifications
               Game game = getGame();
               ParsePush.unsubscribeInBackground(game.getObjectId());
               // Remove the deleted player from the team members list
               List<Player> teamMembers = getTeamMembers();
               teamMembers.remove(teamMember);
            }

            if (callback != null) {
               callback.done(e);
            }
         }
      };

      teamMember.deleteInBackground(wrapper);
   }


   /**
    * Retrieves a list of players that are members of this team.
    * @param callback The callback which handles the result of the request.
    */
   public void retrieveTeamMembersInBackground (final FindCallback<Player> callback) {

      FindCallback<Player> wrapper = new FindCallback<Player>() {
         @Override
         public void done (List<Player> temMembers, ParseException e) {

            if (e == null) {

               setTeamMembers(temMembers);
               for (Player player : temMembers) {
                  player.setGame(Team.this.getGame());
                  player.setTeam(Team.this);
               }

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
