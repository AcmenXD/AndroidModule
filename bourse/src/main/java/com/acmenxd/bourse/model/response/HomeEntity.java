package com.acmenxd.bourse.model.response;

import com.acmenxd.frame.utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/8 11:35
 * @detail something
 */
public class HomeEntity {
    private String[] bannerImages = new String[]{
            "http://img2.imgtn.bdimg.com/it/u=117876771,2856802319&fm=23&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=346026830,1701463557&fm=23&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=2703128497,2952804525&fm=23&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=1981984850,3250591066&fm=23&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=4007582788,1260918218&fm=23&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=757062034,39150752&fm=23&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=1406866991,3993243227&fm=23&gp=0.jpg",};
    private List<Banner> banners;
    private List<Introduce> introduces;
    private List<Product> products;

    public HomeEntity() {
        banners = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();
        for (int i = 0, len = RandomUtils.randomByMinMax(2, 2); i < len; i++) {
            Banner banner = new Banner();
            int index;
            while (true) {
                index = RandomUtils.randomByMinMax(0, bannerImages.length - 1);
                if (!temp.contains(index)) {
                    temp.add(index);
                    break;
                }
            }
            banner.setUri(bannerImages[index]);
            banner.setHtmlUrl(banner.getUri());
            banners.add(banner);
        }

        introduces = new ArrayList<>();
        Introduce introduce1 = new Introduce();
        introduce1.setTitle("交易所介绍");
        introduce1.setUri("http://baike.baidu.com/link?url=NZqBMBM_BHQzTUzJvFlnUJE5Ig6Dob0NkqYLfLfnpcJNAs5rv1Y0IRJBkwMNjfcdUEhDh1sAqBDxQA_WBx0qRG0LCdaRg-5c_PG4RCaDf_Ei3wrVQb79W7skBFHujs2Q");
        introduces.add(introduce1);

        Introduce introduce2 = new Introduce();
        introduce2.setTitle("app介绍");
        introduce2.setUri("http://baike.baidu.com/link?url=3Yzh4daPzS3uAXcRanFZx7VWyrT5W-MkVKRV4Wa-EoAp-YhLwMh-Z1yt14ebyqDYZznCQXOOkDDreuxmRxiRomNOI2M0uZxNuAFJRFiLMXwFlH9kll5JzzajpMdtMQOa7K2oY74vp8Sq9npdyPyq79FiYWGra17utJNLw-nmJXe");
        introduces.add(introduce2);

        products = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product product = new Product();
            product.setTitile("交互盈A" + RandomUtils.randomByMinMax(100000, 999999));
            product.setEarnings(RandomUtils.randomByMinMax(1, 20) + "%");
            product.setDate(RandomUtils.randomByMinMax(10, 20) + "天");
            product.setPercentage(RandomUtils.randomByMinMax(20, 100) + "%");
            if (i == 0) {
                product.setGroupTitle("固收理财 > 分组1");
            }
            if (i == 6) {
                product.setGroupTitle("固收理财 > 分组2");
            }
            products.add(product);
        }
    }

    public List<Banner> getBanners() {
        return banners;
    }

    public List<Introduce> getIntroduces() {
        return introduces;
    }

    public List<Product> getProducts() {
        return products;
    }

    public static class Banner {
        private String uri;
        private String htmlUrl;

        public String getHtmlUrl() {
            return htmlUrl;
        }

        public void setHtmlUrl(String pHtmlUrl) {
            htmlUrl = pHtmlUrl;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String pUri) {
            uri = pUri;
        }
    }

    public static class Introduce {
        private String title;
        private String uri;

        public String getTitle() {
            return title;
        }

        public void setTitle(String pTitle) {
            title = pTitle;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String pUri) {
            uri = pUri;
        }
    }

    public static class Product {
        private String titile;
        private String earnings;
        private String date;
        private String percentage;
        private String groupTitle;

        public String getTitile() {
            return titile;
        }

        public void setTitile(String pTitile) {
            titile = pTitile;
        }

        public String getEarnings() {
            return earnings;
        }

        public void setEarnings(String pEarnings) {
            earnings = pEarnings;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String pDate) {
            date = pDate;
        }

        public String getPercentage() {
            return percentage;
        }

        public void setPercentage(String pPercentage) {
            percentage = pPercentage;
        }

        public String getGroupTitle() {
            return groupTitle;
        }

        public void setGroupTitle(String pGroupTitle) {
            groupTitle = pGroupTitle;
        }
    }
}
