package com.diandi.bean;

/**
 * ************************************************************
 * *********    User : SuLinger(462679107@qq.com) .
 * *********    Date : 2014-09-29  .
 * *********    Time:  2014-09-29  .
 * *********    Project name :Diandi1.17 .
 * *********    Copyright @ 2014, SuLinger, All Rights Reserved
 * *************************************************************
 */


public class OfficialDiandi extends DianDi {
    public final static String CHANNEL="channel";
    private String link;
    private String channel;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
