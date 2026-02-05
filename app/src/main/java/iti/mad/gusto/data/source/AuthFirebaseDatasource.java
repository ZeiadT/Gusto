package iti.mad.gusto.data.source;

import android.content.Context;
import android.os.CancellationSignal;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.credentials.ClearCredentialStateRequest;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.ClearCredentialException;
import androidx.credentials.exceptions.GetCredentialException;

import com.google.android.gms.tasks.Task;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import iti.mad.gusto.R;

public class AuthFirebaseDatasource {
    private static final String TAG = "AuthFirebaseService";
    private final FirebaseAuth mAuth;
    private final CredentialManager credentialManager;

    private AuthFirebaseDatasource(Context applicationContext) {
        mAuth = FirebaseAuth.getInstance();
        credentialManager = CredentialManager.create(applicationContext);
    }

    private static AuthFirebaseDatasource instance;

    public static AuthFirebaseDatasource getInstance(Context applicationContext) {
        if (instance == null)
            instance = new AuthFirebaseDatasource(applicationContext);

        return instance;
    }

    public Single<FirebaseUser> createUserWithEmailAndPassword(String email, String password) {

        return Single.<FirebaseUser>create(
                        emitter -> {
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "signInWithEmailAndPassword:success");
                                            FirebaseUser user = getCurrentUser();
                                            emitter.onSuccess(user);
                                        } else {
                                            Log.w(TAG, "signInWithEmailAndPassword:failure", task.getException());
                                            emitter.onError(task.getException());
                                        }
                                    });

                        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<FirebaseUser> signInWithEmailAndPassword(String email, String password) {

        return Single.<FirebaseUser>create(
                        emitter -> {
                            mAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "signInWithEmailAndPassword:success");
                                            FirebaseUser user = getCurrentUser();
                                            emitter.onSuccess(user);
                                        } else {
                                            Log.w(TAG, "signInWithEmailAndPassword:failure", task.getException());
                                            emitter.onError(task.getException());
                                        }
                                    });

                        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<FirebaseUser> authenticateWithGoogle(String idToken) {
        return Single.fromCallable(() -> GoogleAuthProvider.getCredential(idToken, null))

                .flatMap(credential -> {
                    // 2. Sign in with Firebase and convert Task to Single
                    return googleTaskToSingle(mAuth.signInWithCredential(credential));
                })
                .map(authResult -> {

                    // 3. Extract the User object
                    if (authResult.getUser() == null) {
                        throw new Exception("Firebase User was null");
                    }

                    return authResult.getUser();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<FirebaseUser> anonymousSignIn() {
        return Single.<FirebaseUser>create(
                        emitter -> {
                            mAuth.signInAnonymously()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "anonymousSignIn:success");
                                            FirebaseUser user = getCurrentUser();
                                            emitter.onSuccess(user);
                                        } else {
                                            Log.w(TAG, "anonymousSignIn:failure", task.getException());
                                            emitter.onError(task.getException());
                                        }
                                    });
                        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public Single<Boolean> signOut() {

        return Single.<Boolean>create(emitter -> {

                    mAuth.signOut();

                    ClearCredentialStateRequest clearRequest = new ClearCredentialStateRequest();

                    credentialManager.clearCredentialStateAsync(
                            clearRequest,
                            new CancellationSignal(),
                            Executors.newSingleThreadExecutor(),
                            new CredentialManagerCallback<>() {
                                @Override
                                public void onResult(@NonNull Void result) {
                                    emitter.onSuccess(true);
                                }

                                @Override
                                public void onError(@NonNull ClearCredentialException e) {
                                    Log.e(TAG, "Couldn't clear user credentials: " + e.getLocalizedMessage());
                                    emitter.onError(e);
                                }
                            });
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static <T> Single<T> googleTaskToSingle(Task<T> task) {
        return Single.create(emitter -> {
            task.addOnSuccessListener(result -> {
                if (!emitter.isDisposed()) {
                    emitter.onSuccess(result);
                }
            }).addOnFailureListener(e -> {
                if (!emitter.isDisposed()) {
                    emitter.onError(e);
                }
            });
        });
    }

}
