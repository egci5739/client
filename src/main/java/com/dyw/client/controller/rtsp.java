package com.dyw.client.controller;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import javax.swing.*;
import java.awt.*;

public class rtsp {

    private JPanel main;
    private JPanel video;
    private rtsp rtsp;

    public static void main(String[] args) {
        rtsp rtsp = new rtsp();
        rtsp.init();
    }

    public void init() {
        //用guiform构建的swing窗体部分
        JFrame frame = new JFrame("rtsp");
        frame.setContentPane(this.main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


        //加载vlc播放器相关库
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "vlc"); // vlc : libvlc.dll,libvlccore.dll和plugins目录的路径,这里我直接放到了项目根目录下
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

        //将播放窗口放入swing窗体
        EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        rtsp.video.setLayout(new GridLayout(1, 1, 2, 2));
        rtsp.video.add(mediaPlayerComponent);
        rtsp.video.updateUI();

        //设置参数并播放
        EmbeddedMediaPlayer mediaPlayer = mediaPlayerComponent.getMediaPlayer();
        String[] options = {"rtsp-tcp", "network-caching=300"}; //配置参数 rtsp-tcp作用: 使用 RTP over RTSP (TCP) (默认关闭),network-caching=300:网络缓存300ms,设置越大延迟越大,太小视频卡顿,300较为合适
        mediaPlayer.playMedia("rtsp://admin:hik12345@192.168.3.103:554/Streaming/Channels/101?transportmode=unicast", options); //播放rtsp流
        mediaPlayer.start();
    }
}

