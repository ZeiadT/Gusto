package iti.mad.gusto.data.service;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AuthFirebaseService {
    private static final String TAG = "AuthFirebaseService";
    private FirebaseAuth mAuth;

    private AuthFirebaseService() {
        mAuth = FirebaseAuth.getInstance();
    }

    private static AuthFirebaseService instance;

    public static AuthFirebaseService getInstance() {
        if (instance == null)
            instance = new AuthFirebaseService();

        return instance;
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public Single<FirebaseUser> signInWithEmailAndPassword(String email, String password) {

        Single<FirebaseUser> observable = Single.create(
                emitter -> {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = getCurrentUser();
                                    emitter.onSuccess(user);
                                } else {
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    emitter.onError(task.getException());
                                }
                            });

                });

        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
