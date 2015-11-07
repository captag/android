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

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import junit.framework.Test;

import java.text.DateFormat;
import java.util.List;

import eu.captag.R;
import eu.captag.app.BaseActivity;
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
   }


   // endregion


   private void onLeaveGameClicked () {

      Game game = getGame();
      ParseUser user = ParseUser.getCurrentUser();

      ParseQuery<Player> playerQuery = ParseQuery.getQuery(Player.class);
      playerQuery.whereEqualTo(Player.RELATION_USER, user);
      playerQuery.whereEqualTo(Player.RELATION_GAME, game);

      playerQuery.getFirstInBackground(new GetCallback<Player>() {
         @Override
         public void done (Player player, ParseException e) {
            if (player != null) {
               player.deleteInBackground();
               GameSelectionActivity.start(TeamPagerActivity.this);
            }
         }
      });
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


   // endregion


   private enum GameHolder {


      INSTANCE;


      private Game game;
   }
}
