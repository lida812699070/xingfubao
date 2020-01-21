package com.xfb.xinfubao.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class ItemBalanceModel implements MultiItemEntity {
    private int itemType = 0;

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
