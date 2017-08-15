package com.acmenxd.mvp.model.response;

import com.acmenxd.retrofit.HttpEntity;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/8/14 18:07
 * @detail something
 */
public class TestHttpEntity extends HttpEntity {

    /**
     * data : {"author":{"des":"欢迎下载使用OkHttpUtils网络框架，如果遇到问题，欢迎反馈！如果觉得好用，不妨star一下，以便让更多的人了解使用！","email":"liaojeason@126.com","address":"北京","name":"jeasonlzy","github":"https://github.com/jeasonlzy0216","qq":"QQ交流群：489873144","fullname":"廖子尧"},"des":"请求服务器返回Json对象","method":"PUT","url":"http://server.jeasonlzy.com/OkHttpUtils/jsonObject","ip":"2607:8700:101:5f42:0:0:0:8:60950"}
     */

    public Data data;

    public static class Data {
        /**
         * author : {"des":"欢迎下载使用OkHttpUtils网络框架，如果遇到问题，欢迎反馈！如果觉得好用，不妨star一下，以便让更多的人了解使用！","email":"liaojeason@126.com","address":"北京","name":"jeasonlzy","github":"https://github.com/jeasonlzy0216","qq":"QQ交流群：489873144","fullname":"廖子尧"}
         * des : 请求服务器返回Json对象
         * method : PUT
         * url : http://server.jeasonlzy.com/OkHttpUtils/jsonObject
         * ip : 2607:8700:101:5f42:0:0:0:8:60950
         */

        public Author author;
        public String des;
        public String method;
        public String url;
        public String ip;

        public static class Author {
            /**
             * des : 欢迎下载使用OkHttpUtils网络框架，如果遇到问题，欢迎反馈！如果觉得好用，不妨star一下，以便让更多的人了解使用！
             * email : liaojeason@126.com
             * address : 北京
             * name : jeasonlzy
             * github : https://github.com/jeasonlzy0216
             * qq : QQ交流群：489873144
             * fullname : 廖子尧
             */

            public String des;
            public String email;
            public String address;
            public String name;
            public String github;
            public String qq;
            public String fullname;
        }
    }
}
