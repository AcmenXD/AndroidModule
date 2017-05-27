package com.acmenxd.bourse.view.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acmenxd.bourse.R;
import com.acmenxd.bourse.base.BaseFragment;
import com.acmenxd.bourse.model.response.HomeEntity;
import com.acmenxd.bourse.presenter.home.HomePresenter;
import com.acmenxd.bourse.presenter.home.IHome;
import com.acmenxd.frame.utils.ViewUtils;
import com.acmenxd.frame.widget.RecyclerBannerView;
import com.acmenxd.recyclerview.adapter.AdapterUtils;
import com.acmenxd.recyclerview.adapter.MultiItemTypeAdapter;
import com.acmenxd.recyclerview.adapter.SimpleAdapter;
import com.acmenxd.recyclerview.delegate.ViewHolder;
import com.acmenxd.recyclerview.group.GroupDecoration;
import com.acmenxd.recyclerview.group.GroupHeadLayout;
import com.acmenxd.recyclerview.group.GroupListener;
import com.acmenxd.recyclerview.listener.AddItemListener;
import com.acmenxd.recyclerview.listener.ItemCallback;
import com.acmenxd.recyclerview.listener.ItemSwipeCallback;
import com.acmenxd.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.acmenxd.toaster.Toaster;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/5 17:20
 * @detail something
 */
public class HomeFragment extends BaseFragment implements IHome.IView {
    private RecyclerView rv;
    private SwipeRefreshLayout srl;
    private GroupHeadLayout mGroupHead;
    private MultiItemTypeAdapter mAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    private RecyclerBannerView mBannerView;
    private LinearLayout mHeader;
    private View mFooter;

    private HomePresenter mHomePresenter;
    private List<HomeEntity.Product> mProducts = new ArrayList<>();
    private List<HomeEntity.Banner> mBanners = new ArrayList<>();
    private List<HomeEntity.Introduce> mIntroduces = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showLoadingView(); // 显示加载视图

