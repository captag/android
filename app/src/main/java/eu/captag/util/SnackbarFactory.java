package eu.captag.util;


import android.support.design.widget.Snackbar;
import android.view.View;


/**
 * Factory class for snackbars.
 * @author Ulrich Raab
 */
public class SnackbarFactory {


   private SnackbarFactory () {}


   /**
    * Creates a snackbar with a red background.
    * @param container The container view of the snackbar.
    * @param message The message to show.
    * @param duration How long to display the message. Either {@link Snackbar#LENGTH_SHORT}, {@link
    * Snackbar#LENGTH_LONG} or {@link Snackbar#LENGTH_INDEFINITE}.
    */
   public static Snackbar createErrorSnackbar (View container, String message, int duration) {
      return createErrorSnackbar(container, message, duration, null);
   }


   /**
    * Creates a snackbar with a red background.
    * @param container The container view of the snackbar.
    * @param message The message to show.
    * @param duration How long to display the message. Either {@link Snackbar#LENGTH_SHORT}, {@link
    * Snackbar#LENGTH_LONG} or {@link Snackbar#LENGTH_INDEFINITE}.
    * @param callback Callback to be called when the visibility of this Snackbar changes.
    */
   public static Snackbar createErrorSnackbar (View container, String message, int duration, Snackbar.Callback callback) {
      return createSnackbar(container, message, duration, callback, SnackbarBuilder.STYLE_ERROR);
   }


   /**
    * Creates a snackbar with a green background.
    * @param container The container view of the snackbar.
    * @param message The message to show.
    * @param duration How long to display the message. Either {@link Snackbar#LENGTH_SHORT}, {@link
    * Snackbar#LENGTH_LONG} or {@link Snackbar#LENGTH_INDEFINITE}.
    */
   public static Snackbar createSuccessSnackbar (View container, String message, int duration) {
      return createSuccessSnackbar(container, message, duration, null);
   }


   /**
    * Creates a snackbar with a green background.
    * @param container The container view of the snackbar.
    * @param message The message to show.
    * @param duration How long to display the message. Either {@link Snackbar#LENGTH_SHORT}, {@link
    * Snackbar#LENGTH_LONG} or {@link Snackbar#LENGTH_INDEFINITE}.
    * @param callback Callback to be called when the visibility of this Snackbar changes.
    */
   public static Snackbar createSuccessSnackbar (View container, String message, int duration, Snackbar.Callback callback) {
      return createSnackbar(container, message, duration, callback, SnackbarBuilder.STYLE_SUCCESS);
   }


   /**
    * Creates a snackbar.
    * @param container The container view of the snackbar.
    * @param message The message to show.
    * @param duration How long to display the message. Either {@link Snackbar#LENGTH_SHORT}, {@link
    * Snackbar#LENGTH_LONG} or {@link Snackbar#LENGTH_INDEFINITE}.
    */
   public static Snackbar createSnackbar (View container, String message, int duration) {
      return createSnackbar(container, message, duration, null);
   }


   /**
    * Creates a snackbar.
    * @param container The container view of the snackbar.
    * @param message The message to show.
    * @param duration How long to display the message. Either {@link Snackbar#LENGTH_SHORT}, {@link
    * Snackbar#LENGTH_LONG} or {@link Snackbar#LENGTH_INDEFINITE}.
    * @param callback Callback to be called when the visibility of this Snackbar changes.
    */
   public static Snackbar createSnackbar (View container, String message, int duration, Snackbar.Callback callback) {
      return createSnackbar(container, message, duration, callback, SnackbarBuilder.STYLE_DEFAULT);
   }


   private static Snackbar createSnackbar (View container, String message, int duration, Snackbar.Callback callback, int style) {

      return new SnackbarBuilder().callback(callback)
                                  .container(container)
                                  .duration(duration)
                                  .message(message)
                                  .style(style)
                                  .build();
   }
}
