package de.captag.app.gamebrowser;


import android.content.Context;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;

import de.captag.R;
import de.captag.model.Game;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A view that represents a game.
 * @author Ulrich Raab
 */
public class GameView extends LinearLayout {


   // region Constants


   public static final int LAYOUT_ID = R.layout.view_game;


   // endregion
   // region Fields


   private Game game;

   private CircleImageView iconView;
   private TextView statusView;
   private TextView nameView;
   private TextView startDateView;


   // endregion


   /**
    * Inflates a new GameView widget.
    * @param parent The view to be the parent of the inflated GameView.
    * @param attachToParent Whether the inflated GameView should be attached to the parent view.
    */
   public static GameView inflate (ViewGroup parent, boolean attachToParent) {

      if (parent == null) {
         return null;
      }

      Context context = parent.getContext();
      LayoutInflater inflater = LayoutInflater.from(context);
      return (GameView) inflater.inflate(LAYOUT_ID, parent, attachToParent);
   }


   // region Constructor


   public GameView (Context context) {
      this(context, null);
   }


   public GameView (Context context, AttributeSet attrs) {
      this(context, attrs, 0);
   }


   public GameView (Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
   }


   // endregion


   @Override
   protected void onFinishInflate () {

      super.onFinishInflate();

      iconView = getView(R.id.circleImageView_icon);
      statusView = getView(R.id.textView_status);
      nameView = getView(R.id.textView_name);
      startDateView = getView(R.id.textView_startDate);
   }


   /**
    * Binds this view to the given game.
    * @param game The game to bind.
    */
   public void bind (Game game) {

      if (game == null) {
         String message = "game must be not null";
         throw new IllegalArgumentException(message);
      }

      this.game = game;
      updateView();
   }


   /**
    * Returns the game represented in this view.
    * @return The game represented in this view.
    */
   public Game getGame () {
      return game;
   }


   /**
    * Updates this view and all children using the bound game.
    */
   private void updateView () {

      Context context = getContext();
      Game game = getGame();
      if (game == null) {
         String message = "game must be not null";
         throw new IllegalStateException(message);
      }

      // region Update the icon view
      {
         String iconUrl = game.getIcon();
         if (iconUrl != null && !iconUrl.isEmpty()) {
            // @formatter:off
            Picasso
                  .with(context)
                  .load(game.getIcon())
                  .into(iconView);
            // @formatter:on
         }
      }
      // endregion
      // region Update the status view
      {
         String text = game.getStatus();
         statusView.setText(text);
      }
      // endregion
      // region Update the name view
      {
         String text = game.getName();
         nameView.setText(text);
      }
      // endregion
      // region Update the start date view
      {
         DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
         String text = dateFormat.format(game.getStartDate());
         startDateView.setText(text);
      }
      // endregion
   }


   /**
    * Returns the view with the given id.
    * @param viewId The id of the view.
    * @return The view if found or null otherwise.
    */
   private  <T extends View> T getView (@IdRes int viewId) {

      View view = findViewById(viewId);
      if (view == null) {
         return null;
      }
      // noinspection unchecked
      return (T) view;
   }
}
