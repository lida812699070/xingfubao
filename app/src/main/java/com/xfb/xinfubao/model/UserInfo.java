package com.xfb.xinfubao.model;

public class UserInfo {

    /**
     * userId : 0
     * nickName :
     * name :
     * headIcon :
     * tel :
     * email :
     * isAuth : true
     * grade :
     * parentId : 0
     * inviteCode :
     */

    private int userId;
    private String nickName;
    private String name;
    private String headIcon;
    private String tel;
    private String email;
    private boolean isAuth;
    /**
     * 0 未认证 1 审核中 2 认证成功
     */
    private int authState;
    private String grade;
    private int parentId;
    private String inviteCode;
    private UserAssets userAssets;

    public int getAuthState() {
        return authState;
    }

    public void setAuthState(int authState) {
        this.authState = authState;
    }

    public UserAssets getUserAssets() {
        return userAssets;
    }

    public void setUserAssets(UserAssets userAssets) {
        this.userAssets = userAssets;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isIsAuth() {
        return isAuth;
    }

    public void setIsAuth(boolean isAuth) {
        this.isAuth = isAuth;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
