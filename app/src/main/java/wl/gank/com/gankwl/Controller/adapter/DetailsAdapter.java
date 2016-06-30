package wl.gank.com.gankwl.Controller.adapter;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import wl.gank.com.gankwl.R;
import wl.gank.com.gankwl.cache.CacheConfig;
import wl.gank.com.gankwl.cache.ListCache;
import wl.gank.com.gankwl.cache.netcache.ListCacheFactory;
import wl.gank.com.gankwl.model.entity.Gank;
import wl.gank.com.gankwl.tools.MLog;

/**
 * Created by wanli on 2016/6/15.
 */
public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {



    private Context mContext;
    private List<Gank> gankList;

    boolean isCollectioned = false;

    private Vibrator vibrator;


    private static final int DELAY = 138;
    private int mLastPosition = -1;

    public DetailsAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<Gank> gankList) {
        this.gankList = gankList;
    }

    public int getViewId() {
        return R.layout.item_recy_details;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(getViewId(), parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Gank gank = gankList.get(position);
        if (position == 0) {
            showTitle(holder);
        } else {
            Boolean LastEqualsThis = gank.getType().equals(gankList.get(position - 1).getType());
            if (LastEqualsThis) {
                hideTitle(holder);
            } else {
                showTitle(holder);
            }
        }
        holder.txtTitle.setText(gank.getType());
        holder.txtDesc.setText(gank.getDesc());


        showItemAnim(holder.txtDesc, position);

        holder.cardviewItem.setOnClickListener((v) -> {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(gank.getUrl());
            intent.setData(content_url);
            mContext.startActivity(intent);
        });

        holder.cardviewItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                vibrator = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
                vibrator.vibrate(50);

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                builder.setItems(new String[]{"收藏"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /**
                         * 判断是否有收藏过的
                         *
                         * */
                        List<Gank> gankList = getCache();
                        if (gankList != null && gankList.size() > 0) {
                            for (Gank g : gankList) {
                                isCollectioned = g.getUrl().equals(gank.getUrl());
                                if (isCollectioned) {
                                    return;
                                }
                            }
                        }

                        if (!isCollectioned) {
                            ListCacheFactory listCacheFactory = new ListCacheFactory<Gank>(mContext, CacheConfig.MYCOLLECTION);
                            listCacheFactory.cacheObjectOfList(gank);
                            dialog.cancel();
                            Toast.makeText(mContext, "收藏成功", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (isCollectioned) {
                            Toast.makeText(mContext, "不要重复收藏哦", Toast.LENGTH_LONG).show();
                        }
                        vibrator.cancel();
                    }
                });
                builder.show();
                return false;
            }
        });

    }



    /**
     *
     * 此动画模仿 @drakeet
     *
     * */
    private void showItemAnim(View view, int position) {
        if (position > mLastPosition) {
            view.setAlpha(0);
            view.postDelayed(() -> {
                Animation animation = AnimationUtils.loadAnimation(mContext,
                        R.anim.slide);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {
                        view.setAlpha(1);
                    }
                    @Override public void onAnimationEnd(Animation animation) {}

                    @Override public void onAnimationRepeat(Animation animation) {}
                });
                view.startAnimation(animation);
            }, DELAY * position);
            mLastPosition = position;
        }
    }

    public List<Gank> getCache() {
        ListCacheFactory listCacheFactory = new ListCacheFactory(mContext, CacheConfig.MYCOLLECTION);
        List<Gank> ganks = listCacheFactory.getList();
        return ganks;
    }


    private void showTitle(ViewHolder holder) {
        holder.txtTitle.setVisibility(View.VISIBLE);
    }

    private void hideTitle(ViewHolder holder) {
        holder.txtTitle.setVisibility(View.GONE);
    }


    public boolean isViewble(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    @Override
    public int getItemCount() {
        return gankList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_title)
        TextView txtTitle;
        @Bind(R.id.txt_desc)
        TextView txtDesc;
        @Bind(R.id.cardview_item)
        CardView cardviewItem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
