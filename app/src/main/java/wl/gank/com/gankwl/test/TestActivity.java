package wl.gank.com.gankwl.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import wl.gank.com.gankwl.R;

public class TestActivity extends AppCompatActivity {

    @Bind(R.id.image_test)
    ImageView imageTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);


        Glide.with(this).load("http://ww3.sinaimg.cn/mw690/81309c56jw1f4sx4ybttdj20ku0vd0ym.jpg").into(imageTest);
    }
}
