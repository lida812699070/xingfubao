package com.xfb.xinfubao.model;

import java.util.List;

public class HomeModel {
    private List<BannerModel> banner;
    private String moduleName;
    private List<Product> product;
    private List<HomeModule> module;

    public List<HomeModule> getModule() {
        return module;
    }

    public void setModule(List<HomeModule> module) {
        this.module = module;
    }

    public List<BannerModel> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerModel> banner) {
        this.banner = banner;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }
}
