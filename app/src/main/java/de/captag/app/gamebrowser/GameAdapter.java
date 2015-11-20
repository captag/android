package de.captag.app.gamebrowser;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.captag.model.Game;


/**
 * {@link RecyclerView.Adapter} implementation for games.
 * @author Ulrich Raab
 */
public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {


   private Callback callback;
   private List<Game> games;


   // region RecyclerView.Adapter implementation


   @Override
   public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

      View.OnClickListener listener = new View.OnClickListener() {
         @Override
         public void onClick (View v) {
            GameView gameView = (GameView) v;
            if (callback != null) {
               Game game = gameView.getGame();
               callback.onGameSelected(game);
            }
         }
      };

      GameView gameView = GameView.inflate(parent, false);
      gameView.setOnClickListener(listener);
      return new ViewHolder(gameView);
   }


   @Override
   public void onBindViewHolder (ViewHolder viewHolder, int position) {

      Game game = getGame(position);
      viewHolder.gameView.bind(game);
   }


   @Override
   public int getItemCount () {
      return getGames().size();
   }


   // endregion


   public void setCallback (Callback callback) {
      this.callback = callback;
   }


   /**
    * Returns the game at the given position.
    * @param position The position of the game to return.
    * @return The game at the given position.
    */
   public Game getGame (int position) {
      return getGames().get(position);
   }


   /**
    * Returns the games represented in the recycler view.
    * @return The games represented in the recycler view.
    */
   public List<Game> getGames () {

      if (games == null) {
         games = new ArrayList<>();
      }

      return games;
   }


   /**
    * Sets the games to represent in the recycler view.
    * @param games The games to represent in the recycler view.
    */
   public void setGames (List<Game> games) {
      this.games = games;
   }


   // region ViewHolder


   /**
    * {@link RecyclerView.ViewHolder} implementation which holds a GameView.
    */
   public static class ViewHolder extends RecyclerView.ViewHolder {


      public GameView gameView;


      public ViewHolder (GameView gameView) {

         super(gameView);
         this.gameView = gameView;
      }
   }


   // endregion


   public interface Callback {


      void onGameSelected (Game game);
   }
}
