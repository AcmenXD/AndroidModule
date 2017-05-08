package com.acmenxd.mvp.view;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.acmenxd.logger.Logger;
import com.acmenxd.mvp.R;
import com.acmenxd.mvp.base.BaseActivity;
import com.acmenxd.mvp.model.response.TestEntity;
import com.acmenxd.mvp.utils.EventBusUtils;
import com.acmenxd.mvp.view.test.DBActivity;
import com.acmenxd.mvp.view.test.FileActivity;
import com.acmenxd.mvp.view.test.GlideActivity;
import com.acmenxd.mvp.view.test.LogActivity;
import com.acmenxd.mvp.view.test.RecyclerActivity;
import com.acmenxd.mvp.view.test.RetrofitActivity;
import com.acmenxd.mvp.view.test.SPActivity;
import com.acmenxd.mvp.view.test.ToastActivity;
import com.acmenxd.mvp.view.test.frescoActivity;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/12/16 15:34
 * @detail 主Activity
 */
public class MainActivity extends BaseActivity {
    private ArrayList<DataInfo> datas;
    private ListView lv_main;
    private MyMainAdapter mMyMainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.w("App进入MainActivity!");
        setTitle(getBundle().getString("title", "Android框架设计"));
        setContentView(R.layout.activity_main);
        initData();

        lv_main = (ListView) findViewById(R.id.lv_main);
        mMyMainAdapter = new MyMainAdapter();
        lv_main.setAdapter(mMyMainAdapter);
        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pAdapterView, View pView, int pI, long pL) {
                Bundle bundle = new Bundle();
                bundle.putString("title", datas.get(pI).name);
                startActivity(datas.get(pI).path, bundle);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBusUtils.post(new TestEntity());
    }

    @Subscribe
    public void showTestEntity(TestEntity pTestEntity) {
        Logger.i("EventBus测试!");
    }

    private void initData() {
        datas = new ArrayList<>();
        datas.add(new DataInfo("FileUtils 相关操作", "1.统一管理程序所需目录!", FileActivity.class));
        datas.add(new DataInfo("Logger日志",
                "1.完美兼容Android系统Log日志 \n" +
                        "2.拓展Log日志,参数支持任意类型&Object \n" +
                        "3.支持 开关&等级 控制 \n" +
                        "4.支持 json&xml 自动格式化日志输出 \n" +
                        "5.支持输出日志到指定文件 \n" +
                        "6.支持所有Throwable错误格式化输出 \n" +
                        "7.支持日志追溯功能,一键到日志输出的代码行!"
                , LogActivity.class));
        datas.add(new DataInfo("Toast 相关操作",
                "1.由系统Toast拓展而来 \n" +
                        "2.支持 debug模式Toast 开关 \n" +
                        "3.支持 '无等待' 模式,随用随弹 \n" +
                        "4.支持自定义时长 \n" +
                        "5.支持Object可变参数,无需在转换Strng的繁琐 \n" +
                        "6.支持R.String的id类型,也是可变参,超方便 \n" +
                        "7.支持View类型~自定义就是这么简单 \n" +
                        "8.支持位置可变,上中下左右~支持偏移量,想放哪里放哪里!", ToastActivity.class));
        datas.add(new DataInfo("SharedPreferences 相关操作",
                "1.由系统SharedPreferences扩展 \n" +
                        "2.所有Key-Value默认采用Base64+MD5加解密 \n" +
                        "3.加密方式一行代码替换 \n" +
                        "4.支持数据变更的事件监听!", SPActivity.class));
        datas.add(new DataInfo("Retrofit 相关操作",
                "1.支持基本Get,Post,Put,Options以及Bitmap上传下载请求 \n" +
                        "2.支持网络缓存及Cookie自动管理 \n" +
                        "3.支持格式化Log输出(很详细的日志哦~) \n" +
                        "4.支持公共Header,param,body的设置 \n" +
                        "5.支持RxJava \n" +
                        "6.支持统一错误处理!", RetrofitActivity.class));
        datas.add(new DataInfo("Image -> Glide 相关操作",
                "1.使用方式同glide一样 \n" +
                        "2.提供glide初始的一些函数调用 \n" +
                        "3.提供圆角|圆形支持!", GlideActivity.class));
        datas.add(new DataInfo("Image -> fresco 相关操作",
                "1.支持PNG / GIF / WebP / JPEG格式,支持加载网络/res资源/本地资源 \n" +
                        "2.支持 低分辨率&高分辨率加载 > 缩略图式加载 > 渐进式加载 \n" +
                        "3.支持监听加载进度,加载成功和失败回调 \n" +
                        "4.支持多层构图,占位图-背景图-覆盖图-加载失败图-失败重试图-进度条图 \n" +
                        "5.支持加载失败,点击再次加载 \n" +
                        "6.支持加载过程中显示加载动画 \n" +
                        "7.支持圆角|原型|边框,并设置颜色 \n" +
                        "8.支持图片渐显动画 \n" +
                        "9.支持gif动画的随时播放暂停!", frescoActivity.class));
        datas.add(new DataInfo("RecyclerView相关操作",
                "1.支持下拉上拉刷新加载\n" +
                        "2.支持Header/Footer/Empty布局的添加\n" +
                        "3.支持N种item类型\n" +
                        "4.无需再实现Adapter/ViewHolder类,直接new CommonAdapter或MultiItemTypeAdapter重写回调方法即可\n" +
                        "5.LoadMore & Empty 支持点击回调\n" +
                        "6.支持自定义分隔线 & item动画\n" +
                        "7.未更改任何RecyclerView实现,无缝支持RecyclerView所有功能\n" +
                        "8.链式Adapter调用~ 易读,易懂!\n" +
                        "9.支持item单击&长按&滑动删除&拖拽换位&侧滑菜单功能!", RecyclerActivity.class));
        datas.add(new DataInfo("GreenDao3.x相关操作",
                "1.实体类一键创建数据库表\n" +
                        "2.实体Dao自动生成管理,提供诸多操作数据库函数\n" +
                        "3.数据库版本号一键修改,升级支持增减表字段!", DBActivity.class));
    }

    private class DataInfo {
        public DataInfo(String pName, String pDetail, Class pPath) {
            this.name = pName;
            this.detail = pDetail;
            this.path = pPath;
        }

        String name;
        String detail;
        Class path;
    }

    private class MyMainAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int pI) {
            return datas.get(pI);
        }

        @Override
        public long getItemId(int pI) {
            return pI;
        }

        @Override
        public View getView(int pI, View pView, ViewGroup pViewGroup) {
            ViewHolder vh;
            if (pView == null) {
                pView = View.inflate(MainActivity.this, R.layout.activity_main_item, null);
                vh = new ViewHolder();
                vh.tv_name = (TextView) pView.findViewById(R.id.tv_name);
                vh.tv_detail = (TextView) pView.findViewById(R.id.tv_detail);
                pView.setTag(vh);
            } else {
                vh = (ViewHolder) pView.getTag();
            }
            DataInfo bean = datas.get(pI);
            vh.tv_name.setText(bean.name);
            vh.tv_detail.setText(bean.detail);
            return pView;
        }

        class ViewHolder {
            TextView tv_name;
            TextView tv_detail;
        }
    }
}
