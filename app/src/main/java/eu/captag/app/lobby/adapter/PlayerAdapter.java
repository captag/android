package eu.captag.app.lobby.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eu.captag.app.lobby.widget.PlayerView;
import eu.captag.model.Player;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {


   private List<Player> players;


   @Override
   public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
      return null;
   }


   @Override
   public void onBindViewHolder (ViewHolder holder, int position) {

   }


   @Override
   public int getItemCount () {
      return 0;
   }


   public Player getPlayer (int position) {
      return getPlayers().get(position);
   }


   public List<Player> getPlayers () {

      if (players == null) {
         players = new ArrayList<>();
      }

      return players;
   }


   public void setPlayers (List<Player> players) {
      this.players = players;
   }


   public static class ViewHolder extends RecyclerView.ViewHolder {


      private PlayerView playerView;


      public ViewHolder (PlayerView playerView) {

         super(playerView);
         this.playerView = playerView;
      }
   }
}
