package eu.captag.app;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import eu.captag.R;
import eu.captag.app.lobby.GameSelectionActivity;


public class SignUpActivity extends BaseActivity {


   public static void start (Activity activity) {

      Intent intent = new Intent(activity, SignUpActivity.class);
      activity.startActivity(intent);
   }


   @Override
   protected void onCreate (Bundle savedInstanceState) {

      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_sign_up);

      // Initialization
      initializeViews();
   }


   private void onLoginClicked () {
      LoginActivity.start(this);
   }


   private void onSignUpClicked () {

      String email = getEmail();
      String password = getPassword();
      String username = getUsername();

      SignUpCallback signUpCallback = new SignUpCallback() {
         @Override
         public void done (ParseException e) {
            if (e == null) {
               GameSelectionActivity.start(SignUpActivity.this);
            } else {
               // Sign up didn't succeed. Look at the ParseException
               // to figure out what went wrong
               // Hide the sign up button
               final FloatingActionButton signUpButton = getView(R.id.floatingActionButton_signUp);
               signUpButton.hide();
               // Hide the sign up button
               final Button loginButton = getView(R.id.button_login);
               loginButton.setVisibility(View.GONE);
               // Show a login failed error message
               String message = getString(R.string.error_signUpFailed);
               Snackbar.Callback snackbarCallback = new Snackbar.Callback() {
                  @Override
                  public void onDismissed (Snackbar snackbar, int event) {
                     // Show the login button
                     signUpButton.show();
                     // Show the sign up button
                     loginButton.setVisibility(View.VISIBLE);
                  }
               };
               showErrorSnackbar(message, Snackbar.LENGTH_LONG, snackbarCallback);
            }
         }
      };

      ParseUser user = new ParseUser();
      user.setUsername(username);
      user.setPassword(password);
      user.setEmail(email);

      user.signUpInBackground(signUpCallback);
   }


   private String getEmail () {
      EditText view = getView(R.id.editText_email);
      return view.getText().toString();
   }


   private String getPassword () {

      EditText view = getView(R.id.editText_password);
      return view.getText().toString();
   }


   private String getUsername () {

      EditText view = getView(R.id.editText_username);
      return view.getText().toString();
   }


   private void initializeViews () {

      // region Find the login button and set the click listener
      {
         Button loginButton = getView(R.id.button_login);
         loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
               onLoginClicked();
            }
         });
      }
      // endregion
      // region Find the sign up button and set the click listener
      {
         FloatingActionButton signUpButton = getView(R.id.floatingActionButton_signUp);
         signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
               onSignUpClicked();
            }
         });
      }
      // endregion
   }


}
