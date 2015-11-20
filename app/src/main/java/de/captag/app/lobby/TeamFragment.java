package de.captag.app.lobby;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import de.captag.R;
import de.captag.app.BaseFragment;
import de.captag.model.Player;
import de.captag.model.Team;
import de.captag.util.DividerItemDecoration;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class TeamFragment extends BaseFragment {


   // region Constants


   public static final int LAYOUT_ID = R.layout.fragment_team;


   // endregion
   // region Fields


   private Team team;
   private PlayerAdapter teamMemberAdapter;


   // endregion


   /**
    * Factory method for TeamFragments.
    * @return A new instance of the TeamFragment.
    */
   public static TeamFragment newInstance () {
      return new TeamFragment();
   }


   // region Fragment lifecycle


   @Override
   public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle inState) {
      return inflater.inflate(LAYOUT_ID, container, false);
   }


   @Override
   public void onViewCreated (View view, Bundle inState) {

      super.onViewCreated(view, inState);

      // Initialize the views
      initializeJoinTeamButton();
      initializeLeaveTeamButton();
      initializeTeamMembersRecyclerView();
   }


   // endregion
   // region View initialization


   private void initializeJoinTeamButton () {

      Button button = getView(R.id.button_joinTeam);
      button.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick (View v) {
            onJoinTeamClicked();
         }
      });
   }


   private void initializeLeaveTeamButton () {

      Button button = getView(R.id.button_leaveTeam);
      button.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick (View v) {
            onLeaveTeamClicked();
         }
      });
   }


   private void initializeTeamMembersRecyclerView () {

      Context context = getActivity();
      RecyclerView view = getView(R.id.recyclerView_teamMembers);
      // @formatter:off
      view.addItemDecoration(new DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL,
            R.drawable.divider_horizontal_inset_left_72dp
         )
      );
      view.setLayoutManager(
            new LinearLayoutManager(context)
      );
      view.setAdapter(
            getTeamMemberAdapter()
      );
      // @formatter:on
   }


   // endregion


   /**
    * This method is called as soon as the team members have been loaded.
    * @param teamMembers The loaded team members.
    */
   private void onLoadingTeamMembersDone (List<Player> teamMembers) {

      // region Update the team members subhead view
      {
         Resources resources = getResources();
         int count = teamMembers.size();
         String text = resources.getQuantityString(R.plurals.teamMembers, count, count);
         TextView view = getView(R.id.textView_subhead_teamMembers);
         view.setText(text);
      }
      // endregion
      // region Update the tem member adapter
      PlayerAdapter adapter = getTeamMemberAdapter();
      adapter.setPlayers(teamMembers);
      adapter.notifyDataSetChanged();
      // endregion
      // region Update the visibility of the join team and leave team buttons
      Button joinTeamButton = getView(R.id.button_joinTeam);
      Button leaveTeamButton = getView(R.id.button_leaveTeam);
      ParseUser user = getUser();
      Team team = getTeam();
      if (team.isTeamMember(user)) {
         joinTeamButton.setVisibility(View.GONE);
         leaveTeamButton.setVisibility(View.VISIBLE);
      } else {
         joinTeamButton.setVisibility(View.VISIBLE);
         leaveTeamButton.setVisibility(View.GONE);
      }
      // endregion
   }


   /**
    * This method is called as soon as the user clicks on the join team button.
    */
   private void onJoinTeamClicked () {

      SaveCallback callback = new SaveCallback() {
         @Override
         public void done (ParseException e) {

            if (e == null) {
               // Refresh the UI
               Team team = getTeam();
               List<Player> teamMembers = team.getTeamMembers();
               onLoadingTeamMembersDone(teamMembers);
               return;
            }

            // Show error message
            String message = getString(R.string.error_joiningTeamFailed);
            Snackbar snackbar = createErrorSnackbar(message, Snackbar.LENGTH_INDEFINITE, null);
            snackbar.setAction(R.string.action_retry, new View.OnClickListener() {
               @Override
               public void onClick (View v) {
                  onJoinTeamClicked();
               }
            });
            snackbar.show();
         }
      };

      joinTeam(callback);
   }


   /**
    * This method is called as soon as the user clicks on the leave team button.
    */
   private void onLeaveTeamClicked () {

      DeleteCallback callback = new DeleteCallback() {
         @Override
         public void done (ParseException e) {

            if (e == null) {
               // Refresh the UI
               Team team = getTeam();
               List<Player> teamMembers = team.getTeamMembers();
               onLoadingTeamMembersDone(teamMembers);
               return;
            }

            // Show error message
            String message = getString(R.string.error_leavingTeamFailed);
            Snackbar snackbar = createErrorSnackbar(message, Snackbar.LENGTH_INDEFINITE, null);
            snackbar.setAction(R.string.action_retry, new View.OnClickListener() {
               @Override
               public void onClick (View v) {
                  onLeaveTeamClicked();
               }
            });
            snackbar.show();
         }
      };

      leaveTeam(callback);
   }


   /**
    * Returns the team represented in this fragment.
    * @return The team represented in this fragment.
    */
   public Team getTeam () {
      return team;
   }


   /**
    * Sets the team which should be represented in the fragment.
    * @param team The team to represent.
    */
   public void setTeam (Team team) {

      this.team = team;
      retrieveTeamMembers();
   }


   public PlayerAdapter getTeamMemberAdapter () {

      if (teamMemberAdapter == null) {
         teamMemberAdapter = new PlayerAdapter();
      }

      return teamMemberAdapter;
   }


   // region Communication with Captag Api


   private void retrieveTeamMembers () {

      Team team = getTeam();
      if (team == null) {
         PlayerAdapter adapter = getTeamMemberAdapter();
         adapter.setPlayers(new ArrayList<Player>(0));
         return;
      }

      FindCallback<Player> callback = new FindCallback<Player>() {
         @Override
         public void done (List<Player> teamMembers, ParseException e) {

            if (e == null) {
               onLoadingTeamMembersDone(teamMembers);
               return;
            }

            String message = getString(R.string.error_loadingTeamMembersFailed);
            Snackbar snackbar = createErrorSnackbar(message, Snackbar.LENGTH_INDEFINITE, null);
            // @formatter:off
            snackbar.setAction(R.string.action_retry, new View.OnClickListener() {
               @Override
               public void onClick (View v) {
                  retrieveTeamMembers();
               }
            });
            // @formatter:on
            snackbar.show();
         }
      };
      // Request the team members in background
      team.retrieveTeamMembersInBackground(callback);
   }


   private void joinTeam (SaveCallback callback) {

      ParseUser user = getUser();
      Team team = getTeam();
      // Execute the request
      team.joinInBackground(user, callback);
   }


   private void leaveTeam (DeleteCallback callback) {

      ParseUser user = getUser();
      Team team = getTeam();
      // Execute the request
      team.leaveInBackground(user, callback);
   }


   // endregion
}
