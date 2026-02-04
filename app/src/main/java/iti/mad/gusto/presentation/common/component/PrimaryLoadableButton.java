package iti.mad.gusto.presentation.common.component;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import iti.mad.gusto.R;
import iti.mad.gusto.core.managers.VibrationManager;

public class PrimaryLoadableButton {
    private final ConstraintLayout holderLayout;
    private final ProgressBar progressBar;
    private final TextView textView;
    private String loadingText;
    private String defaultText;

    public PrimaryLoadableButton(View btnView, String defaultText, String loadingText) {
        this.holderLayout = btnView.findViewById(R.id.btn_content_holder);
        this.progressBar = btnView.findViewById(R.id.btn_progress_bar);
        this.textView = btnView.findViewById(R.id.btn_text);

        this.defaultText = defaultText;
        this.loadingText = loadingText;
        textView.setText(defaultText);
    }

    public void setOnClickListener(View.OnClickListener listener){
        holderLayout.setOnClickListener(listener);
    }

    public void setLoading(){
        holderLayout.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        textView.setText(loadingText);
    }

    public void setFinished(){
        progressBar.setVisibility(View.GONE);
        textView.setText(defaultText);
        holderLayout.setEnabled(true);
    }

    public void setFinishedWithVibration(Context context, int duration){
        progressBar.setVisibility(View.GONE);
        textView.setText(defaultText);
        holderLayout.setEnabled(true);

        VibrationManager.vibrate(context, duration);
    }

    public void setDefaultText(String text){
        this.defaultText = text;
    }
    public void setLoadingText(String text){
        this.loadingText = text;
    }
}