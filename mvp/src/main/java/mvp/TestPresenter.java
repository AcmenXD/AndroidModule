package mvp;

import android.support.annotation.NonNull;

import com.acmenxd.frame.basis.RequestCallback;
import com.acmenxd.mvp.base.BasePresenter;
import com.acmenxd.mvp.model.response.TestHttpEntity;
import com.acmenxd.retrofit.exception.HttpException;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/8/3 11:42
 * @detail something
 */
public class TestPresenter extends BasePresenter<ITest.IView> implements ITest.IPresenter {
    private ITestM mModel = new TestModel();

    /**
     * 构造器,传入BaseView实例
     *
     * @param pView
     */
    public TestPresenter(ITest.IView pView) {
        super(pView);
    }

    /**
     * 请求数据
     */
    public void requestData() {
        mModel.getData(new RequestCallback<TestHttpEntity>(mView) {
            @Override
            public void succeed(@NonNull TestHttpEntity pData) {
                super.succeed(pData);
                mView.refreshView(pData);
            }

            @Override
            public void failed(@NonNull HttpException pE) {
                super.failed(pE);
                mView.refreshView(null);
            }
        });
    }

}
