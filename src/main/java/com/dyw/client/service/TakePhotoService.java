package com.dyw.client.service;

import com.dyw.client.form.RegisterForm;
import com.github.sarxos.webcam.*;
import com.github.sarxos.webcam.util.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.ByteBuffer;


/**
 * Proof of concept of how to handle webcam video stream from Java
 *
 * @author Bartosz Firyn (SarXos)
 */
public class TakePhotoService extends JFrame implements Runnable, WebcamListener, WindowListener, UncaughtExceptionHandler, ItemListener, WebcamDiscoveryListener {
    private static final long serialVersionUID = 1L;
    private Webcam webcam = null;
    private WebcamPanel panel = null;
    private WebcamPicker picker = null;
    private JButton takePicture = new JButton("拍照");
    private RegisterForm registerForm;

    public TakePhotoService(RegisterForm registerForm) {
        this.registerForm = registerForm;
    }

    @Override
    public void run() {
        try {
            Webcam.addDiscoveryListener(this);
            setTitle("人像采集");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());
            addWindowListener(this);
            picker = new WebcamPicker();
            picker.addItemListener(this);
            webcam = picker.getSelectedWebcam();
            if (webcam == null) {
                JOptionPane.showMessageDialog(null, "打开摄像头出错");
                this.dispose();
            }
            webcam.setViewSize(WebcamResolution.VGA.getSize());
            webcam.addWebcamListener(TakePhotoService.this);
            panel = new WebcamPanel(webcam, false);
            panel.setFPSDisplayed(true);
            add(picker, BorderLayout.NORTH);
            add(panel, BorderLayout.CENTER);
            add(takePicture, BorderLayout.SOUTH);
            takePicture.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    takePicture.setEnabled(false);  //设置按钮不可点击
                    //实现拍照保存-------start
                    String fileName = System.getProperty("user.dir") + "\\snap.jpg";       //保存路径即图片名称（不用加后缀）
                    WebcamUtils.capture(webcam, fileName, ImageUtils.FORMAT_JPG);//存入本地
                    registerForm.displayPhoto();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
//                            JOptionPane.showMessageDialog(null, "拍照成功");
                            takePicture.setEnabled(true);    //设置按钮可点击
                            return;
                        }
                    });
                }
            });
            pack();
            setVisible(true);
            Thread t = new Thread() {
                @Override
                public void run() {
                    panel.start();
                }
            };
            t.setName("example-starter");
            t.setDaemon(true);
            t.setUncaughtExceptionHandler(this);
            t.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "打开摄像头出错");
            this.dispose();
        }
    }

    @Override
    public void webcamOpen(WebcamEvent we) {
        System.out.println("webcam open");
    }

    @Override
    public void webcamClosed(WebcamEvent we) {
        System.out.println("webcam closed");
    }

    @Override
    public void webcamDisposed(WebcamEvent we) {
        System.out.println("webcam disposed");
    }

    @Override
    public void webcamImageObtained(WebcamEvent we) {
        // do nothing
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
        webcam.close();
    }

    @Override
    public void windowClosing(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        System.out.println("webcam viewer resumed");
        panel.resume();
    }

    @Override
    public void windowIconified(WindowEvent e) {
        System.out.println("webcam viewer paused");
        panel.pause();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.err.println(String.format("Exception in thread %s", t.getName()));
        e.printStackTrace();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getItem() != webcam) {
            if (webcam != null) {

                panel.stop();

                remove(panel);

                webcam.removeWebcamListener(this);
                webcam.close();

                webcam = (Webcam) e.getItem();
                webcam.setViewSize(WebcamResolution.VGA.getSize());
                webcam.addWebcamListener(this);

                System.out.println("selected " + webcam.getName());

                panel = new WebcamPanel(webcam, false);
                panel.setFPSDisplayed(true);

                add(panel, BorderLayout.CENTER);
                pack();

                Thread t = new Thread() {

                    @Override
                    public void run() {
                        panel.start();
                    }
                };
                t.setName("example-stoper");
                t.setDaemon(true);
                t.setUncaughtExceptionHandler(this);
                t.start();
            }
        }
    }

    @Override
    public void webcamFound(WebcamDiscoveryEvent event) {
        if (picker != null) {
            picker.addItem(event.getWebcam());
        }
    }

    @Override
    public void webcamGone(WebcamDiscoveryEvent event) {
        if (picker != null) {
            picker.removeItem(event.getWebcam());
        }
    }
}