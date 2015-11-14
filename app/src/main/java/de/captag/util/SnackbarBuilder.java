package de.captag.util;


import android.support.design.widget.Snackbar;
import android.view.View;

import de.captag.R;


/**
 * @author Ulrich Raab
 */
public class SnackbarBuilder {


   public static final int STYLE_DEFAULT = 0;
   public static final int STYLE_ERROR = 1;
   public static final int STYLE_SUCCESS = 2;


   private Snackbar.Callback callback;
   private View container;
   private int duration;
   private String message;
   private int style;


   public SnackbarBuilder () {

      this.duration = Snackbar.LENGTH_SHORT;
      this.style = STYLE_DEFAULT;
   }


   public SnackbarBuilder callback (Snackbar.Callback callback) {

      this.callback = callback;
      return this;
   }


   public SnackbarBuilder container (View container) {

      if (container == null) {
         throw new IllegalArgumentException("container must be not null");
      }

      this.container = container;
      return this;
   }


   public SnackbarBuilder duration (int duration) {

      this.duration = duration;
      return this;
   }


   public SnackbarBuilder message (String message) {

      if (message == null || message.isEmpty()) {
         throw new IllegalArgumentException("message must be not null and not empty");
      }

      this.message = message;
      return this;
   }


   public SnackbarBuilder style (int style) {

      this.style = style;
      return this;
   }


   public Snackbar build () {

      Snackbar snackbar = Snackbar.make(container, message, duration);
      snackbar.setCallback(callback);
      View view = snackbar.getView();
      // Update the background color of the snackbar according to the style
      switch (style) {
         case STYLE_ERROR: {
            view.setBackgroundResource(R.color.captag_red);
            break;
         }
         case STYLE_SUCCESS: {
            view.setBackgroundResource(R.color.captag_green);
            break;
         }
      }

      return snackbar;
   }
}
