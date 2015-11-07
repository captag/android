package eu.captag.app.lobby.adapter;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import eu.captag.model.Team;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class TeamPagerAdapter extends FragmentStatePagerAdapter {


   private List<Team> teams;


   public TeamPagerAdapter (FragmentManager fragmentManager) {
      super(fragmentManager);
   }


   @Override
   public Fragment getItem (int position) {
      return null;
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
