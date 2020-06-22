package com.xfb.xinfubao.model;

import java.util.ArrayList;
import java.util.List;

/** 收银台数据 */
public class CashRegisterModel {
    private double totalAmount;
    private List<PayMethod> payMethod;
    private List<RegisterOrderVo> registerOrderVos;
    private ArrayList<SelectBalanceModel> favorableVosVos;

    public ArrayList<SelectBalanceModel> getFavorableVosVos() {
        return favorableVosVos;
    }

    public void setFavorableVosVos(ArrayList<SelectBalanceModel> favorableVosVos) {
        this.favorableVosVos = favorableVosVos;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<PayMethod> getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(List<PayMethod> payMethod) {
        this.payMethod = payMethod;
    }

    public List<RegisterOrderVo> getRegisterOrderVos() {
        return registerOrderVos;
    }

    public void setRegisterOrderVos(List<RegisterOrderVo> registerOrderVos) {
        this.registerOrderVos = registerOrderVos;
    }
}
