package de.captag.app;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import de.captag.R;
import de.captag.app.authentication.LoginSignupActivity;
import de.captag.app.game.GameFragment;
import de.captag.app.gamebrowser.GameBrowserFragment;
import de.captag.app.lobby.LobbyFragment;
import de.captag.model.Game;
import de.captag.widget.NavigationDrawerHeaderView;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class MainActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener {


   private static final int FRAGMENT_CONTAINER_ID = R.id.frameLayout_content;
   private static final int LAYOUT_ID = R.layout.activity_main;


   /**
    * Launches this activity.
    * @param activity The activity which launches this activity.
    */
   public static void start (Activity activity) {

      Intent intent = new Intent(activity, MainActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
      activity.startActivity(intent);
   }


   @Override
   protected void onCreate (Bundle inState) {

      super.onCreate(inState);
      setContentView(LAYOUT_ID);
      initializeNavigationDrawer();
      showGameBrowserFragment();
   }


   @Override
   public boolean onNavigationItemSelected (MenuItem item) {

      switch (item.getItemId()) {
         case R.id.action_logout: {

            LogOutCallback callback = new LogOutCallback() {
               @Override
               public void done (ParseException e) {
                  if (e == null) {
                     LoginSignupActivity.start(MainActivity.this);
                     finish();
                  } else {
                     // TODO
                  }
               }
            };

            ParseUser.logOutInBackground(callback);
            return true;
         }
      }

      return false;
   }


   private void initializeNavigationDrawer () {

      ParseUser user = ParseUser.getCurrentUser();

      NavigationView navigationView = getView(R.id.navigationView);
      navigationView.setNavigationItemSelectedListener(this);
      NavigationDrawerHeaderView headerView = (NavigationDrawerHeaderView) navigationView.getHeaderView(0);
      headerView.bind(user);
   }


   private void showGameBrowserFragment () {

      GameBrowserFragment.Callback callback = new GameBrowserFragment.Callback() {

         @Override
         public void onGameSelected (final Game game) {

            new Handler().postDelayed(new Runnable() {
               @Override
               public void run () {
                  showGameFragment(game);
               }
            }, 300);

            // showLobbyFragment(game);
         }

         @Override
         public void onOpenNavigationDrawerClicked () {
            openNavigationDrawer();
         }
      };

      GameBrowserFragment fragment = GameBrowserFragment.newInstance();
      fragment.setCallback(callback);
      // Show the game browser fragment
      showFragment(fragment, GameBrowserFragment.TAG);
   }


   private void showLobbyFragment (Game game) {

      if (game == null) {
         // TODO show error snackbar
         return;
      }

      LobbyFragment.Callback callback = new LobbyFragment.Callback() {
         @Override
         public void onOpenNavigationDrawerClicked () {
            openNavigationDrawer();
         }
      };

      LobbyFragment fragment = LobbyFragment.newInstance();
      fragment.setCallback(callback);
      fragment.setGame(game);
      // Show the lobby fragment
      showFragment(fragment, LobbyFragment.TAG);
   }


   private void showGameFragment (Game game) {

      if (game == null) {
         // TODO show error snackbar
         return;
      }

      GameFragment fragment = GameFragment.newInstance(game);
      showFragment(fragment, GameFragment.TAG);
   }


   private void showFragment (Fragment fragment, String tag) {

      FragmentManager manager = getFragmentManager();
      boolean addToBackStack = manager.findFragmentById(FRAGMENT_CONTAINER_ID) != null;

      FragmentTransaction transaction = manager.beginTransaction();
      transaction.setCustomAnimations(
         android.R.animator.fade_in,
         android.R.animator.fade_out
      );
      transaction.replace(FRAGMENT_CONTAINER_ID, fragment, tag);
      if (addToBackStack) {
         transaction.addToBackStack(null);
      }
      transaction.commit();
   }


   private void openNavigationDrawer () {

      DrawerLayout drawerLayout = getView(R.id.drawerLayout);
      drawerLayout.openDrawer(GravityCompat.START);
   }


   /**
    * Returns the view with the given id.
    * @param viewId The id of the view.
    * @return The view if found or null otherwise.
    */
   public <T> T getView (@IdRes int viewId) {

      View view = findViewById(viewId);
      if (view == null) {
         return null;
      }

      // noinspection unchecked
      return (T) view;
   }
}
