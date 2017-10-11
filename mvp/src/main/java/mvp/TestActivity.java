package mvp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acmenxd.mvp.R;
import com.acmenxd.mvp.base.BaseActivity;
import com.acmenxd.mvp.model.response.TestEntity;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/8/3 11:37
 * @detail something
 */
public class TestActivity extends BaseActivity implements IPTest.IView {

    private TextView tv;
    private IPTest.IPresenter mPresenter = new TestPresenter(this);

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        tv = new TextView(this);
        tv.setTextSize(40);
        tv.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        pa.gravity = Gravity.CENTER;
        ((LinearLayout) getContentView()).addView(tv, pa);

        // 请求数据
        mPresenter.requestData();
    }

    @Override
    public void refreshView(TestEntity bean) {
        if (bean != null) {
            tv.setText(bean.data.url);
        } else {
            tv.setText("请求出错");
        }
    }
}
