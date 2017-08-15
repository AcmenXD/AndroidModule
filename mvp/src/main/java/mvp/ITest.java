package mvp;

import com.acmenxd.frame.basis.IBPresenter;
import com.acmenxd.frame.basis.IBView;
import com.acmenxd.mvp.model.response.TestHttpEntity;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/8/3 11:39
 * @detail something
 */
public interface ITest {
    interface IView extends IBView {
        void refreshView(TestHttpEntity bean);
    }

    interface IPresenter extends IBPresenter {
        void requestData();
    }
}
