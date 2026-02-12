package iti.mad.gusto.presentation.auth.activity;

public interface AuthActivityCommunicator {
    void navigateReplacementToAnotherActivity(Class<?> clazz);
    void navigateReplacementToAnotherActivityWithAnimation(Class<?> clazz);
}
