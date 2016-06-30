package wl.gank.com.gankwl.view.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;
import wl.gank.com.gankwl.R;
import wl.gank.com.gankwl.view.ContentActivity;

public class PictureActivity extends ContentActivity {

    public static final String TRANSIT_NAME = "picture";

    public static final String EXTRA_IMAGE_URL = "image_url";

    @Bind(R.id.picture)
    ImageView picture;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    private PhotoViewAttacher mPhotoViewAttacher;
    private String mImageUrl;


    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent(context, PictureActivity.class);
        intent.putExtra(PictureActivity.EXTRA_IMAGE_URL, url);
        return intent;
    }

    private void parseIntent() {
        mImageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }


    @Override
    public void initView() {
        ViewCompat.setTransitionName(picture, TRANSIT_NAME);
        parseIntent();
        Glide.with(this).load(mImageUrl).into(picture);
        appBarLayout.setAlpha(0.2f);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void initEvent() {
        setupPhotoAttacher();
    }

    private void setupPhotoAttacher() {
        mPhotoViewAttacher = new PhotoViewAttacher(picture);
        mPhotoViewAttacher.setOnViewTapListener((view,x,y)->onBackPressed());
    }



    public boolean mIsHidden;
    public void hideOrShowToolbar() {
        appBarLayout.animate().translationY(mIsHidden ? 0 : -appBarLayout.getHeight()).setInterpolator(new DecelerateInterpolator()).start();
        mIsHidden = !mIsHidden;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


}
