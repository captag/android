package eu.captag.app.lobby;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

import eu.captag.R;
import eu.captag.app.BaseActivity;
import eu.captag.app.BaseFragment;
import eu.captag.app.lobby.adapter.PlayerAdapter;
import eu.captag.model.Player;
import eu.captag.model.Team;
import eu.captag.util.DividerItemDecoration;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class TeamMembersFragment extends BaseFragment {


   private Team team;
   private PlayerAdapter teamMemberAdapter;


   // region Fragment lifecycle


   @Override
   public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle inState) {
      return inflater.inflate(R.layout.fragment_team_members, container, false);
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

      Team team = getTeam();
      team.getTeamMembersInBackground(new FindCallback<Player>() {
         @Override
         public void done (List<Player> players, ParseException e) {
            if (players != null) {
               PlayerAdapter teamMemberAdapter = getTeamMemberAdapter();
               teamMemberAdapter.setPlayers(players);
               teamMemberAdapter.notifyDataSetChanged();
            } else {

               String message = getString(R.string.error_loadingTeamMembersFailed);
               BaseActivity activity = (BaseActivity) getActivity();
               activity.showErrorSnackbar(message, Snackbar.LENGTH_LONG);
            }
         }
      });
   }


   // endregion
}
