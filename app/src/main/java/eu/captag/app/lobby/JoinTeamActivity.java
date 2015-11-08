package eu.captag.app.lobby;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;

import java.util.List;

import eu.captag.R;
import eu.captag.app.BaseActivity;
import eu.captag.app.lobby.adapter.TeamAdapter;
import eu.captag.model.Game;
import eu.captag.model.Player;
import eu.captag.model.Team;
import eu.captag.util.DividerItemDecoration;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class JoinTeamActivity extends BaseActivity implements TeamAdapter.InteractionListener {


   private TeamAdapter teamAdapter;


   public static void start (Activity activity, Game game) {

      if (game == null) {
         String message = "game must be not null";
         throw new IllegalArgumentException(message);
      }

      GameHolder.INSTANCE.game = game;
      Intent intent = new Intent(activity, JoinTeamActivity.class);
      activity.startActivity(intent);
   }


   @Override
   protected void onCreate (Bundle savedInstanceState) {

      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_join_team);

      // Initialization
      initializeViews();
   }


   @Override
   public void onJoinTeamClicked (Team team) {

      Player player = new Player();
      player.setGame(getGame());
      player.setTeam(team);
      player.setUser(getUser());

      // Subscript for notification
      Game game = getGame();
      ParsePush.subscribeInBackground(game.getObjectId());

      SaveCallback saveCallback = new SaveCallback() {
         @Override
         public void done (ParseException e) {
            if (e == null) {
               TeamPagerActivity.start(JoinTeamActivity.this, getGame());
               // Finish this activity to prevent the user from navigating back
               finish();
            } else {
               String message = getString(R.string.error_joiningTeamFailed);
               showErrorSnackbar(message, Snackbar.LENGTH_LONG);
            }
         }
      };

      player.saveInBackground(saveCallback);
   }


   private Game getGame () {

      Game game = GameHolder.INSTANCE.game;
      if (game == null) {
         String message = "game not set";
         throw new IllegalStateException(message);
      }

      return game;
   }


   private List<Team> getTeams () {

      Game game = getGame();
      return game.getTeams();
   }


   private TeamAdapter getTeamAdapter () {

      if (teamAdapter == null) {
         teamAdapter = new TeamAdapter();
         teamAdapter.setInteractionListener(this);
         teamAdapter.setTeams(getTeams());
      }

      return teamAdapter;
   }


   // region Views


   private void initializeViews () {

      RecyclerView teamsRecyclerView = getView(R.id.recyclerView_teams);
      teamsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
      teamsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
      teamsRecyclerView.setAdapter(getTeamAdapter());
   }


   // endregion


   private enum GameHolder {


      INSTANCE;


      private Game game;
   }
}
