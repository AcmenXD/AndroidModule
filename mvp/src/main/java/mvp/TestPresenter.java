package mvp;

import android.support.annotation.NonNull;

import com.acmenxd.frame.basis.RequestCallback;
import com.acmenxd.mvp.base.BasePresenter;
import com.acmenxd.mvp.model.response.TestEntity;
import com.acmenxd.retrofit.exception.HttpException;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/8/3 11:42
 * @detail something
 */
public class TestPresenter extends BasePresenter<IPTest.IView> implements IPTest.IPresenter {
    private TestModel mModel ;

    /**
     * 构造器,传入BaseView实例
     *
     * @param pView
     */
    public TestPresenter(IPTest.IView pView) {
        super(pView);
        mModel = new TestModel();
        addModels(mModel);
    }

    /**
     * 请求数据
     */
    public void requestData() {
        mModel.getData(new RequestCallback<TestEntity>(mView) {
            @Override
            public void succeed(@NonNull TestEntity pData) {
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
