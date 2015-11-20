package de.captag.app.gamebrowser;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import de.captag.R;
import de.captag.app.BaseFragment;
import de.captag.model.Game;
import de.captag.util.DividerItemDecoration;


/**
 * Fragment that displays a collection of games.
 * @author Ulrich Raab
 */
public class GameBrowserFragment extends BaseFragment {


   // region Constants


   public static final String TAG = GameBrowserFragment.class.getName();
   public static final int LAYOUT_ID = R.layout.fragment_game_browser;


   // endregion
   // region Fields


   private Callback callback;
   private GameAdapter gameAdapter;


   // endregion


   /**
    * Factory method for GameBrowserFragments.
    * @return A new instance of the GameBrowserFragment.
    */
   public static GameBrowserFragment newInstance () {
      return new GameBrowserFragment();
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
      initializeToolbar();
      initializeGamesRecyclerView();
      // Request the games
      retrieveGames();
   }


   // endregion
   // region UI initialization


   private void initializeToolbar () {

      Toolbar toolbar = getView(R.id.toolbar);
      toolbar.setNavigationOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick (View v) {
            if (callback != null) {
               callback.onOpenNavigationDrawerClicked();
            }
         }
      });
   }


   private void initializeGamesRecyclerView () {

      Context context = getActivity();
      RecyclerView view = getView(R.id.recyclerView_games);
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
            getGameAdapter()
      );
      // @formatter:on
   }


   // endregion


   /**
    * This method is called as soon as the games have been loaded.
    * @param games The loaded games.
    */
   private void onLoadingGamesDone (List<Game> games) {

      GameAdapter gameAdapter = getGameAdapter();
      gameAdapter.setGames(games);
      gameAdapter.notifyDataSetChanged();
   }


   public void setCallback (Callback callback) {

      this.callback = callback;
      // Update the game adapter callback
      GameAdapter gameAdapter = getGameAdapter();
      gameAdapter.setCallback(callback);
   }


   private GameAdapter getGameAdapter () {

      if (gameAdapter == null) {
         gameAdapter = new GameAdapter();
      }

      return gameAdapter;
   }


   // region Communication with Captag Api


   private void retrieveGames () {

      FindCallback<Game> callback = new FindCallback<Game>() {
         @Override
         public void done (List<Game> games, ParseException e) {

            if (e == null) {
               onLoadingGamesDone(games);
               return;
            }

            String message = getString(R.string.error_loadingGamesFailed);
            Snackbar snackbar = createErrorSnackbar(message, Snackbar.LENGTH_INDEFINITE, null);
            // @formatter:off
            snackbar.setAction(R.string.action_retry, new View.OnClickListener() {
               @Override
               public void onClick (View v) {
                  retrieveGames();
               }
            });
            // @formatter:on
            snackbar.show();
         }
      };

      ParseQuery<Game> query = ParseQuery.getQuery(Game.class);
      query.whereNotEqualTo(Game.ATTRIBUTE_STATUS, Game.STATUS_FINISHED);
      query.findInBackground(callback);
   }


   // endregion


   public interface Callback extends GameAdapter.Callback {


      /**
       * This method is called as soon as the user clicks on the open navigation drawer button.
       */
      void onOpenNavigationDrawerClicked ();
   }
}
