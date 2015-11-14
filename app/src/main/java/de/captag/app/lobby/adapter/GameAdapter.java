package de.captag.app.lobby.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.captag.app.lobby.widget.GameView;
import de.captag.model.Game;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {


   private List<Game> games;
   private InteractionListener interactionListener;


   @Override
   public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

      GameView gameView = GameView.inflate(parent, false);
      gameView.setInteractionListener(new GameView.InteractionListener() {
         @Override
         public void onGameClicked (GameView gameView) {
            if (interactionListener != null) {
               Game game = gameView.getGame();
               interactionListener.onJoinGameClicked(game);
            }
         }
      });

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


   private Game getGame (int position) {
      return getGames().get(position);
   }


   public List<Game> getGames () {

      if (games == null) {
         games = new ArrayList<>();
      }

      return games;
   }


   public void setGames (List<Game> games) {
      this.games = games;
   }


   public void setInteractionListener (InteractionListener interactionListener) {
      this.interactionListener = interactionListener;
   }


   public static class ViewHolder extends RecyclerView.ViewHolder {


      private GameView gameView;


      public ViewHolder (GameView gameView) {

         super(gameView);
         this.gameView = gameView;
      }
   }


   public interface InteractionListener {
      void onJoinGameClicked (Game game);
   }
}