        init();
    }

    private void init() {
        initData(); // 初始化数据

        initView(); // 初始化View

        loadData(); // 加载数据
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mHomePresenter = new HomePresenter(this); // 创建HomePresenter
        addPresenters(mHomePresenter); // 添加到BaseFragment管理
    }

    /**
     * 初始化View
     */
    private void initView() {
        rv = getView(R.id.fragment_home_rv);
        srl = getView(R.id.fragment_home_srl);
        mGroupHead = getView(R.id.fragment_home_group);

        // 设置SwipeRefreshLayout样式
        ViewUtils.setSwipeRefreshLayoutStyle(srl, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        //设置布局管理器
        LinearLayoutManager linearManager = new LinearLayoutManager(mActivity);
        linearManager.setOrientation(OrientationHelper.VERTICAL);
        rv.setLayoutManager(linearManager);

        //设置增加或删除条目的动画
        rv.setItemAnimator(new DefaultItemAnimator());

        // 添加悬浮菜单
        GroupListener mGroupListener = new GroupListener() {
            @Override
            public int getGroupItemLevelNum() {
                return 1;
            }

            @Override
            public boolean isGroupItemTypeMoreOne() {
                return false;
            }

            @Override
            public boolean isAutoSetGroupHeadViewWidthHeightByGroupItemView() {
                return false;
            }

            @Override
            public boolean isCreateGroupItemView(int dataPosition) {
                if (mProducts.size() > dataPosition && !TextUtils.isEmpty(mProducts.get(dataPosition).getGroupTitle())) {
                    return true;
                }
                return false;
            }

            @Override
            public View getGroupItemView(ViewGroup root, int groupLevel, int dataPosition) {
                View view = null;
                if (mProducts.size() > dataPosition && !TextUtils.isEmpty(mProducts.get(dataPosition).getGroupTitle())) {
                    return LayoutInflater.from(mActivity).inflate(R.layout.fragment_home_rv_groupitem, root, false);
                }
                return view;
            }

            @Override
            public void changeGroupItemView(View groupItemView, int groupLevel, int dataPosition) {
                TextView tv = (TextView) groupItemView.findViewById(R.id.fragment_home_rv_groupitem_tvTitle);
                tv.setText(mProducts.get(dataPosition).getGroupTitle());
            }

            @Override
            public View getGroupHeadView(ViewGroup root, int groupLevel, int dataPosition) {
                return getGroupItemView(root, groupLevel, dataPosition);
            }

            @Override
            public void changeGroupHeadView(View groupHeadView, int groupLevel, int dataPosition) {
                changeGroupItemView(groupHeadView, groupLevel, dataPosition);
            }
        };
        rv.addItemDecoration(new GroupDecoration(mGroupHead, mGroupListener));

        // 设置监听
        new AddItemListener(rv, new ItemCallback() {
            @Override
            public void onClick(RecyclerView.ViewHolder pViewHolder, int pI) {
                Toaster.show("产品详情");
            }
        }, new ItemSwipeCallback() {
            @Override
            public boolean onDeleteData(RecyclerView.ViewHolder pViewHolder, int dataPosition, int pI1) {
                if (mProducts.size() > dataPosition) {
                    mProducts.remove(dataPosition);
                }
                return true;
            }
        }, null);

        // 设置数据Adapter
        mAdapter = new SimpleAdapter<HomeEntity.Product>(mActivity, rv, R.layout.fragment_home_rv_item, mProducts) {
            @Override
            public void convert(ViewHolder viewHolder, HomeEntity.Product item, int dataPosition) {
                TextView tvTitile = viewHolder.getView(R.id.fragment_home_rv_item_tvTitle);
                TextView tvEarnings = viewHolder.getView(R.id.fragment_home_rv_item_tvEarnings);
                TextView tvDate = viewHolder.getView(R.id.fragment_home_rv_item_tvDate);
                TextView tvPercentage = viewHolder.getView(R.id.fragment_home_rv_item_tvPercentage);
                tvTitile.setText(item.getTitile());
                tvEarnings.setText(item.getEarnings());
                tvDate.setText(item.getDate());
                tvPercentage.setText(item.getPercentage());
            }
        };

        // Header&Footer Adapter
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(rv, mAdapter);
        mBannerView = (RecyclerBannerView) LayoutInflater.from(mActivity).inflate(R.layout.fragment_home_rv_header_banner, rv, false);
        mHeader = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.fragment_home_rv_header, rv, false);
        mFooter = LayoutInflater.from(mActivity).inflate(R.layout.fragment_home_rv_footer, rv, false);
        mHeaderAndFooterWrapper.addHeaderView(mBannerView);
        mHeaderAndFooterWrapper.addHeaderView(mHeader);
        mHeaderAndFooterWrapper.addFootView(mFooter);
        rv.setAdapter(mHeaderAndFooterWrapper);
    }

    /**
     * 刷新Banner
     */
    private void refreshBannerView() {
        if (mBannerView.getOnListener() == null) {
            mBannerView.setOnListener(new RecyclerBannerView.OnListener<HomeEntity.Banner>() {
                @Override
                public void onClick(int position, HomeEntity.Banner pData) {
                    Toaster.show(pData.getHtmlUrl());
                }

                @Override
                public ViewGroup getItemView(int position, HomeEntity.Banner pData) {
                    FrameLayout layout = new FrameLayout(mActivity);
                    layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    ImageView iv = new ImageView(mActivity);
                    iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    layout.addView(iv);
                    Glide.with(mActivity).load(pData.getUri()).into(iv);
                    return layout;
                }

                @Override
                public int getIndicatorResource() {
                    return R.drawable.widget_banner_indicator_selected;
                }
            });
        }
        mBannerView.setDataCommit(mBanners);
    }

    /**
     * 刷新Header
     */
    private void refreshHeader() {
        for (int i = 0; i < mIntroduces.size(); i++) {
            final HomeEntity.Introduce introduce = mIntroduces.get(i);
            Button btn = null;
            if (i == 0) {
                btn = (Button) mHeader.findViewById(R.id.fragment_home_rv_header_btnBourseDetail);
            } else if (i == 1) {
                btn = (Button) mHeader.findViewById(R.id.fragment_home_rv_header_btnAppDetail);
            }
            btn.setText(introduce.getTitle());
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toaster.show(introduce.getUri());
                }
            });
        }
        TextView tvFinishRegister = (TextView) mHeader.findViewById(R.id.fragment_home_rv_header_tvFinishRegister);
        String text = "注册并完成风险评测,寻找最适合的理财产品>";
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toaster.show("完成评测注册!");
            }
        }, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvFinishRegister.setMovementMethod(LinkMovementMethod.getInstance());
        tvFinishRegister.setText(ss);
    }

    /**
     * 刷新Footer
     */
    private void refreshFooter() {

    }

    /**
     * 刷新Adapter
     */
    private void refreshAdapter() {
        /**
         * 在Adapter.onBindViewHolder()中调用notifyDataSetChanged()会使程序崩溃
         * mEmptyWarpper.notifyDataSetChanged();
         */
        mAdapter.setDatas(mProducts);
        AdapterUtils.notifyDataSetChanged(rv, mHeaderAndFooterWrapper);
    }

    /**
     * 模拟数据加载
     */
    private void loadData() {
        mHomePresenter.loadData();
    }

    @Override
    public void refreshRootView(HomeEntity data) {
        mProducts = data.getProducts();
        mBanners = data.getBanners();
        mIntroduces = data.getIntroduces();
        // 更新Banner
        refreshBannerView();
        // 更新Header
        refreshHeader();
        // 更新Footer
        refreshFooter();
        // 更新列表
        refreshAdapter();
        // 视图状态恢复
        srl.setRefreshing(false);
        showContentView(true);
    }

}
