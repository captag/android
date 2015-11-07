package eu.captag.app.lobby.widget;


import android.content.Context;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import eu.captag.R;
import eu.captag.model.Team;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class TeamView extends LinearLayout {


   // region Constants


   @SuppressWarnings("unused")
   public static final String TAG = GameView.class.getName();
   public static final int LAYOUT_ID = R.layout.widget_team;

   @SuppressWarnings("unused")
   private static final String LOG_TAG = TeamView.class.getSimpleName();


   // endregion
   // region Fields


   private InteractionListener interactionListener;
   private View colorView;
   private TextView nameView;
   private Team team;
   private TextView teamMembersView;


   // endregion


   /**
    * Inflates a new TeamView widget.
    * @param parent The view to be the parent of the inflated TeamView.
    * @param attachToParent Whether the inflated TeamView should be attached to the parent view.
    */
   public static TeamView inflate (ViewGroup parent, boolean attachToParent) {

      if (parent == null) {
         return null;
      }

      Context context = parent.getContext();
      LayoutInflater inflater = LayoutInflater.from(context);
      return (TeamView) inflater.inflate(LAYOUT_ID, parent, attachToParent);
   }


   // region Constructor


   public TeamView (Context context) {
      this(context, null);
   }


   public TeamView (Context context, AttributeSet attrs) {
      this(context, attrs, 0);
   }


   public TeamView (Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
   }


   // endregion


   @Override
   protected void onFinishInflate () {

      super.onFinishInflate();

      Button joinTeamButton = getView(R.id.button_joinTeam);
      joinTeamButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick (View v) {
            if (interactionListener != null) {
               interactionListener.onJoinTeamClicked(TeamView.this);
            }
         }
      });

      colorView = getView(R.id.view_color);
      nameView = getView(R.id.textView_title);
      teamMembersView = getView(R.id.textView_subtitle);
   }


   @Override
   public void setOnClickListener (View.OnClickListener listener) {
      // Ignore
   }


   public void bind (Team team) {

      if (team == null) {
         String message = "team must be not null";
         throw new IllegalArgumentException(message);
      }

      this.team = team;
      updateView();
   }


   public Team getTeam () {
      return team;
   }


   public void setInteractionListener (InteractionListener interactionListener) {
      this.interactionListener = interactionListener;
   }


   private void updateView () {

      Team team = getTeam();
      if (team == null) {
         String message = "team must be not null";
         throw new IllegalStateException(message);
      }

      // region Update the color view
      int color = team.getColor();
      colorView.setBackgroundColor(color);
      // endregion
      // region Update the name text view
      String name = team.getName();
      nameView.setText(name);
      // endregion
      // region Update the team members text view
      int teamMemberCount = team.getTeamMembers().size();
      String teamMembers = getResources().getQuantityString (
            R.plurals.teamMembers, teamMemberCount, teamMemberCount
      );
      teamMembersView.setText(teamMembers);
      // endregion
   }


   /**
    * Returns the view with the given id.
    * @param viewId The id of the view.
    */
   private  <T extends View> T getView (@IdRes int viewId) {

      View view = findViewById(viewId);
      // noinspection unchecked
      return (T) view;
   }


   public interface InteractionListener {


      /**
       * This method is called as soon as the user clicks on the join team button.
       * @param teamView The TeamView containing the clicked button.
       */
      void onJoinTeamClicked(TeamView teamView);
   }
}
