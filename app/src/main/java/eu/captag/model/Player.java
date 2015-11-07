package eu.captag.model;


import com.parse.ParseClassName;
import com.parse.ParseException;
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


   public static final String ATTRIBUTE_POSITION = "position";

   public static final String RELATION_GAME = "game";
   public static final String RELATION_TEAM = "team";
   public static final String RELATION_USER = "user";


   // endregion


   public void setGame (Game game) {

      ParseRelation<Game> gameRelation = getRelation(RELATION_GAME);
      gameRelation.add(game);
   }


   public void setTeam (Team team) {

      ParseRelation<Team> teamRelation = getRelation(RELATION_TEAM);
      teamRelation.add(team);
   }


   public ParseUser getUser () {

      ParseRelation<ParseUser> userRelation = getRelation(RELATION_USER);
      ParseQuery<ParseUser> userQuery = userRelation.getQuery();
      try {
         return userQuery.getFirst();
      } catch (ParseException e) {
         return null;
      }
   }


   public void setUser (ParseUser user) {

      ParseRelation<ParseUser> userRelation = getRelation(RELATION_USER);
      userRelation.add(user);
   }
}
