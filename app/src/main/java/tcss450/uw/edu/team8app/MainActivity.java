package tcss450.uw.edu.team8app;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tcss450.uw.edu.team8app.model.Credentials;

/**
 * The entry activity for the application.
 *
 * @author Jim Phan akari0@uw.edu
 */
public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
    RegisterFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            if(findViewById(R.id.main_fragment_container) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.main_fragment_container, new LoginFragment())
                        .commit();
            }
        }

//        Intent myIntent = new Intent(this, HomeActivity.class);
//        startActivity(myIntent);
    }

    @Override
    public void onRegisterClicked() {
        //should we add a backstack to the login page?
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, new RegisterFragment());
        transaction.commit();
    }

    @Override
    public void onLoginSuccess(Credentials credentials) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(Credentials.CREDIT_TAG, credentials);
        startActivity(intent);
        //End this activity and remove it from the Activity back stack.
        finish();
    }

    @Override
    public void onWaitFragmentInteractionShow() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_fragment_container, new WaitFragment(), "WAIT")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onWaitFragmentInteractionHide() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();
    }

    @Override
    public void onRegisterSuccess(Credentials credentials) {
        tellUserToVerify(credentials);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_fragment_container, new LoginFragment()).addToBackStack(null)
                .commit();
    }

    @Override
    public void tellUserToVerify(Credentials credentials) {
        VerifyAccountDialog dialog = new VerifyAccountDialog();
        dialog.setWarningDialog(getString(R.string.notify_check_email));
        dialog.setEmail(credentials.getEmail());
        dialog.setUsername(credentials.getUsername());
        dialog.show(getSupportFragmentManager(), VerifyAccountDialog.DIALOG_TAG);
    }
}
