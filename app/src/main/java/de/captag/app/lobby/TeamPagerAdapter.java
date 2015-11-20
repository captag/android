package de.captag.app.lobby;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import de.captag.model.Team;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class TeamPagerAdapter extends FragmentStatePagerAdapter {


   private List<Team> teams;


   public TeamPagerAdapter (FragmentManager fm) {
      super(fm);
   }


   // region FragmentPagerAdapter implementation


   @Override
   public Fragment getItem (int position) {

      Team team = getTeam(position);
      // Create a new team fragment
      TeamFragment fragment = TeamFragment.newInstance();
      fragment.setTeam(team);
      // Return the newly created team fragment
      return fragment;
   }


   @Override
   public int getCount () {
      return getTeams().size();
   }


   @Override
   public int getItemPosition (Object object) {
      return POSITION_NONE;
   }


   @Override
   public CharSequence getPageTitle (int position) {

      Team team = getTeam(position);
      return team.getName();
   }


   // endregion


   /**
    * Returns the team at the given position.
    * @param position The position of the team to return.
    * @return The team at the given position.
    */
   public Team getTeam (int position) {
      return getTeams().get(position);
   }


   /**
    * Returns the teams represented in the view pager.
    * @return The teams represented in the view pager.
    */
   public List<Team> getTeams () {

      if (teams == null) {
         teams = new ArrayList<>();
      }

      return teams;
   }


   /**
    * Sets the teams to represent in the view pager.
    * @param teams The games to represent in the view pager.
    */
   public void setTeams (List<Team> teams) {
      this.teams = teams;
   }
}
