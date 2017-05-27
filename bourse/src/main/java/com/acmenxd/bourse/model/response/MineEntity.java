package com.acmenxd.bourse.model.response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/8 11:35
 * @detail something
 */
public class MineEntity {
    private List<Item> items;
    public MineEntity() {
        items = new ArrayList<>();
        items.add(new Item("账户余额","可用余额0.00元","查看我的账户"));
        items.add(new Item("风险评测","风险评测 成为合格投资人","去查看"));
        items.add(new Item("安全保障","金融机构发行产品","去查看"));
        items.add(new Item("关于我们","优质资产尽在xx理财超市","去查看"));
        items.add(new Item("帮助中心","为何起投金额那么高?","去查看"));
    }

    public List<Item> getItems() {
        return items;
    }

    public static class Item {
        private String title;
        private String detail;
        private String go;

        public Item(String pTitle, String pDetail, String pGo) {
            title = pTitle;
            detail = pDetail;
            go = pGo;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String pTitle) {
            title = pTitle;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String pDetail) {
            detail = pDetail;
        }

        public String getGo() {
            return go;
        }

        public void setGo(String pGo) {
            go = pGo;
        }
    }

}
