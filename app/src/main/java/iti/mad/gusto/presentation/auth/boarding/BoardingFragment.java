package iti.mad.gusto.presentation.auth.boarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import iti.mad.gusto.R;
import iti.mad.gusto.data.model.BoardingItem;
import iti.mad.gusto.presentation.auth.login.LoginFragment;
import iti.mad.gusto.presentation.common.component.WormDotIndicator;
import iti.mad.gusto.presentation.common.util.AnimationUtil;

public class BoardingFragment extends Fragment implements BoardingContract.View {

    private static final String POSITION_KEY = "position";

    // UI Components
    private Button btnNext;
    private TextView btnPrev;
    private TextView btnSkip;
    private WormDotIndicator wormDotIndicator;
    private TextView titleTV;
    private TextView subTitleTV;
    private ImageView illustrationTV;
    private ConstraintLayout backgroundContainer;

    private BoardingContract.Presenter presenter;
    private BoardingItem lastItemRendered;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_boarding, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new BoardingPresenter(this);

        initUI(view);

        btnNext.setOnClickListener(v -> presenter.onNextClicked());
        btnPrev.setOnClickListener(v -> presenter.onPrevClicked());
        btnSkip.setOnClickListener(v -> presenter.onSkipClicked());


        int startPos = (savedInstanceState != null) ? savedInstanceState.getInt(POSITION_KEY, 0) : 0;
        presenter.init(startPos);
    }

    private void initUI(View view) {
        btnNext = view.findViewById(R.id.btn_next);
        btnPrev = view.findViewById(R.id.btn_prev);
        btnSkip = view.findViewById(R.id.btn_skip);
        wormDotIndicator = view.findViewById(R.id.dots_indicator);
        titleTV = view.findViewById(R.id.tv_title);
        subTitleTV = view.findViewById(R.id.tv_subtitle);
        illustrationTV = view.findViewById(R.id.iv_boarding);
        backgroundContainer = view.findViewById(R.id.bg_container);

        wormDotIndicator.setCount(presenter.getCount());
    }

    @Override
    public void renderPage(BoardingItem newItem, boolean animate, boolean isMovingForward) {
        String title = getString(newItem.getTitleRes());
        String desc = getString(newItem.getDescRes());
        int color = ContextCompat.getColor(requireContext(), newItem.getBackgroundColorRes());

        wormDotIndicator.selectDot(presenter.getCurrentPosition(), animate);

        if (animate && lastItemRendered != null) {
            int oldColor = ContextCompat.getColor(requireContext(), lastItemRendered.getBackgroundColorRes());

            AnimationUtil.animateBackgroundColor(backgroundContainer, oldColor, color, 300);
            AnimationUtil.animateTextFadeChange(titleTV, title, 300);
            AnimationUtil.animateTextFadeChange(subTitleTV, desc, 300);
            AnimationUtil.animateImageFadeSlideChange(illustrationTV, newItem.getImageRes(), isMovingForward, 300);
        } else {
            titleTV.setText(title);
            subTitleTV.setText(desc);
            illustrationTV.setImageResource(newItem.getImageRes());
            backgroundContainer.setBackgroundColor(color);
        }

        lastItemRendered = newItem;
    }

    @Override
    public void updateButtonState(boolean isFirstPage, boolean isLastPage) {
        btnPrev.setVisibility(isFirstPage ? View.INVISIBLE : View.VISIBLE);
        btnNext.setText(isLastPage ? getString(R.string.join_now) : getString(R.string.next));
    }

    @Override
    public void navigateToLogin() {
        FragmentManager parentManager = getParentFragmentManager();
        if (!parentManager.isDestroyed()) {
            parentManager.beginTransaction()
                    .replace(R.id.frag_container_auth, new LoginFragment())
                    .commitNow();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (presenter != null) {
            outState.putInt(POSITION_KEY, presenter.getCurrentPosition());
        }
    }
}
