package com.acmenxd.bourse.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.acmenxd.bourse.R;
import com.acmenxd.bourse.base.AppConfig;
import com.acmenxd.bourse.base.BaseActivity;
import com.acmenxd.bourse.base.BaseFragment;
import com.acmenxd.bourse.base.EventBusHelper;
import com.acmenxd.bourse.model.response.TestEntity;
import com.acmenxd.bourse.view.home.HomeFragment;
import com.acmenxd.bourse.view.mine.MineFragment;
import com.acmenxd.frame.widget.NavigationBar;
import com.acmenxd.frame.widget.TitleView;
import com.acmenxd.logger.Logger;
import com.acmenxd.toaster.Toaster;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/12/16 15:34
 * @detail 主Activity
 */
public class MainActivity extends BaseActivity {
    private TitleView mTitleView;
    private NavigationBar mNavigationBar;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;

    private List<BaseFragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.w("App进入MainActivity!");
        setContentView(R.layout.activity_main);

        // 设置标题
        mTitleView = getView(R.id.title);

        // 设置导航栏
        mNavigationBar = getView(R.id.navigation_bar);
        mNavigationBar.addItem("首页", R.drawable.widget_navigation_bar_home);
        mNavigationBar.addItem("我的", R.drawable.widget_navigation_bar_mine);
        mNavigationBar.setListener(new NavigationBar.OnNavigationListener() {
            @Override
            public void onTabChange(int position) {
                switch (position) {
                    case 0:
                        mNavigationBar.setRedPoint(0, -1);
                        mNavigationBar.setRedPoint(1, 0);
                        mTitleView.setTitle("首页");
                        break;
                    case 1:
                        mNavigationBar.setRedPoint(0, 0);
                        mNavigationBar.setRedPoint(1, -1);
                        mTitleView.setTitle("我的");
                        break;
                }
            }

            @Override
            public void onClick(int position) {
                mViewPager.setCurrentItem(position);
            }
        });
        mNavigationBar.setSelectTab(0);

        // 设置ViewPager
        mViewPager = getView(R.id.activity_main_viewpager);
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new MineFragment());
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mNavigationBar.setSelectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(0);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<BaseFragment> mFragments;

        public ViewPagerAdapter(FragmentManager pFragmentManager, List<BaseFragment> pFragments) {
            super(pFragmentManager);
            this.mFragments = pFragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBusHelper.post(new TestEntity());
    }

    @Subscribe
    public void showTestEntity(TestEntity pTestEntity) {
        Toaster.show("Debug:" + AppConfig.DEBUG);
    }

}
