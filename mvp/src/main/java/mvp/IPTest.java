package mvp;

import com.acmenxd.frame.basis.IBPresenter;
import com.acmenxd.frame.basis.IBView;
import com.acmenxd.mvp.model.response.TestEntity;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/8/3 11:39
 * @detail something
 */
public interface IPTest {
    interface IView extends IBView {
        void refreshView(TestEntity bean);
    }

    interface IPresenter extends IBPresenter {
        void requestData();
    }
}
