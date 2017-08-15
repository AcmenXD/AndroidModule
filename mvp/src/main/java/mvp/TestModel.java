package mvp;

import com.acmenxd.frame.basis.RequestCallback;
import com.acmenxd.mvp.base.BaseModel;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/8/3 11:45
 * @detail something
 */
public class TestModel extends BaseModel implements ITestM {

    public void getData(final RequestCallback pCallback) {
        request().get("token").enqueue(pCallback);
    }
}
