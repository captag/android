package eu.captag.app.lobby;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import eu.captag.R;
import eu.captag.app.BaseActivity;
import eu.captag.app.lobby.adapter.TeamPageAdapter;
import eu.captag.model.Game;
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
      // Initialize the team pager
      initializeViews();
   }


   // endregion


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
