package eu.captag.app.lobby;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DateFormat;

import eu.captag.R;
import eu.captag.app.BaseActivity;
import eu.captag.app.game.GameMapActivity;
import eu.captag.app.lobby.adapter.TeamPageAdapter;
import eu.captag.model.Game;
import eu.captag.model.Player;
import eu.captag.model.Team;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class TeamPagerActivity extends BaseActivity {


   private TeamPageAdapter teamPageAdapter;


   public static void start (Activity activity, Game game) {

      if (activity == null) {
         String message = "activity must be not null";
         throw new IllegalArgumentException(message);
      }

      if (game == null) {
         String message = "game must be not null";
         throw new IllegalArgumentException(message);
      }

      GameHolder.INSTANCE.game = game;
      Intent intent = new Intent(activity, TeamPagerActivity.class);
      activity.startActivity(intent);
   }


   // region Activity lifecycle


   @Override
   public void onCreate (Bundle instanceState) {

      super.onCreate(instanceState);
      setContentView(R.layout.activity_team_pager);
      // Initialize
      initializeHeader();
      initializeViews();
      initializeFooter();
   }


   // endregion


   private void onLeaveGameClicked () {

      Game game = getGame();
      ParseUser user = ParseUser.getCurrentUser();
      // Unsubscribe for notification
      ParsePush.unsubscribeInBackground(game.getObjectId());
      // Delete player
      ParseQuery<Player> query = ParseQuery.getQuery(Player.class);
      query.whereEqualTo(Player.POINTER_USER, user);
      query.whereEqualTo(Player.POINTER_GAME, game);
      query.getFirstInBackground(new GetCallback<Player>() {
         @Override
         public void done (Player player, ParseException e) {
            if (player != null) {
               player.deleteInBackground();
               GameSelectionActivity.start(TeamPagerActivity.this);
            }
         }
      });
   }


   private void onPlayClicked () {

      Game game = getGame();
      GameMapActivity.start(this, game);
   }


   public Game getGame () {

      Game game = GameHolder.INSTANCE.game;
      if (game == null) {
         String message = "game not set";
         throw new IllegalStateException(message);
      }

      return game;
   }


   private TeamPageAdapter getTeamPageAdapter () {

      if (teamPageAdapter == null) {
         FragmentManager fragmentManager = getFragmentManager();
         teamPageAdapter = new TeamPageAdapter(fragmentManager);
         teamPageAdapter.setTeams(getGame().getTeams());
      }

      return teamPageAdapter;
   }


   // region Views


   private void initializeHeader () {

      Game game = getGame();

      TextView gameNameView = getView(R.id.textView_gameName);
      gameNameView.setText(game.getName());

      DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
      TextView gameStartDateView = getView(R.id.textView_gameStartDate);
      gameStartDateView.setText(dateFormat.format(game.getStartDate()));

      Button leaveGameButton = getView(R.id.button_leaveGame);
      leaveGameButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick (View v) {
            onLeaveGameClicked();
         }
      });
   }


   private void initializeViews () {

      TeamPageAdapter teamPageAdapter = getTeamPageAdapter();

      // Initialize the view pager and tab layout
      TabLayout tabLayout = getView(R.id.tabLayout);
      for (Team team : teamPageAdapter.getTeams()) {
         TabLayout.Tab tab = tabLayout.newTab();
         tab.setText(team.getName());
         tabLayout.addTab(tab);
      }

      final ViewPager viewPager = getView(R.id.viewPager);
      viewPager.setAdapter(teamPageAdapter);
      viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
      tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

         @Override
         public void onTabSelected (TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition());
         }


         @Override
         public void onTabUnselected (TabLayout.Tab tab) {}


         @Override
         public void onTabReselected (TabLayout.Tab tab) {}
      });
   }


   private void initializeFooter () {

      Game game = getGame();
      // Find the play button and set the click listener
      Button playButton = getView(R.id.button_play);
      playButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick (View v) {
            onPlayClicked();
         }
      });
      // Show or hide the play button according to the game status
      switch (game.getStatus()) {
         case Game.STATUS_NEW:
         case Game.STATUS_INFORMED:
         case Game.STATUS_FINISHED: {
            playButton.setVisibility(View.GONE);
            break;
         }
         case Game.STATUS_STARTED: {
            playButton.setVisibility(View.VISIBLE);
            break;
         }
      }
   }


   // endregion


   private enum GameHolder {


      INSTANCE;


      private Game game;
   }
}
