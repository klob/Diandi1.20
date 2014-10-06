package com.diandi.bean;


import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 重载BmobChatUser对象：若还有其他需要增加的属性可在此添加
 *
 * @author smile
 * @ClassName: TextUser
 * @Description: TODO
 * @date 2014-5-29 下午6:15:45
 */
public class User extends BmobChatUser {

    /**
     * BmobObject
     * private static final long serialVersionUID = 3265156078627207765L;
     * private java.lang.String objectId;
     * private java.lang.String createdAt;
     * private java.lang.String updatedAt;
     * private cn.bmob.v3.BmobACL ACL;
     * private java.lang.String _c_;
     * protected static java.util.List<org.json.JSONObject> increments;
     * private static org.json.JSONObject data;
     * <p/>
     * BmobUser
     * <p/>
     * private java.lang.String username;
     * private java.lang.String password;
     * private java.lang.String email;
     * private java.lang.Boolean emailVerified;
     * private java.lang.String sessionToken;
     * static org.json.JSONObject current;
     * <p/>
     * BmobChatUser
     * <p/>
     * private java.lang.String nick;
     * private java.lang.String avatar;
     * private cn.bmob.v3.datatype.BmobRelation contacts;
     * private java.lang.String installId;
     * private java.lang.String deviceType;
     * private cn.bmob.v3.datatype.BmobRelation blacklist;
     */

    private static final long serialVersionUID = 2L;
    private String sortLetters;
    private String sex;
    private BmobGeoPoint location;
    private String signature;
    private String school;
    private BmobRelation favorite;
    private BmobFile avatarImg;
    private boolean official;
    private boolean V;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "User{" +
                "UserName'" + getUsername() + '\'' +
                ",  Nick='" + getNick() + '\'' +
                ", V=" + isV() +
                ", official=" + isOfficial() +
                '}';
    }

    public boolean isOfficial() {
        return official;
    }

    public void setOfficial(boolean official) {
        this.official = official;
    }

    public boolean isV() {
        return V;
    }

    public void setV(boolean v) {
        V = v;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public BmobRelation getFavorite() {
        return favorite;
    }

    public void setFavorite(BmobRelation favorite) {
        this.favorite = favorite;
    }

    public BmobFile getAvatarImg() {
        return avatarImg;
    }

    public void setAvatarImg(BmobFile avatarImg) {
        this.avatarImg = avatarImg;
    }


    public BmobGeoPoint getLocation() {
        return location;
    }

    public void setLocation(BmobGeoPoint location) {
        this.location = location;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

}
