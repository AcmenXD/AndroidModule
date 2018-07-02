package module;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.acmenxd.core.base.BaseActivity;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2018/6/20 17:43
 * @detail something
 */
public class FristActivity extends BaseActivity {
    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setTextColor(Color.RED);
        tv.setTextSize(35);
        tv.setText("demo模块启动页!");
        setContentView(tv);
    }
}
