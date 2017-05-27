package com.acmenxd.frame.configs;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/24 18:39
 * @detail Bourse项目配置文件
 */
public class BourseConfig extends ConfigInfo {

    public String SP_User = "spUser";
    public String SP_Config = "spConfig";

    @Override
    public void init(boolean isDebug) {
        super.init(isDebug);

        DB_NAME = "bourse_db";
        BASE_DIR = SDCARD_DIR + "/Bourse/";
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

    @Override
    protected void initNetParams() {
        ParameterMaps.clear();
        HeaderMaps.clear();
        HeaderMaps2.clear();
        BodyMaps.clear();
        ParameterMaps.put("parameter_key_1", "parameter_value_1");
        HeaderMaps.put("header_key_1", "header_value_1");
        HeaderMaps2.put("header_key_1", "header_value_1");
        HeaderMaps2.put("header_key_2", "header_value_2");
        BodyMaps.put("body_key_1", "body_value_1");
    }
}
