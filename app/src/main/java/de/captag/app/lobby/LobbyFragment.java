package de.captag.app.lobby;


import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import de.captag.R;
import de.captag.app.BaseFragment;
import de.captag.model.Game;
import de.captag.model.Team;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class LobbyFragment extends BaseFragment {


   // region Constants


   public static final String TAG = LobbyFragment.class.getName();
   public static final int LAYOUT_ID = R.layout.fragment_lobby;


   // endregion
   // region Fields


   private Callback callback;
   private Game game;
   private TeamPagerAdapter teamPagerAdapter;


   // endregion


   /**
    * Factory method for GameBrowserFragments.
    * @return A new instance of the GameBrowserFragment.
    */
   public static LobbyFragment newInstance () {
      return new LobbyFragment();
   }


   // region Fragment lifecycle


   @Override
   public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle inState) {
      return inflater.inflate(LAYOUT_ID, container, false);
   }


   @Override
   public void onViewCreated (View view, Bundle inState) {

      super.onViewCreated(view, inState);
      // Initialize the toolbar
      initializeToolbar();
   }


   // endregion
   // region View initialization


   private void initializeToolbar () {

      Toolbar toolbar = getView(R.id.toolbar);
      toolbar.setNavigationOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick (View v) {
            if (callback != null) {
               callback.onOpenNavigationDrawerClicked();
            }
         }
      });
   }


   private void initializeTeamsViewPager () {

      final TeamPagerAdapter adapter = getTeamPagerAdapter();

      // Initialize the tab layout
      final TabLayout tabLayout = getView(R.id.tabLayout);
      tabLayout.setTabsFromPagerAdapter(adapter);
      // Initialize the view pager
      final ViewPager viewPager = getView(R.id.viewPager_teams);
      viewPager.setAdapter(adapter);
      viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
      viewPager.addOnPageChangeListener(
            new ViewPager.SimpleOnPageChangeListener() {
               @Override
               public void onPageSelected (int position) {

                  super.onPageSelected(position);

                  Team team = adapter.getTeam(position);
                  tabLayout.setBackgroundColor(team.getColor());
               }
            }
      );

      // Set the tag layout selection listener
      tabLayout.setOnTabSelectedListener(
            new TabLayout.OnTabSelectedListener() {

               @Override
               public void onTabSelected (TabLayout.Tab tab) {
                  viewPager.setCurrentItem(tab.getPosition());
               }


               @Override
               public void onTabUnselected (TabLayout.Tab tab) {}


               @Override
               public void onTabReselected (TabLayout.Tab tab) {}
            }
      );

      // Update the tab layout background
      int position= viewPager.getCurrentItem();
      Team team = adapter.getTeam(position);
      tabLayout.setBackgroundColor(team.getColor());
   }


   // endregion


   /**
    * This method is called as soon as the teams have been loaded.
    * @param teams The loaded teams.
    */
   private void onLoadingTeamsDone (List<Team> teams) {

      // Update the team pager adapter
      TeamPagerAdapter adapter = getTeamPagerAdapter();
      adapter.setTeams(teams);
      // Initialize the teams view pager
      initializeTeamsViewPager();
   }


   public void setCallback (Callback callback) {
      this.callback = callback;
   }


   public Game getGame () {
      return game;
   }


   public void setGame (Game game) {

      this.game = game;
      retrieveTeams();
   }


   private TeamPagerAdapter getTeamPagerAdapter () {

      if (teamPagerAdapter == null) {
         FragmentManager fragmentManager = getFragmentManager();
         teamPagerAdapter = new TeamPagerAdapter(fragmentManager);
      }

      return teamPagerAdapter;
   }


   // region Communication with Captag Api


   private void retrieveTeams () {

      Game game = getGame();
      if (game == null) {
         TeamPagerAdapter adapter = getTeamPagerAdapter();
         adapter.setTeams(new ArrayList<Team>(0));
         return;
      }

      FindCallback<Team> callback = new FindCallback<Team>() {
         @Override
         public void done (List<Team> teams, ParseException e) {

            if (e == null) {
               onLoadingTeamsDone(teams);
               return;
            }

            String message = getString(R.string.error_loadingTeamsFailed);
            Snackbar snackbar = createErrorSnackbar(message, Snackbar.LENGTH_INDEFINITE, null);
            // @formatter:off
            snackbar.setAction(R.string.action_retry, new View.OnClickListener() {
               @Override
               public void onClick (View v) {
                  retrieveTeams();
               }
            });
            // @formatter:on
            snackbar.show();
         }
      };
      // Request the teams in background
      game.retrieveTeamsInBackground(callback);
   }


   // endregion


   /**
    * TODO Write documentation
    */
   public interface Callback {


      /**
       * This method is called as soon as the user clicks on the open navigation drawer button.
       */
      void onOpenNavigationDrawerClicked ();
   }
}
