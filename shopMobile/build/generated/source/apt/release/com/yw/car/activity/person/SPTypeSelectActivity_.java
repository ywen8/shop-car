//
// DO NOT EDIT THIS FILE.Generated using AndroidAnnotations 3.3.2.
//  You can create a larger work that contains this file and distribute that work under terms of your choice.
//


package com.yw.car.activity.person;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yw.car.R.id;
import com.yw.car.R.layout;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class SPTypeSelectActivity_
    extends SPTypeSelectActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.type_select_citiy);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static SPTypeSelectActivity_.IntentBuilder_ intent(Context context) {
        return new SPTypeSelectActivity_.IntentBuilder_(context);
    }

    public static SPTypeSelectActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new SPTypeSelectActivity_.IntentBuilder_(fragment);
    }

    public static SPTypeSelectActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new SPTypeSelectActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (((SdkVersionHelper.getSdkInt()< 5)&&(keyCode == KeyEvent.KEYCODE_BACK))&&(event.getRepeatCount() == 0)) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        lv_city = ((ListView) hasViews.findViewById(id.lv_city));
        btn_right = ((Button) hasViews.findViewById(id.btn_right));
        btn_back = ((RelativeLayout) hasViews.findViewById(id.btn_back));
        tvTittle = ((TextView) hasViews.findViewById(id.tv_tittle));
        {
            View view = hasViews.findViewById(id.rb_first);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        SPTypeSelectActivity_.this.onViewClick(view);
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.rb_second);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        SPTypeSelectActivity_.this.onViewClick(view);
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.rb_third);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        SPTypeSelectActivity_.this.onViewClick(view);
                    }

                }
                );
            }
        }
        if (btn_back!= null) {
            btn_back.setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    SPTypeSelectActivity_.this.onViewClick(view);
                }

            }
            );
        }
        if (btn_right!= null) {
            btn_right.setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    SPTypeSelectActivity_.this.onViewClick(view);
                }

            }
            );
        }
        init();
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<SPTypeSelectActivity_.IntentBuilder_>
    {

        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, SPTypeSelectActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), SPTypeSelectActivity_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), SPTypeSelectActivity_.class);
            fragmentSupport_ = fragment;
        }

        @Override
        public void startForResult(int requestCode) {
            if (fragmentSupport_!= null) {
                fragmentSupport_.startActivityForResult(intent, requestCode);
            } else {
                if (fragment_!= null) {
                    fragment_.startActivityForResult(intent, requestCode, lastOptions);
                } else {
                    if (context instanceof Activity) {
                        Activity activity = ((Activity) context);
                        ActivityCompat.startActivityForResult(activity, intent, requestCode, lastOptions);
                    } else {
                        context.startActivity(intent, lastOptions);
                    }
                }
            }
        }

    }

}
