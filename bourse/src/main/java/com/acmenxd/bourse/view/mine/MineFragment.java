package com.acmenxd.bourse.view.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acmenxd.bourse.R;
import com.acmenxd.bourse.base.BaseFragment;
import com.acmenxd.bourse.model.response.MineEntity;
import com.acmenxd.bourse.presenter.mine.IMine;
import com.acmenxd.bourse.presenter.mine.MinePresenter;
import com.acmenxd.frame.utils.ViewUtils;
import com.acmenxd.recyclerview.adapter.AdapterUtils;
import com.acmenxd.recyclerview.adapter.MultiItemTypeAdapter;
import com.acmenxd.recyclerview.adapter.SimpleAdapter;
import com.acmenxd.recyclerview.delegate.ViewHolder;
import com.acmenxd.recyclerview.listener.AddItemListener;
import com.acmenxd.recyclerview.listener.ItemCallback;
import com.acmenxd.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.acmenxd.toaster.Toaster;
import java.util.ArrayList;
import java.util.List;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/5 17:20
 * @detail something
 */
public class MineFragment extends BaseFragment implements IMine.IView {
    private RecyclerView rv;
    private SwipeRefreshLayout srl;
    private MultiItemTypeAdapter mAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    private LinearLayout mHeader;
    private LinearLayout mFooter;

    private MinePresenter mMinePresenter;
    private List<MineEntity.Item> mItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showLoadingView(); // 显示加载视图
    }

    @Override
    protected void onViewPagerFragmentFirstVisible() {
        super.onViewPagerFragmentFirstVisible();
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
        mMinePresenter = new MinePresenter(this); // 创建HomePresenter
        addPresenters(mMinePresenter); // 添加到BaseFragment管理
    }

    /**
     * 初始化View
     */
    private void initView() {
        rv = getView(R.id.fragment_mine_rv);
        srl = getView(R.id.fragment_mine_srl);

        // 设置刷新时动画的颜色
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

        // 设置监听
        new AddItemListener(rv, new ItemCallback() {
            @Override
            public void onClick(RecyclerView.ViewHolder pViewHolder, int pI) {
                Toaster.show(mItems.get(pI).getTitle());
            }
        });

        // 设置数据Adapter
        mAdapter = new SimpleAdapter<MineEntity.Item>(mActivity, rv, R.layout.fragment_mine_rv_item, mItems) {
            @Override
            public void convert(ViewHolder viewHolder, MineEntity.Item item, int dataPosition) {
                TextView tvTitile = viewHolder.getView(R.id.fragment_mine_rv_item_tvTitle);
                TextView tvDetail = viewHolder.getView(R.id.fragment_mine_rv_item_tvDetail);
                TextView tvGo = viewHolder.getView(R.id.fragment_mine_rv_item_tvGo);
                tvTitile.setText(item.getTitle());
                tvDetail.setText(item.getDetail());
                tvGo.setText(item.getGo());
            }
        };

        // Header&Footer Adapter
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(rv, mAdapter);
        mHeader = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.fragment_mine_rv_header, rv, false);
        mFooter = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.fragment_mine_rv_footer, rv, false);
        mHeaderAndFooterWrapper.addHeaderView(mHeader);
        mHeaderAndFooterWrapper.addFootView(mFooter);
        rv.setAdapter(mHeaderAndFooterWrapper);
    }

    /**
     * 刷新Header
     */
    private void refreshHeader() {

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
        mAdapter.setDatas(mItems);
        AdapterUtils.notifyDataSetChanged(rv, mHeaderAndFooterWrapper);
    }

    /**
     * 模拟数据加载
     */
    private void loadData() {
        mMinePresenter.loadData();
    }

    @Override
    public void refreshRootView(MineEntity data) {
        mItems = data.getItems();
        // 更新Header
        refreshHeader();
        // 更新Footer
        refreshFooter();
        // 更新列表
        refreshAdapter();
        // 视图状态恢复
        srl.setRefreshing(false);
        showContentView();
    }
}
