package eu.captag;


import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import eu.captag.model.Game;
import eu.captag.model.Player;
import eu.captag.model.Tag;
import eu.captag.model.Team;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class Captag extends Application {


   private static final String APPLICATION_ID = "VeEodLh11HU4otvwIHpl3slzqN21jYFRefHMQPvp";
   private static final String CLIENT_KEY = "hgNnjxaaMFealnuJAYWzQtONMldi5tY9mJ61pm2R";


   @Override
   public void onCreate () {

      super.onCreate();
      // Initialize parse
      Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
      ParseInstallation.getCurrentInstallation().saveInBackground();

      ParseObject.registerSubclass(Game.class);
      ParseObject.registerSubclass(Player.class);
      ParseObject.registerSubclass(Tag.class);
      ParseObject.registerSubclass(Team.class);
   }
}
