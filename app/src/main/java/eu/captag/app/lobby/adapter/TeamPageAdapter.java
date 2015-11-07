package eu.captag.app.lobby.adapter;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import eu.captag.app.lobby.TeamMembersFragment;
import eu.captag.model.Team;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class TeamPageAdapter extends FragmentStatePagerAdapter {


   private List<Team> teams;


   public TeamPageAdapter (FragmentManager fragmentManager) {
      super(fragmentManager);
   }


   @Override
   public Fragment getItem (int position) {

      Team team = getTeam(position);
      // Create the fragment
      TeamMembersFragment fragment = new TeamMembersFragment();
      fragment.setTeam(team);
      return fragment;
   }


   @Override
   public int getCount () {
      return getTeams().size();
   }


   public Team getTeam (int position) {
      return getTeams().get(position);
   }


   public List<Team> getTeams () {

      if (teams == null) {
         teams = new ArrayList<>();
      }

      return teams;
   }


   public void setTeams (List<Team> teams) {
      this.teams = teams;
   }
}
