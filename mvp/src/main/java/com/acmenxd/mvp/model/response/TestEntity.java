package com.acmenxd.mvp.model.response;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/12/13 14:05
 * @detail 测试用实体类
 */
public class TestEntity<T> {

    /**
     * author : {"des":"欢迎下载使用OkHttpUtils网络框架，如果遇到问题，欢迎反馈！如果觉得好用，不妨star一下，以便让更多的人了解使用！","email":"liaojeason@126.com","address":"北京","name":"jeasonlzy","github":"https://github.com/jeasonlzy0216","qq":"QQ交流群：489873144","fullname":"廖子尧"}
     * des : 请求服务器返回Json对象
     * method : POST
     * url : http://server.jeasonlzy.com/OkHttpUtils/jsonObject
     * ip : 2606:df00:3:0:0:0:b25d:6e94:35354
     */

    private AuthorBean author;
    private String des;
    private String method;
    private String url;
    private String ip;

    public AuthorBean getAuthor() {
        return author;
    }

    public void setAuthor(AuthorBean author) {
        this.author = author;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public static class AuthorBean {
        /**
         * des : 欢迎下载使用OkHttpUtils网络框架，如果遇到问题，欢迎反馈！如果觉得好用，不妨star一下，以便让更多的人了解使用！
         * email : liaojeason@126.com
         * address : 北京
         * name : jeasonlzy
         * github : https://github.com/jeasonlzy0216
         * qq : QQ交流群：489873144
         * fullname : 廖子尧
         */

        private String des;
        private String email;
        private String address;
        private String name;
        private String github;
        private String qq;
        private String fullname;

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGithub() {
            return github;
        }

        public void setGithub(String github) {
            this.github = github;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }
    }
}
