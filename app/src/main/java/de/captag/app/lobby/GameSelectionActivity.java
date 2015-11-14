package de.captag.app.lobby;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import de.captag.R;
import de.captag.app.BaseActivity;
import de.captag.app.lobby.adapter.GameAdapter;
import de.captag.model.Game;
import de.captag.model.Player;
import de.captag.util.DividerItemDecoration;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class GameSelectionActivity extends BaseActivity implements GameAdapter.InteractionListener {


   private GameAdapter gameAdapter;


   public static void start (Activity activity) {

      Intent intent = new Intent(activity, GameSelectionActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
      activity.startActivity(intent);
   }


   @Override
   protected void onCreate (Bundle savedInstanceState) {

      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_game_selection);

      // Initialization
      initializeViews();
      // Retrieve games
      retrieveGames();
   }


   @Override
   public void onJoinGameClicked (Game game) {

      TeamPagerActivity.start(this, game);

      /*
      for (Team team : game.getTeams()) {
         for (Player player : team.getTeamMembers()) {
            if (isCurrentUser(player)) {

               return;
            }
         }
      }

      if (Game.STATUS_STARTED.equals(game.getStatus())) {

         String message = getString(R.string.message_gameAlreadyStarted);
         showSnackbar(message, Snackbar.LENGTH_LONG);

      } else {
         JoinTeamActivity.start(this, game);
      }
      */
   }


   private void onLoadingGamesDone (List<Game> games) {

      GameAdapter adapter = getGameAdapter();
      adapter.setGames(games);
      adapter.notifyDataSetChanged();

      if (games.isEmpty()) {
         // Show error message
         String message = getString(R.string.error_noGamesFound);
         showErrorSnackbar(message, Snackbar.LENGTH_LONG);
      }
   }


   private boolean isCurrentUser (Player player) {

      ParseUser user = player.getUser();
      ParseUser currentUser = ParseUser.getCurrentUser();
      return user.getObjectId().equals(currentUser.getObjectId());
   }


   public GameAdapter getGameAdapter () {

      if (gameAdapter == null) {
         gameAdapter = new GameAdapter();
         gameAdapter.setInteractionListener(this);
      }

      return gameAdapter;
   }


   // region Views


   private void initializeViews () {

      DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
            this, DividerItemDecoration.VERTICAL
      );
      dividerItemDecoration.setDividerDrawable(this, R.drawable.divider_horizontal_inset_left_72dp);

      RecyclerView gamesRecyclerView = getView(R.id.recyclerView_games);
      gamesRecyclerView.addItemDecoration(dividerItemDecoration);
      gamesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
      gamesRecyclerView.setAdapter(getGameAdapter());
   }


   // endregion
   // region Communication with Captag Api


   /**
    * Retrieves a list of Games.
    */
   private void retrieveGames () {

      FindCallback<Game> callback = new FindCallback<Game>() {
         @Override
         public void done (List<Game> games, ParseException e) {

            if (e == null) {
               onLoadingGamesDone(games);
            } else {
               // Show error message
               String message = getString(R.string.error_loadingGamesFailed);
               showErrorSnackbar(message, Snackbar.LENGTH_LONG);
            }
         }
      };

      ParseQuery<Game> query = ParseQuery.getQuery(Game.class);
      query.whereNotEqualTo(Game.ATTRIBUTE_STATUS, Game.STATUS_FINISHED);
      query.findInBackground(callback);
   }


   // endregion
}
