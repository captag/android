package eu.captag.app.lobby.widget;


import android.content.Context;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;

import eu.captag.R;
import eu.captag.model.Game;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class GameView extends LinearLayout {


   // region Constants


   @SuppressWarnings("unused")
   public static final String TAG = GameView.class.getName();
   public static final int LAYOUT_ID = R.layout.widget_game;

   @SuppressWarnings("unused")
   private static final String LOG_TAG = GameView.class.getSimpleName();


   // endregion
   // region Fields


   private Game game;
   private ImageView iconView;
   private InteractionListener interactionListener;
   private TextView subtitleView;
   private TextView titleView;


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
      super.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick (View v) {
            if (interactionListener != null) {
               interactionListener.onGameClicked(GameView.this);
            }
         }
      });

      iconView = getView(R.id.imageView_icon);
      subtitleView = getView(R.id.textView_subtitle);
      titleView = getView(R.id.textView_title);
   }


   @Override
   public void setOnClickListener (OnClickListener listener) {
      // Ignore
   }


   public void bind (Game game) {

      if (game == null) {
         String message = "game must be not null";
         throw new IllegalArgumentException(message);
      }

      this.game = game;
      updateView();
   }


   public Game getGame () {
      return game;
   }


   public void setInteractionListener (InteractionListener interactionListener) {
      this.interactionListener = interactionListener;
   }


   private void updateView () {

      Game game = getGame();
      if (game == null) {
         String message = "game must be not null";
         throw new IllegalStateException(message);
      }

      Picasso.with(getContext())
            .load(game.getIcon())
            .placeholder(R.mipmap.ic_game_icon_placeholder_black_40dp)
            .error(R.mipmap.ic_game_icon_placeholder_black_40dp)
            .into(iconView);

      // region Update the subtitle text view
      DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
      String subtitle = dateFormat.format(game.getStartDate());
      subtitleView.setText(subtitle);
      // endregion
      // region Update the title text view
      String title = game.getName();
      titleView.setText(title);
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
       * This method is called as soon as the user clicks on the GameView.
       * @param gameView The clicked GameView.
       */
      void onGameClicked (GameView gameView);
   }
}
