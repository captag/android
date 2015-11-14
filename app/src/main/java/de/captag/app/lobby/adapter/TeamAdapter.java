package de.captag.app.lobby.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.captag.app.lobby.widget.TeamView;
import de.captag.model.Team;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {


   private List<Team> teams;
   private InteractionListener interactionListener;


   @Override
   public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

      TeamView teamView = TeamView.inflate(parent, false);
      teamView.setInteractionListener(new TeamView.InteractionListener() {
         @Override
         public void onJoinTeamClicked (TeamView teamView) {
            if (interactionListener != null) {
               Team team = teamView.getTeam();
               interactionListener.onJoinTeamClicked(team);
            }
         }
      });

      return new ViewHolder(teamView);
   }


   @Override
   public void onBindViewHolder (ViewHolder viewHolder, int position) {

      Team team = getTeam(position);
      viewHolder.teamView.bind(team);
   }


   @Override
   public int getItemCount () {
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


   public void setInteractionListener (InteractionListener interactionListener) {
      this.interactionListener = interactionListener;
   }


   public static class ViewHolder extends RecyclerView.ViewHolder {


      private TeamView teamView;


      public ViewHolder (TeamView teamView) {

         super(teamView);
         this.teamView = teamView;
      }
   }


   public interface InteractionListener {
      void onJoinTeamClicked (Team team);
   }
}
