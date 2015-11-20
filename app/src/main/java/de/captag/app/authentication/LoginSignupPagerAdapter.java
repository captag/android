package de.captag.app.authentication;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;

import de.captag.R;


/**
 * {@link FragmentPagerAdapter} implementation that represents a login- and a sign up page.
 * @author Ulrich Raab
 */
public class LoginSignupPagerAdapter extends FragmentStatePagerAdapter {


   // region Constants


   public static final int POSITION_LOGIN_PAGE = 0;
   public static final int POSITION_SIGNUP_PAGE = 1;


   // endregion
   // region Fields


   private Context context;


   // endregion


   public LoginSignupPagerAdapter (Context context, FragmentManager fm) {

      super(fm);

      if (context == null) {
         String message = "context must be not null";
         throw new IllegalArgumentException(message);
      }

      this.context = context;
   }


   @Override
   public Fragment getItem (int position) {

      switch (position) {
         case POSITION_LOGIN_PAGE: {
            return LoginFragment.newInstance();
         }
         case POSITION_SIGNUP_PAGE: {
            return SignupFragment.newInstance();
         }
      }

      String message = "position " + position + " is invalid";
      throw new RuntimeException(message);
   }


   @Override
   public CharSequence getPageTitle (int position) {

      switch (position) {
         case POSITION_LOGIN_PAGE: {
            return context.getString(R.string.title_fragment_login);
         }
         case POSITION_SIGNUP_PAGE: {
            return context.getString(R.string.title_fragment_signup);
         }
      }

      return super.getPageTitle(position);
   }


   @Override
   public int getCount () {
      return 2;
   }
}
