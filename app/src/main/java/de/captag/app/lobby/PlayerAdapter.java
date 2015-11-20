package de.captag.app.lobby;


import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.captag.model.Player;


/**
 * {@link RecyclerView.Adapter} implementation for players.
 * @author Ulrich Raab
 */
public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {


   private List<Player> players;


   public PlayerAdapter () {
      setHasStableIds(true);
   }


   // region RecyclerView.Adapter implementation


   @Override
   public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

      PlayerView playerView = PlayerView.inflate(parent, false);
      return new ViewHolder(playerView);
   }


   @Override
   public void onBindViewHolder (ViewHolder viewHolder, int position) {

      Player player = getPlayer(position);
      viewHolder.playerView.bind(player);
   }


   @Override
   public long getItemId (int position) {

      Player player = getPlayer(position);
      return player.hashCode();
   }


   @Override
   public int getItemCount () {
      return getPlayers().size();
   }


   // endregion


   /**
    * Returns the player at the given position.
    * @param position The position of the player to return.
    * @return The player at the given position.
    */
   public Player getPlayer (int position) {
      return getPlayers().get(position);
   }


   /**
    * Returns the players represented in the recycler view.
    * @return The players represented in the recycler view.
    */
   public List<Player> getPlayers () {

      if (players == null) {
         players = new ArrayList<>();
      }

      return players;
   }


   /**
    * Sets the players to represent in the recycler view.
    * @param players The players to represent in the recycler view.
    */
   public void setPlayers (List<Player> players) {
      this.players = players;
   }


   // region ViewHolder


   /**
    * {@link RecyclerView.ViewHolder} implementation which holds a PlayerView.
    */
   public static class ViewHolder extends RecyclerView.ViewHolder {


      private PlayerView playerView;


      public ViewHolder (PlayerView playerView) {

         super(playerView);
         this.playerView = playerView;
      }
   }


   // endregion
}
