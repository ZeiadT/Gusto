package iti.mad.gusto.data.repo;

import android.content.Context;

import iti.mad.gusto.data.source.AuthFirebaseDatasource;

import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Single;

public class AuthRepository {
    private final AuthFirebaseDatasource authFirebaseDatasource;

    private AuthRepository(Context applicationContext) {
        authFirebaseDatasource = AuthFirebaseDatasource.getInstance(applicationContext);
    }

    private static AuthRepository instance;

    public static AuthRepository getInstance(Context applicationContext) {
        if (instance == null)
            instance = new AuthRepository(applicationContext);

        return instance;
    }

    public Single<FirebaseUser> createUserWithEmailAndPassword(String email, String password) {
        return authFirebaseDatasource.createUserWithEmailAndPassword(email, password);
    }
    public Single<FirebaseUser> signInWithEmailAndPassword(String email, String password) {
        return authFirebaseDatasource.signInWithEmailAndPassword(email, password);
    }

    public Single<FirebaseUser> authenticateWithGoogle(String idToken) {
        return authFirebaseDatasource.authenticateWithGoogle(idToken);
    }

    public Single<FirebaseUser> signInAnonymously() {
        return authFirebaseDatasource.anonymousSignIn();
    }

    public Boolean isAnonymousUser(){
        return authFirebaseDatasource.isAnonymousUser();
    }

    public Boolean isSignedIn() {
        return authFirebaseDatasource.isSignedIn();
    }

    public FirebaseUser getCurrentUser() {
        return authFirebaseDatasource.getCurrentUser();
    }

    public Single<Boolean> signOut() {
        return authFirebaseDatasource.signOut();
    }

}
