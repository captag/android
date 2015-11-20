package de.captag.app;


import android.app.Fragment;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.parse.ParseUser;

import de.captag.util.SnackbarBuilder;


/**
 * Base class for all fragments in captag.
 * @author Ulrich Raab
 */
public class BaseFragment extends Fragment {



   /**
    * Returns the currently logged in user.
    * @return The currently logged in user.
    */
   public ParseUser getUser () {
      return ParseUser.getCurrentUser();
   }


   /**
    * Returns the view with the given id.
    * @param viewId The id of the view.
    * @return The view if found or null otherwise.
    */
   public <T> T getView (@IdRes int viewId) {

      View root = getView();
      if (root == null) {
         return null;
      }

      View view = root.findViewById(viewId);
      if (view == null) {
         return null;
      }

      // noinspection unchecked
      return (T) view;
   }


   // region Snackbar


   /**
    * Call this method th create a snackbar.
    * @param message The message to show.
    * @param duration The duration the message should be shown.
    * @param callback The callback to be called when the visibility of the Snackbar changes.
    */
   public Snackbar createSnackbar (String message, int duration, Snackbar.Callback callback) {

      View container = getView();
      int style = SnackbarBuilder.STYLE_DEFAULT;
      // @formatter:off
      return new SnackbarBuilder()
            .callback(callback)
            .container(container)
            .duration(duration)
            .message(message)
            .style(style)
            .build();
      // @formatter:on
   }


   /**
    * Call this method th show a snackbar.
    * @param message The message to show.
    * @param duration The duration the message should be shown.
    */
   public void showSnackbar (String message, int duration) {
      showSnackbar(message, duration, null);
   }


   /**
    * Call this method th show a snackbar.
    * @param message The message to show.
    * @param duration The duration the message should be shown.
    * @param callback The callback to be called when the visibility of the Snackbar changes.
    */
   public void showSnackbar (String message, int duration, Snackbar.Callback callback) {

      Snackbar snackbar = createSnackbar(message, duration, callback);
      snackbar.show();
   }


   /**
    * Call this method th create a error snackbar.
    * @param message The message to show.
    * @param duration The duration the message should be shown.
    * @param callback The callback to be called when the visibility of the Snackbar changes.
    */
   public Snackbar createErrorSnackbar (String message, int duration, Snackbar.Callback callback) {

      View container = getView();
      int style = SnackbarBuilder.STYLE_ERROR;
      // @formatter:off
      return new SnackbarBuilder()
            .callback(callback)
            .container(container)
            .duration(duration)
            .message(message)
            .style(style)
            .build();
      // @formatter:on
   }


   /**
    * Call this method th show a error snackbar.
    * @param message The message to show.
    * @param duration The duration the message should be shown.
    */
   public void showErrorSnackbar (String message, int duration) {
      showErrorSnackbar(message, duration, null);
   }


   /**
    * Call this method th show a error snackbar.
    * @param message The message to show.
    * @param duration The duration the message should be shown.
    * @param callback The callback to be called when the visibility of the Snackbar changes.
    */
   public void showErrorSnackbar (String message, int duration, Snackbar.Callback callback) {

      Snackbar snackbar = createErrorSnackbar(message, duration, callback);
      snackbar.show();
   }


   /**
    * Call this method th create a success snackbar.
    * @param message The message to show.
    * @param duration The duration the message should be shown.
    * @param callback The callback to be called when the visibility of the Snackbar changes.
    */
   public Snackbar createSuccessSnackbar (String message, int duration, Snackbar.Callback callback) {

      View container = getView();
      int style = SnackbarBuilder.STYLE_SUCCESS;
      // @formatter:off
      return new SnackbarBuilder()
            .callback(callback)
            .container(container)
            .duration(duration)
            .message(message)
            .style(style)
            .build();
      // @formatter:on
   }


   /**
    * Call this method th show a success snackbar.
    * @param message The message to show.
    * @param duration The duration the message should be shown.
    */
   public void showSuccessSnackbar (String message, int duration) {
      showSuccessSnackbar(message, duration, null);
   }


   /**
    * Call this method th show a success snackbar.
    * @param message The message to show.
    * @param duration The duration the message should be shown.
    * @param callback The callback to be called when the visibility of the Snackbar changes.
    */
   public void showSuccessSnackbar (String message, int duration, Snackbar.Callback callback) {

      Snackbar snackbar = createSuccessSnackbar(message, duration, callback);
      snackbar.show();
   }


   // endregion
}
