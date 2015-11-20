package de.captag.app.authentication;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import de.captag.R;
import de.captag.app.BaseFragment;



/**
 * This fragment displays a login form.
 * @author Ulrich Raab
 */
public class LoginFragment extends BaseFragment {


   // region Constants


   public static final String TAG = LoginFragment.class.getName();
   public static final int LAYOUT_ID = R.layout.fragment_login;


   // endregion


   /**
    * Creates a new LoginFragment instance.
    * @return The new LoginFragment instance.
    */
   public static LoginFragment newInstance () {
      return new LoginFragment();
   }


   // region Fragment lifecycle


   @Override
   public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle inState) {
      return inflater.inflate(LAYOUT_ID, container, false);
   }


   // endregion


   /**
    * Returns the entered password of the user.
    * @return The password or a empty String if no password was entered.
    */
   public String getPassword () {

      EditText view = getView(R.id.editText_password);
      if (view == null) {
         return "";
      }

      return view.getText().toString();
   }


   /**
    * Returns the entered username of the user.
    * @return The username or a empty String if no username was entered.
    */
   public String getUsername () {

      EditText view = getView(R.id.editText_username);
      if (view == null) {
         return "";
      }

      return view.getText().toString();
   }
}
