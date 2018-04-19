package com.erongdu.wireless.permissions;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/4/18 上午10:20
 * <p>
 * Description:
 */
public class ItemModel {
    private String group;
    private String name;

    public ItemModel(String group, String name) {
        this.group = group;
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
