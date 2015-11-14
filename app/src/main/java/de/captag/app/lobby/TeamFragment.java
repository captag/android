package de.captag.app.lobby;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

import de.captag.R;
import de.captag.app.BaseFragment;
import de.captag.app.lobby.adapter.PlayerAdapter;
import de.captag.model.Player;
import de.captag.model.Team;
import de.captag.util.DividerItemDecoration;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class TeamFragment extends BaseFragment {


   private Team team;
   private PlayerAdapter teamMemberAdapter;


   // region Fragment lifecycle


   @Override
   public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle inState) {
      return inflater.inflate(R.layout.fragment_team, container, false);
   }


   @Override
   public void onViewCreated (View view, Bundle instanceState) {

      super.onViewCreated(view, instanceState);
      initializeTeamMembersRecyclerView();
      retrieveTeamMembers();
   }


   // endregion


   public Team getTeam () {
      return team;
   }


   public void setTeam (Team team) {
      this.team = team;
   }


   public PlayerAdapter getTeamMemberAdapter () {

      if (teamMemberAdapter == null) {
         teamMemberAdapter = new PlayerAdapter();
      }

      return teamMemberAdapter;
   }


   // region Views


   private void initializeTeamMembersRecyclerView () {

      Context context = getActivity();
      RecyclerView recyclerView = getView(R.id.recyclerView_teamMembers);
      recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
      recyclerView.setLayoutManager(new LinearLayoutManager(context));
      recyclerView.setAdapter(getTeamMemberAdapter());
   }


   // endregion
   // region Communication with Captag Api


   private void retrieveTeamMembers () {

      // Get the team members
      Team team = getTeam();
      team.retrieveTeamMembersInBackground(new FindCallback<Player>() {
         @Override
         public void done (List<Player> teamMembers, ParseException e) {
            // Update the player adapter
            PlayerAdapter adapter = getTeamMemberAdapter();
            adapter.setPlayers(teamMembers);
            adapter.notifyDataSetChanged();
         }
      });
   }


   // endregion
}
