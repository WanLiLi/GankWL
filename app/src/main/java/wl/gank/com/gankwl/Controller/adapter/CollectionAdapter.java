package wl.gank.com.gankwl.Controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import wl.gank.com.gankwl.R;
import wl.gank.com.gankwl.model.entity.Gank;
import wl.gank.com.gankwl.tools.MLog;
import wl.gank.com.gankwl.view.activity.CollectionActivity;

/**
 * Created by wanli on 2016/6/17.
 */
public class CollectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Gank> gankList;
    private Context mContext;
    public Boolean isOpen = false;


    public CollectionAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public int getViewId() {
        return R.layout.item_collection;
    }

    public int getViewIdNoData() {
        return R.layout.nodata;
    }

    public void setData(List<Gank> gankList) {
        this.gankList = gankList;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Gank getItem(int arg0) {
        return gankList.get(arg0);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEMTYPE_NODATA) {
            View view = LayoutInflater.from(mContext).inflate(getViewIdNoData(), parent, false);
            return new ViewHolderNoData(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(getViewId(), parent, false);
            return new MyViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderNoData) {

        } else {
            Gank gank = gankList.get(position);

            ((MyViewHolder) holder).txtDesc.setText(gank.getDesc());
            ((MyViewHolder) holder).txtWho.setText(gank.getWho());


            ((MyViewHolder) holder).checkboxCollnection.setVisibility(isOpen ? View.VISIBLE : View.GONE);

            ((MyViewHolder) holder).checkboxCollnection.setChecked(gank.isCheck());  //防止选中状态错乱

            ((MyViewHolder) holder).checkboxCollnection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                    } else {
//                    Toast.makeText(mContext, "false", Toast.LENGTH_LONG).show();
                    }
                }
            });


            ((MyViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isOpen) {
                        boolean ischecked = ((MyViewHolder) holder).checkboxCollnection.isChecked();
                        //如果是选中的
                        if (ischecked) {
                            MLog.d("wanli", "gank: " + gank.getUrl());

                            gank.setCheck(false); //防止选中状态错乱
                            hideOrShow(((MyViewHolder) holder), false);
                        } else {
                            MLog.d("wanli", "gank: " + gank.getUrl());

                            gank.setCheck(true);
                            hideOrShow(((MyViewHolder) holder), true);
                        }
                        OnclickItem(position, !ischecked);
                    } else {
                        ((CollectionActivity) mContext).startWeb(gank.getUrl());
                    }
                }
            });
        }

    }

    public void OnclickItem(int position, boolean isChecked) {

    }

    private void hideOrShow(MyViewHolder holder, boolean setChecked) {
        holder.checkboxCollnection.setChecked(setChecked);
    }


    private int ITEMTYPE_NODATA = 0;
    private int ITEMTYPE_NORMAL = 1;

    @Override
    public int getItemCount() {
        //至少返回1是因为至少加载一个nodata布局
        return gankList == null || gankList.size() == 0 ? 1 : gankList.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (gankList == null || gankList.size() == 0) {
            return ITEMTYPE_NODATA;
        } else {
            return ITEMTYPE_NORMAL;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.checkbox_collnection)
        CheckBox checkboxCollnection;
        @Bind(R.id.txt_desc)
        TextView txtDesc;
        @Bind(R.id.txt_who)
        TextView txtWho;
        @Bind(R.id.cardview_item)
        RelativeLayout cardviewItem;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class ViewHolderNoData extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_nodata)
        TextView txtNodata;

        ViewHolderNoData(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
