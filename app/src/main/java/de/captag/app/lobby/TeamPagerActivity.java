package de.captag.app.lobby;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.util.List;

import de.captag.R;
import de.captag.app.BaseActivity;
import de.captag.app.game.GameMapActivity;
import de.captag.app.lobby.adapter.TeamPageAdapter;
import de.captag.model.Game;
import de.captag.model.Player;
import de.captag.model.Team;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class TeamPagerActivity extends BaseActivity {


   private Button joinTeamButton;
   private Button leaveTeamButton;
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
      // Retrieve the game data
      Game game = getGame();
      retrieveTeams(game);
   }


   // endregion


   private void onLoadingDataDone () {

      initializeHeader();
      initializeTeamPager();
      initializeFooter();
   }


   private void onJoinTeamClicked () {

      // Subscript for notification
      ViewPager viewPager = getView(R.id.viewPager);
      final TeamPageAdapter teamPageAdapter = getTeamPageAdapter();

      Game game = getGame();
      Team team = teamPageAdapter.getTeam(viewPager.getCurrentItem());
      ParseUser user = getUser();

      // Create a new player
      Player player = new Player();
      player.setGame(game);
      player.setTeam(team);
      player.setUser(user);
      // Save the player object in background
      player.saveInBackground(new SaveCallback() {
         @Override
         public void done (ParseException e) {
            if (e == null) {

               // Update the team page adapter
               teamPageAdapter.notifyDataSetChanged();
               // Subscript for notifications
               Game game = getGame();
               ParsePush.subscribeInBackground(game.getObjectId());

            } else {
               String message = getString(R.string.error_joiningTeamFailed);
               showErrorSnackbar(message, Snackbar.LENGTH_LONG);
            }
         }
      });
   }


   private void onLeaveTeamClicked () {

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

      // region Initialize the join team button
      joinTeamButton = getView(R.id.button_joinTeam);
      joinTeamButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick (View v) {
            onJoinTeamClicked();
         }
      });
      // endregion
      // region Initialize the leave team button
      leaveTeamButton = getView(R.id.button_leaveTeam);
      leaveTeamButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick (View v) {
            onLeaveTeamClicked();
         }
      });
      // endregion
   }


   private void initializeTeamPager () {

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
      viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
         @Override
         public void onPageSelected (int position) {

            super.onPageSelected(position);

            TeamPageAdapter teamPageAdapter = getTeamPageAdapter();
            Team team = teamPageAdapter.getTeam(position);
            if (team.isTeamMember(getUser())) {
               joinTeamButton.setVisibility(View.GONE);
               leaveTeamButton.setVisibility(View.VISIBLE);
            } else {
               joinTeamButton.setVisibility(View.VISIBLE);
               leaveTeamButton.setVisibility(View.GONE);
            }

            Log.wtf("WTF", "onPageSelected(" + position + ")");
         }
      });

      // Set the tag layout selection listner
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
   // region Communication with Captag Api


   private void retrieveTeams (Game game) {

      game.retrieveTeamsInBackground(new FindCallback<Team>() {
         @Override
         public void done (List<Team> teams, ParseException e) {
            if (e == null) {
               onLoadingDataDone();
            } else {
               // Show error message
               String message = getString(R.string.error_loadingTeamsFailed);
               showErrorSnackbar(message, Snackbar.LENGTH_INDEFINITE);
            }
         }
      });
   }


   // endregion


   private enum GameHolder {


      INSTANCE;


      private Game game;
   }
}
