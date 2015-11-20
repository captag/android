package de.captag.widget;


import android.content.Context;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import de.captag.R;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class NavigationDrawerHeaderView extends LinearLayout {


   // region Fields


   private ParseUser user;


   // endregion


   // region Constructor


   public NavigationDrawerHeaderView (Context context) {
      this(context, null);
   }


   public NavigationDrawerHeaderView (Context context, AttributeSet attrs) {
      this(context, attrs, 0);
   }


   public NavigationDrawerHeaderView (Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
   }


   // endregion


   /**
    * Binds this view to the given user.
    * @param user The user to bind.
    */
   public void bind (ParseUser user) {

      if (user == null) {
         String message = "user must be not null";
         throw new IllegalArgumentException(message);
      }

      this.user = user;
      updateView();
   }


   /**
    * Returns the user represented in this view.
    * @return The user represented in this view.
    */
   private ParseUser getUser () {
      return user;
   }


   /**
    * Updates this view and all children using the bound user.
    */
   private void updateView () {

      ParseUser user = getUser();
      if (user == null) {
         String message = "user must be not null";
         throw new IllegalStateException(message);
      }

      // region Update the avatar view
      {
         CircleImageView view = getView(R.id.circleImageView_avatar);
         ParseFile file = user.getParseFile("avatar");
         if (file != null) {
            String url = file.getUrl();
            Picasso
               .with(getContext())
               .load(url)
               .into(view);
         }
      }
      // endregion
      // region Update the username view
      {
         TextView view = getView(R.id.textView_username);
         if (view != null) {
            String text = user.getUsername();
            view.setText(text);
         }
      }
      // endregion
      // region Update the email view
      {
         TextView view = getView(R.id.textView_email);
         if (view != null) {
            String text = user.getEmail();
            view.setText(text);
         }
      }
      // endregion
   }


   /**
    * Returns the view with the given id.
    * @param viewId The id of the view.
    * @return The view if found or null otherwise.
    */
   private  <T extends View> T getView (@IdRes int viewId) {

      View view = findViewById(viewId);
      if (view == null) {
         return null;
      }
      // noinspection unchecked
      return (T) view;
   }
}
