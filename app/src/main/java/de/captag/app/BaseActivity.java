package de.captag.app;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.parse.ParseUser;

import de.captag.R;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class BaseActivity extends Activity {


   private static final int[] SNACKBAR_CONTAINER_IDS = {
         R.id.coordinatorLayout,
         android.R.id.content
   };


   public ParseUser getUser () {
      return ParseUser.getCurrentUser();
   }


   // region Snackbar


   /**
    * Call this method th show a snackbar.
    * @param message The message to show.
    * @param duration The duration the message should be shown.
    */
   public void showSnackbar (String message, int duration) {
   }


   /**
    * Call this method th show a snackbar.
    * @param message The message to show.
    * @param duration The duration the message should be shown.
    * @param callback The snackbar callback to be notified when the snackbar is shown or dismissed.
    */
   public void showSnackbar (String message, int duration, Snackbar.Callback callback) {
   }


   /**
    * Call this method th show a error snackbar.
    * @param message The message to show.
    * @param duration The duration the message should be shown.
    */
   public void showErrorSnackbar (String message, int duration) {
   }


   /**
    * Call this method th show a success snackbar.
    * @param message The message to show.
    * @param duration The duration the message should be shown.
    * @param callback The snackbar callback to be notified when the snackbar is shown or dismissed.
    */
   public void showErrorSnackbar (String message, int duration, Snackbar.Callback callback) {
   }


   /**
    * Call this method th show a success snackbar.
    * @param message The message to show.
    * @param duration The duration the message should be shown.
    */
   public void showSuccessSnackbar (String message, int duration) {
   }


   /**
    * Call this method th show a success snackbar.
    * @param message The message to show.
    * @param duration The duration the message should be shown.
    * @param callback The snackbar callback to be notified when the snackbar is shown or dismissed.
    */
   public void showSuccessSnackbar (String message, int duration, Snackbar.Callback callback) {
   }


   /**
    * Returns the container view of the snackbar.
    */
   protected View getSnackbarContainer () {

      for (int snackbarContainerId : SNACKBAR_CONTAINER_IDS) {
         View container = getView(snackbarContainerId);
         if (container != null) {
            return container;
         }
      }

      return null;
   }


   // endregion


   /**
    * Returns the fragment with the given id.
    * @param id The id of the fragment.
    * @return The fragment if found or null otherwise.
    */
   public <T> T getFragment (@IdRes int id) {

      FragmentManager manager = getFragmentManager();
      Fragment fragment = manager.findFragmentById(id);
      if (fragment == null) {
         return null;
      }

      // noinspection unchecked
      return (T) fragment;
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
