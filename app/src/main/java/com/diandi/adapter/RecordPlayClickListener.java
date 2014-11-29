package com.diandi.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;

import com.diandi.R;

import cn.bmob.im.BmobPlayManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.inteface.OnPlayChangeListener;
import cn.bmob.im.util.BmobLog;


/**
 * *******************************************************************************
 * *********    Author : klob(kloblic@gmail.com) .
 * *********    Date : 2014-11-29  .
 * *********    Time : 11:46 .
 * *********    Project name : Diandi1.18 .
 * *********    Version : 1.0
 * *********    Copyright @ 2014, klob, All Rights Reserved
 * *******************************************************************************
 */
public class RecordPlayClickListener implements View.OnClickListener {

    public static RecordPlayClickListener currentPlayListener = null;
    static BmobMsg currentMsg = null;// 用于区分两个不同语音的播放
    BmobMsg message;
    ImageView iv_voice;
    BmobPlayManager playMananger;
    Context context;

    String currentObjectId = "";
    private AnimationDrawable anim = null;

    public RecordPlayClickListener(Context context, BmobMsg msg, ImageView voice) {
        this.iv_voice = voice;
        this.message = msg;
        this.context = context;
        currentMsg = msg;
        currentPlayListener = this;
        currentObjectId = BmobUserManager.getInstance(context)
                .getCurrentUserObjectId();
        playMananger = BmobPlayManager.getInstance(context);
        playMananger.setOnPlayChangeListener(new OnPlayChangeListener() {

            @Override
            public void onPlayStop() {
                // TODO Auto-generated method stub
                currentPlayListener.stopRecordAnimation();
            }

            @Override
            public void onPlayStart() {
                // TODO Auto-generated method stub
                currentPlayListener.startRecordAnimation();
            }
        });
    }

    /**
     * 开启播放动画
     * @Title: startRecordAnimation
     * @Description: TODO
     * @param
     * @return void
     * @throws
     */
    public void startRecordAnimation() {
        if (message.getBelongId().equals(currentObjectId)) {
            iv_voice.setImageResource(R.anim.anim_chat_voice_right);
        } else {
            iv_voice.setImageResource(R.anim.anim_chat_voice_left);
        }
        anim = (AnimationDrawable)iv_voice.getDrawable();
        anim.start();
    }

    /**
     * 停止播放动画
     *
     * @Title: stopRecordAnimation
     * @Description: TODO
     * @param
     * @return void
     * @throws
     */
    public void stopRecordAnimation() {
        if (message.getBelongId().equals(currentObjectId)) {
            iv_voice.setImageResource(R.drawable.voice_left3);
        } else {
            iv_voice.setImageResource(R.drawable.voice_right3);
        }
        if(anim!=null){
            anim.stop();
        }
    }

    @Override
    public void onClick(View arg0) {
        if (playMananger.isPlaying()) {
            playMananger.stopPlayback();
            if (currentMsg != null
                    && currentMsg.hashCode() == message.hashCode()) {// 是否是同条语音消息
                currentMsg = null;
                return;
            }
        } else {
            String localPath = message.getContent().split("&")[0];
            BmobLog.i("voice", "本地地址:" + localPath);
            if (message.getBelongId().equals(currentObjectId)) {// 如果是自己发送的语音消息，则播放本地地址
                playMananger.playRecording(localPath, true);
            } else {// 如果是收到的消息，则需要先下载后播放

            }
        }

    }

}
