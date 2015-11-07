package eu.captag.app;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import eu.captag.R;
import eu.captag.app.lobby.GameSelectionActivity;


public class LoginActivity extends BaseActivity {


   public static void start (Activity activity) {

      Intent intent = new Intent(activity, LoginActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
      activity.startActivity(intent);
   }


   @Override
   protected void onCreate (Bundle savedInstanceState) {

      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_login);

      // Initialization
      initializeViews();
   }


   private void onLoginClicked () {

      String password = getPassword();
      String username = getUsername();

      LogInCallback callback = new LogInCallback() {
         @Override
         public void done (ParseUser user, ParseException e) {
            if (user != null) {
               GameSelectionActivity.start(LoginActivity.this);
            } else {
               // Hide the login button
               final FloatingActionButton loginButton = getView(R.id.floatingActionButton_login);
               loginButton.hide();
               // Hide the sign up button
               final Button signUpButton = getView(R.id.button_signUp);
               signUpButton.setVisibility(View.GONE);
               // Show a login failed error message
               String message = getString(R.string.error_loginFailed);
               Snackbar.Callback snackbarCallback = new Snackbar.Callback() {
                  @Override
                  public void onDismissed (Snackbar snackbar, int event) {
                     // Show the login button
                     loginButton.show();
                     // Show the sign up button
                     signUpButton.setVisibility(View.VISIBLE);
                  }
               };
               showErrorSnackbar(message, Snackbar.LENGTH_LONG, snackbarCallback);
            }
         }
      };

      ParseUser.logInInBackground(username, password, callback);
   }


   private void onSignUpClicked () {
      SignUpActivity.start(this);
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
         FloatingActionButton loginButton = getView(R.id.floatingActionButton_login);
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
         Button signUpButton = getView(R.id.button_signUp);
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
