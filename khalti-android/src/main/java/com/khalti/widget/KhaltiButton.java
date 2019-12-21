package com.khalti.widget;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.button.MaterialButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.khalti.R;
import com.khalti.checkOut.api.Config;
import com.khalti.checkOut.helper.KhaltiCheckOut;
import com.khalti.utils.EmptyUtil;

@Keep
public class KhaltiButton extends FrameLayout implements KhaltiButtonInterface {
    private Context context;
    private AttributeSet attrs;

    private Config config;

    private PayContract.Presenter presenter;

    private FrameLayout flCustomView;
    private FrameLayout flStyle;
    private MaterialButton btnPay;
    private View customView;
    private int buttonStyle;
    private OnClickListener onClickListener;

    public KhaltiButton(@NonNull Context context) {
        super(context);
        this.context = context;

        Pay pay = new Pay();
        presenter = pay.getPresenter();
        init();
    }

    public KhaltiButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;

        Pay pay = new Pay();
        presenter = pay.getPresenter();
        init();
    }

    @Override
    public void setText(String text) {
        presenter.setButtonText(text);
    }

    @Override
    public void setCheckOutConfig(Config config) {
        this.config = config;
        String message = presenter.checkConfig(config);
        if (EmptyUtil.isNotNull(message)) {
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public void setCustomView(View view) {
        this.customView = view;
        presenter.setCustomButtonView();
        presenter.setButtonClick();
    }

    @Override
    public void setButtonStyle(ButtonStyle buttonStyle) {
        this.buttonStyle = buttonStyle.getId();
        presenter.setButtonStyle(this.buttonStyle);
        presenter.setButtonClick();
    }

    @Override
    public void showCheckOut() {
        presenter.openForm();
    }

    @Override
    public void showCheckOut(Config config) {
        this.config = config;
        String message = presenter.checkConfig(config);
        if (EmptyUtil.isNotNull(message)) {
            throw new IllegalArgumentException(message);
        }
        presenter.openForm();
    }

    @Override
    public void destroyCheckOut() {
        presenter.destroyCheckOut();
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
        this.onClickListener = l;
        presenter.setButtonClick();
    }

    private void init() {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.khalti, 0, 0);
        String buttonText = a.getString(R.styleable.khalti_text);
        buttonStyle = a.getInt(R.styleable.khalti_button_style, -1);
        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (EmptyUtil.isNotNull(inflater)) {
            View mainView = inflater.inflate(R.layout.component_button, this, true);

            btnPay = mainView.findViewById(R.id.btnPay);
            flCustomView = mainView.findViewById(R.id.flCustomView);
            flStyle = mainView.findViewById(R.id.flStyle);

            presenter.setButtonText(buttonText);
            presenter.setButtonStyle(buttonStyle);
            presenter.setButtonClick();
        }
    }

    private class Pay implements PayContract.View {
        private PayContract.Presenter presenter;
        private KhaltiCheckOut khaltiCheckOut;

        Pay() {
            presenter = new PayPresenter(this);
        }

        @Override
        public void setCustomButtonView() {
            btnPay.setVisibility(View.GONE);
            flCustomView.addView(customView);
        }

        @Override
        public void setButtonStyle(int id) {
            int imageId = -1;
            switch (id) {
                case 0:
                    imageId = R.mipmap.ebanking_dark;
                    break;
                case 1:
                    imageId = R.mipmap.ebanking_light;
                    break;
            }
            if (imageId != -1) {
                btnPay.setVisibility(GONE);
                flCustomView.setVisibility(View.GONE);
                flStyle.setBackgroundResource(imageId);
            }
        }

        @Override
        public void setButtonText(String text) {
            btnPay.setText(text);
        }

        @Override
        public void setButtonClick() {
            onClickListener = EmptyUtil.isNull(onClickListener) ? view -> presenter.openForm() : onClickListener;

            if (EmptyUtil.isNotNull(customView)) {
                flCustomView.getChildAt(0).setOnClickListener(onClickListener);
            } else if (buttonStyle != -1) {
                flStyle.setOnClickListener(onClickListener);
            } else {
                btnPay.setOnClickListener(onClickListener);
            }
        }

        @Override
        public void openForm() {
            khaltiCheckOut = new KhaltiCheckOut(context, config);
            khaltiCheckOut.show();
        }

        @Override
        public void destroyCheckOut() {
            if (EmptyUtil.isNull(khaltiCheckOut)) {
                throw new IllegalArgumentException("CheckOut cannot be destroyed before it is shown.");
            }
            khaltiCheckOut.destroy();
        }

        PayContract.Presenter getPresenter() {
            return presenter;
        }

        @Override
        public void setPresenter(PayContract.Presenter presenter) {
            this.presenter = presenter;
        }
    }
}