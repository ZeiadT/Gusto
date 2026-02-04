package iti.mad.gusto.data.repo;

import iti.mad.gusto.data.service.AuthFirebaseService;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Single;

public class AuthRepository {
    private final AuthFirebaseService authFirebaseService;

    private AuthRepository() {
        authFirebaseService = AuthFirebaseService.getInstance();
    }

    private static AuthRepository instance;

    public static AuthRepository getInstance() {
        if (instance == null)
            instance = new AuthRepository();

        return instance;
    }

    public Single<FirebaseUser> signInWithEmailAndPassword(String email, String password) {
        return authFirebaseService.signInWithEmailAndPassword(email, password);
    }

}
