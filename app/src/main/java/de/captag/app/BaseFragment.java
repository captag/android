package de.captag.app;


import android.app.Fragment;
import android.support.annotation.IdRes;
import android.view.View;

import com.parse.ParseUser;


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
}
