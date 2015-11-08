package eu.captag.model;


import android.graphics.Color;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO Write documentation
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


   public int getColor () {

      String hexColor = getString(ATTRIBUTE_COLOR);
      return Color.parseColor(hexColor);
   }


   public Game getGame () {
      return (Game) getParseObject(POINTER_GAME);
   }


   public int getMaxTeamMembers () {
      return getInt(ATTRIBUTE_MAX_TEAM_MEMBERS);
   }


   public String getName () {
      return getString(ATTRIBUTE_NAME);
   }


   public List<Player> getTeamMembers () {

      ParseQuery<Player> query = ParseQuery.getQuery(Player.class);
      query.whereEqualTo(Player.POINTER_TEAM, this);
      query.include(Player.POINTER_GAME);
      query.include(Player.POINTER_TEAM);
      query.include(Player.POINTER_USER);
      try {
         return query.find();
      } catch (ParseException e) {
         return new ArrayList<>();
      }
   }


   public void getTeamMembersInBackground (FindCallback<Player> callback) {

      ParseQuery<Player> playerQuery = ParseQuery.getQuery(Player.class);
      playerQuery.whereEqualTo(Player.POINTER_TEAM, this);
      playerQuery.findInBackground(callback);
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
