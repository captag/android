package de.captag.app.authentication;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import de.captag.R;
import de.captag.app.BaseFragment;



/**
 * This fragment displays a sign up form.
 * @author Ulrich Raab
 */
public class SignupFragment extends BaseFragment {


   // region Constants


   public static final String TAG = SignupFragment.class.getName();
   public static final int LAYOUT_ID = R.layout.fragment_signup;


   // endregion


   /**
    * Creates a new SignupFragment instance.
    * @return The new SignupFragment instance.
    */
   public static SignupFragment newInstance () {
      return new SignupFragment();
   }


   // region Fragment lifecycle


   @Override
   public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle inState) {
      return inflater.inflate(LAYOUT_ID, container, false);
   }


   // endregion


   /**
    * Returns the entered username.
    * @return The username or a empty String if no username was entered.
    */
   public String getUsername () {

      EditText view = getView(R.id.editText_username);
      if (view == null) {
         return "";
      }

      return view.getText().toString();
   }


   /**
    * Returns the entered email address.
    * @return The email or a empty String if no email was entered.
    */
   public String getEmail () {

      EditText view = getView(R.id.editText_email);
      if (view == null) {
         return "";
      }

      return view.getText().toString();
   }


   /**
    * Returns the entered password.
    * @return The password or a empty String if no password was entered.
    */
   public String getPassword () {

      EditText view = getView(R.id.editText_password);
      if (view == null) {
         return "";
      }

      return view.getText().toString();
   }
}
