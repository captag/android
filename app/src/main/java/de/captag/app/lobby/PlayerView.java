package de.captag.app.lobby;


import android.content.Context;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import de.captag.R;
import de.captag.model.Player;
import de.captag.model.Team;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class PlayerView extends LinearLayout {


   // region Constants


   @SuppressWarnings("unused")
   public static final String TAG = PlayerView.class.getName();
   public static final int LAYOUT_ID = R.layout.view_player;

   @SuppressWarnings("unused")
   private static final String LOG_TAG = PlayerView.class.getSimpleName();


   // endregion
   // region Fields


   private Player player;
   private CircleImageView avatarView;
   private TextView usernameView;


   // endregion


   /**
    * Inflates a new PlayerView widget.
    * @param parent The view to be the parent of the inflated PlayerView.
    * @param attachToParent Whether the inflated PlayerView should be attached to the parent view.
    */
   public static PlayerView inflate (ViewGroup parent, boolean attachToParent) {

      if (parent == null) {
         return null;
      }

      Context context = parent.getContext();
      LayoutInflater inflater = LayoutInflater.from(context);
      return (PlayerView) inflater.inflate(LAYOUT_ID, parent, attachToParent);
   }


   // region Constructor


   public PlayerView (Context context) {
      this(context, null);
   }


   public PlayerView (Context context, AttributeSet attrs) {
      this(context, attrs, 0);
   }


   public PlayerView (Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
   }


   // endregion


   @Override
   protected void onFinishInflate () {

      super.onFinishInflate();

      avatarView = getView(R.id.circleImageView_avatar);
      usernameView = getView(R.id.textView_username);
   }


   public void bind (Player player) {

      if (player == null) {
         String message = "player must be not null";
         throw new IllegalArgumentException(message);
      }

      this.player = player;
      updateView();
   }


   public Player getPlayer () {
      return player;
   }


   private void updateView () {

      Player player = getPlayer();
      if (player == null) {
         String message = "player must be not null";
         throw new IllegalStateException(message);
      }

      ParseUser user = player.getUser();

      // region Update the avatar view
      {
         ParseFile file = user.getParseFile("avatar");
         if (file != null) {
            String url = file.getUrl();
            Picasso
               .with(getContext())
               .load(url)
               .into(avatarView);
         } else {
            Team team = player.getTeam();
            avatarView.setFillColor(team.getColor());
         }
      }
      // endregion
      // region Update the username text view
      {
         String text = user.getUsername();
         usernameView.setText(text);
      }
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
}
