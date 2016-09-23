package wl.gank.com.gankwl.view;

import android.support.v7.app.AppCompatActivity;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by wanli on 2016/6/14.
 */
public class BaseActivity extends AppCompatActivity {
    private CompositeSubscription mCompositeSubscription;
    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }

}
