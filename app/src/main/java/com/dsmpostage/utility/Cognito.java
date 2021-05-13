package com.dsmpostage.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.NewPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.regions.Regions;
import com.dsmpostage.main.HomeActivity;
import com.dsmpostage.main.LoginActivity;
import com.dsmpostage.main.ResetPassword;

import java.io.Serializable;

public class Cognito {

    private String poolID = "ap-southeast-2_Li6mtJEjd";
    private String clientID = "7ssf34oku1ug890thv8dgpurvc";
    private String clientSecret = "10kd46djq7j5ge57f48u0ahjk7gibc0q1qglu77ctn0bu52hhck3";
//    private Regions awsRegion = Regions.valueOf("ap-southeast-2");
        private Regions awsRegion = Regions.AP_SOUTHEAST_2;

    private CognitoUserPool userPool;
    private CognitoUserAttributes userAttributes;       // Used for adding attributes to the user
    private Context appContext;
    private String userPassword;
    AppPreferences appPreferences;

    public Cognito(Context context){
        appContext = context;
        appPreferences=new AppPreferences(context);
        userPool = new CognitoUserPool(context, this.poolID, this.clientID, this.clientSecret, this.awsRegion);
        userAttributes = new CognitoUserAttributes();
    }
    public void userLogin(String userId, String password){
        //CognitoUser cognitoUser =  userPool.getUser(userId);
        CognitoUser cognitoUser =  userPool.getUser(userId);
        this.userPassword = password;
        cognitoUser.getSessionInBackground(authenticationHandler);
    }

    public void userLogout(Activity activity,String userId){
        CognitoUser cognitoUser =  userPool.getUser(userId);
        cognitoUser.signOut();
        activity.finishAffinity();
        appContext.startActivity(new Intent(appContext, LoginActivity.class));
    }
    // Callback handler for the sign-in process
    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {
            Log.d("Challange--->",continuation.getChallengeName());


            if ("NEW_PASSWORD_REQUIRED".equals(continuation.getChallengeName())) {


                appContext.startActivity(new Intent(appContext, ResetPassword.class)
                .putExtra("config", (Serializable) continuation)
                );
            }

//            CognitoUser cognitoUser =  userPool.getUser("yankitpatel91@gmail.com");
//            cognitoUser.forgotPasswordInBackground(new ForgotPasswordHandler() {
//                @Override
//                public void onSuccess() {
//                    Log.d("CHANGE PASSWORD SUCCESS","SUCCESS");
//                }
//
//                @Override
//                public void getResetCode(ForgotPasswordContinuation continuation) {
//
//                    Log.d("CHANGE PASSWORD RESET CODE---",continuation.getParameters().getDestination().toString());
//                    Log.d("CHANGE PASSWORD RESET---",continuation.getParameters().getAttributeName().toString());
//                   // continuation.continueTask();
//                }
//
//                @Override
//                public void onFailure(Exception exception) {
//                    Log.d("CHANGE ERROR---",exception.toString());
//                }
//            });


        }
        @Override
        public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
            Toast.makeText(appContext,"Sign in success", Toast.LENGTH_LONG).show();
            Log.d("SESSION--->",userSession.getIdToken().toString());
            System.out.println("Called Session------"+userSession.getRefreshToken().getToken());
            appPreferences.set("ACCESS_TOKEN",userSession.getAccessToken().getJWTToken());
            appPreferences.set("USERNAME",userSession.getUsername());
            appPreferences.set("ID_TOKEN",userSession.getIdToken().getJWTToken());
            appPreferences.set("REFRESH_TOKEN",userSession.getRefreshToken().getToken());

            appContext.startActivity(new Intent(appContext, HomeActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
            );
        }
        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
            // The API needs user sign-in credentials to continue
            AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, userPassword, null);
            // Pass the user sign-in credentials to the continuation
            authenticationContinuation.setAuthenticationDetails(authenticationDetails);
            // Allow the sign-in to continue
            authenticationContinuation.continueTask();

            System.out.println("Called");
        }
        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {
            // Multi-factor authentication is required; get the verification code from user
            //multiFactorAuthenticationContinuation.setMfaCode(mfaVerificationCode);
            // Allow the sign-in process to continue
            //multiFactorAuthenticationContinuation.continueTask();
        }
        @Override
        public void onFailure(Exception exception) {
            // Sign-in failed, check exception for the cause
            Toast.makeText(appContext,"Sign in Failure", Toast.LENGTH_LONG).show();
        }
    };
}
