package com.acmenxd.frame.configs;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/24 18:39
 * @detail Mvp项目配置文件
 */
public final class MvpConfig extends BaseConfig {

    public String SP_User = "spUser";
    public String SP_Config = "spConfig";

    @Override
    protected void init(boolean isDebug) {
        super.init(isDebug);

        DB_NAME = "mvp_db";
        BASE_DIR = SDCARD_DIR + "/Mvp/";
        LOG_DIR = BASE_DIR + "Logger/";
        // 请求地址配置 -1:正式版  0->预发布  1->测试1
        URL_Type = 1;
    }

    @Override
    protected void initSpData() {
        spAll = new String[]{SP_Device, SP_User, SP_Config};
    }

    @Override
    protected void initNetURL() {
        switch (URL_Type) {
            case -1:
                //正式版
                BASE_URL = "http://server.jeasonlzy.com/OkHttpUtils/";
                break;
            case 0:
                //预发布
                BASE_URL = "http://server.jeasonlzy.com/OkHttpUtils/";
                break;
            case 1:
                //测试1
                BASE_URL = "http://server.jeasonlzy.com/OkHttpUtils/";
                break;
            default:
                BASE_URL = "http://server.jeasonlzy.com/OkHttpUtils/";
                break;
        }
    }
}
