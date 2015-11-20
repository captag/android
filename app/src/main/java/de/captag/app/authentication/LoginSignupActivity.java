package de.captag.app.authentication;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import de.captag.R;
import de.captag.app.MainActivity;


/**
 * TODO Write documentation
 * @author Ulrich Raab
 */
public class LoginSignupActivity extends Activity {


   // region Constants


   public static final String TAG = LoginSignupActivity.class.getName();
   public static final int LAYOUT_ID = R.layout.activity_login_signup;


   // endregion
   // region Fields


   private LoginSignupPagerAdapter loginSignupPagerAdapter;


   // endregion


   /**
    * Launches this activity.
    * @param activity The activity which launches this activity.
    * @throws IllegalArgumentException If activity is null.
    */
   public static void start (Activity activity) {

      if (activity == null) {
         String message = "activity must be not null";
         throw new IllegalArgumentException(message);
      }

      Intent intent = new Intent(activity, LoginSignupActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
      activity.startActivity(intent);
   }


   // region Activity lifecycle


   @Override
   protected void onCreate (Bundle inState) {

      super.onCreate(inState);

      // Check if the user is already logged in and skip the login/signup process if true
      ParseUser user = ParseUser.getCurrentUser();
      if (user != null) {
         MainActivity.start(this);
      } else {
         setContentView(LAYOUT_ID);
         // Initialize the views
         initializeLoginFab();
         initializeSignupFab();
         initializeLoginSignupViewPager();
      }
   }


   // endregion
   // region View initialization


   private void initializeLoginFab () {

      FloatingActionButton fab = getView(R.id.floatingActionButton_login);
      fab.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick (View v) {
            onLoginClicked();
         }
      });
   }


   private void initializeSignupFab () {

      FloatingActionButton fab = getView(R.id.floatingActionButton_signup);
      fab.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick (View v) {
            onSignupClicked();
         }
      });
   }


   private void initializeLoginSignupViewPager () {

      // region Get the pager adapter
      final LoginSignupPagerAdapter adapter = getLoginSignupPagerAdapter();
      // endregion
      // region Initialize the tab layout
      TabLayout tabLayout = getView(R.id.tabLayout);
      tabLayout.setTabsFromPagerAdapter(adapter);
      // endregion
      // region Initialize the view pager
      final ViewPager viewPager = getView(R.id.viewPager_loginSignup);
      viewPager.setAdapter(adapter);
      viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
      viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

         @Override
         public void onPageSelected (int position) {

            super.onPageSelected(position);

            switch (position) {
               case LoginSignupPagerAdapter.POSITION_LOGIN_PAGE: {

                  // Hide the signup fab
                  FloatingActionButton signupFab = getView(R.id.floatingActionButton_signup);
                  signupFab.hide();
                  // Show the login fab
                  viewPager.postDelayed(new Runnable() {
                     @Override
                     public void run () {
                        FloatingActionButton loginFab = getView(R.id.floatingActionButton_login);
                        loginFab.show();
                     }
                  }, 200);

                  break;
               }
               case LoginSignupPagerAdapter.POSITION_SIGNUP_PAGE: {

                  // Hide the login fab
                  FloatingActionButton loginFab = getView(R.id.floatingActionButton_login);
                  loginFab.hide();
                  // Show the signup fab
                  viewPager.postDelayed(new Runnable() {
                     @Override
                     public void run () {
                        FloatingActionButton signupFab = getView(R.id.floatingActionButton_signup);
                        signupFab.show();
                     }
                  }, 200);

                  break;
               }
            }
         }
      });
      // endregion
      // region Set the tag layout selection listener
      tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

         @Override
         public void onTabSelected (TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition());
         }


         @Override
         public void onTabUnselected (TabLayout.Tab tab) {}


         @Override
         public void onTabReselected (TabLayout.Tab tab) {}
      });
      // endregion
   }


   // endregion


   private void onLoginClicked () {

      LoginFragment loginFragment = getLoginFragment();
      // Get the entered username and password
      String username = loginFragment.getUsername();
      String password = loginFragment.getPassword();

      LogInCallback callback = new LogInCallback() {
         @Override
         public void done (ParseUser user, ParseException e) {
            if (user != null) {
               MainActivity.start(LoginSignupActivity.this);
            } else {
               Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
               vibrator.vibrate(500);
            }
         }
      };

      ParseUser.logInInBackground(username, password, callback);
   }


   private void onSignupClicked () {

      SignupFragment fragment = getSignupFragment();
      // Get the entered username, email and password
      String username = fragment.getUsername();
      String email = fragment.getEmail();
      String password = fragment.getPassword();
      // Create a new user object
      ParseUser user = new ParseUser();
      user.setUsername(username);
      user.setEmail(email);
      user.setPassword(password);

      SignUpCallback callback = new SignUpCallback() {
         @Override
         public void done (ParseException e) {
            if (e == null) {
               MainActivity.start(LoginSignupActivity.this);
            } else {
               Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
               vibrator.vibrate(500);
            }
         }
      };

      user.signUpInBackground(callback);
   }


   private LoginSignupPagerAdapter getLoginSignupPagerAdapter () {

      if (loginSignupPagerAdapter == null) {
         FragmentManager fragmentManager = getFragmentManager();
         loginSignupPagerAdapter = new LoginSignupPagerAdapter(this, fragmentManager);
      }

      return loginSignupPagerAdapter;
   }


   private LoginFragment getLoginFragment () {

      LoginSignupPagerAdapter adapter = getLoginSignupPagerAdapter();
      ViewPager viewPager = getView(R.id.viewPager_loginSignup);
      int position = LoginSignupPagerAdapter.POSITION_LOGIN_PAGE;

      return (LoginFragment) adapter.instantiateItem(viewPager, position);
   }


   private SignupFragment getSignupFragment () {

      LoginSignupPagerAdapter adapter = getLoginSignupPagerAdapter();
      ViewPager viewPager = getView(R.id.viewPager_loginSignup);
      int position = LoginSignupPagerAdapter.POSITION_SIGNUP_PAGE;

      return (SignupFragment) adapter.instantiateItem(viewPager, position);
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
