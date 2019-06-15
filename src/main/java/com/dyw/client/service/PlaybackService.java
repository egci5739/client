package com.dyw.client.service;

import com.dyw.client.HCNetSDK;
import com.dyw.client.controller.Egci;
import com.dyw.client.functionForm.PlaybackFunction;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.examples.win32.W32API.HWND;
import com.sun.jna.ptr.IntByReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.*;

/*
 * 远程按时间回放操作
 * */
public class PlaybackService extends javax.swing.JDialog {
    private Logger logger = LoggerFactory.getLogger(PlaybackService.class);
    private java.awt.Panel panelPlay;
    private NativeLong m_lUserID;//用户ID
    private NativeLong m_lPlayHandle;//播放句柄
    private boolean m_bPause;//是否已暂停
    private Timer PlaybackTimer;//回放用定时器
    private LoginService loginService;
    private PlaybackFunction playbackFunction;

    /*
     * 构造函数
     * */
    public PlaybackService() {
        //登陆NVR
        loginService = new LoginService();
        loginService.login(Egci.configEntity.getNvrServerIp(), (short) Egci.configEntity.getNvrServerPort(), "admin", "hik12345");
        m_lUserID = loginService.getlUserID();
        //打开播放窗口
        playbackFunction = new PlaybackFunction();
        playbackFunction.init();
        panelPlay = playbackFunction.getPanelPlay();
        m_lPlayHandle = new NativeLong(-1);
    }

    /*
     * 开始或暂停按时间回放
     * */
    public void playOrPause(HCNetSDK.NET_DVR_TIME struStartTime, HCNetSDK.NET_DVR_TIME struStopTime, int m_iChanShowNum) {
        if (m_lPlayHandle.intValue() == -1) {
            //获取窗口句柄
            HWND hwnd = new HWND();
            hwnd.setPointer(Native.getComponentPointer(panelPlay));
            m_lPlayHandle = Egci.hcNetSDK.NET_DVR_PlayBackByTime(m_lUserID, new NativeLong(m_iChanShowNum), struStartTime, struStopTime, hwnd);
            if (m_lPlayHandle.intValue() == -1) {
                JOptionPane.showMessageDialog(this, "按时间回放失败");
                return;
            } else {
                //还要调用该接口才能开始回放
                Egci.hcNetSDK.NET_DVR_PlayBackControl(m_lPlayHandle, HCNetSDK.NET_DVR_PLAYSTART, 0, null);
                logger.info("开始回放");
            }
            //开始计时器
            PlaybackTimer = new Timer();//新建定时器
            PlaybackTimer.schedule(new PlaybackTask(), 0, 3000);//0秒后开始响应函数
        } else {
            if (m_bPause) {
                if (Egci.hcNetSDK.NET_DVR_PlayBackControl(m_lPlayHandle, HCNetSDK.NET_DVR_PLAYRESTART, 0, null)) {
                    m_bPause = false;
                }
            } else {
                if (Egci.hcNetSDK.NET_DVR_PlayBackControl(m_lPlayHandle, HCNetSDK.NET_DVR_PLAYPAUSE, 0, null)) {
                    m_bPause = true;
                }
            }
        }
    }

    /*
     * 回放定时器响应函数
     * */
    class PlaybackTask extends java.util.TimerTask {
        //定时器函数
        @Override
        public void run() {
            IntByReference nPos = new IntByReference(0);
            if (m_lPlayHandle.intValue() >= 0) {
                if (Egci.hcNetSDK.NET_DVR_PlayBackControl(m_lPlayHandle, HCNetSDK.NET_DVR_PLAYGETPOS, 0, nPos)) {
                    System.out.println("回放进度" + nPos.getValue());
                } else {
                    System.out.println("获取回放进度失败");
                }
                if (nPos.getValue() > 100) {
                    StopPlay();
                    JOptionPane.showMessageDialog(null, "由于网络原因或DVR忙,回放异常终止!");
                }
                if (nPos.getValue() == 100) {
                    StopPlay();
                }
            }
        }
    }

    /*
     * 停止回放的相关操作
     * */
    private void StopPlay() {
        if (m_lPlayHandle.intValue() >= 0) {
            Egci.hcNetSDK.NET_DVR_PlayBackControl(m_lPlayHandle, HCNetSDK.NET_DVR_PLAYSTOPAUDIO, 0, null);
            Egci.hcNetSDK.NET_DVR_StopPlayBack(m_lPlayHandle);
            m_lPlayHandle.setValue(-1);
            PlaybackTimer.cancel();
            panelPlay.repaint();
            loginService.logout();
            playbackFunction.dispose();
        }
    }
}

