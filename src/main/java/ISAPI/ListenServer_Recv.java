package ISAPI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.sun.net.httpserver.spi.HttpServerProvider;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Window.Type;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class ListenServer_Recv extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField ListenServerPort;

    JTextArea textListenServerPane = new JTextArea();
    JsonFormatTool JsonFormatTool = new JsonFormatTool();

    public HttpServer httpserver = null;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            ListenServer_Recv dialog = new ListenServer_Recv();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public ListenServer_Recv() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                httpserver.stop(1);
            }
        });
        setType(Type.UTILITY);
        setTitle("ListenServer_Recv");
        setBounds(100, 100, 436, 629);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblNewLabel_1 = new JLabel("Listen Server Port");
        lblNewLabel_1.setBounds(10, 557, 115, 15);
        contentPanel.add(lblNewLabel_1);

        ListenServerPort = new JTextField();
        ListenServerPort.setBounds(135, 554, 66, 21);
        ListenServerPort.setText("8080");
        contentPanel.add(ListenServerPort);
        ListenServerPort.setColumns(10);

        final JButton btnStartListen = new JButton("Start Listen");
        btnStartListen.setBounds(225, 553, 115, 23);
        btnStartListen.addActionListener(new ActionListener() {


            class MyHttpHandler implements HttpHandler {

                public void handle(HttpExchange httpExchange) throws IOException {

                    String requestMethod = httpExchange.getRequestMethod();
                    if (requestMethod.equalsIgnoreCase("POST")) {

                        InputStreamReader ISR = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
                        BufferedReader br = new BufferedReader(ISR);

                        String strout = "";
                        String temp = "";
                        while ((temp = br.readLine()) != null) {
                            strout = strout + temp;
                        }

                        httpExchange.sendResponseHeaders(200, 0);
                        httpExchange.close();

                        textListenServerPane.setText(JsonFormatTool.formatJson(strout));
                    }

                }

            }

            public void Listener() {

                try {

                    if (btnStartListen.getText() == "Start Listen") {
                        HttpServerProvider provider = HttpServerProvider.provider();
                        httpserver = provider.createHttpServer(new InetSocketAddress(Integer.parseInt(ListenServerPort.getText())), 100);
                        httpserver.createContext("/alarm", new MyHttpHandler());
                        httpserver.setExecutor(null);
                        httpserver.start();
                        textListenServerPane.setText("Listen Start success");

                        btnStartListen.setText("Stop Listen");
                    } else {
                        httpserver.stop(1);
                        textListenServerPane.setText("Listen Stop success");

                        btnStartListen.setText("Start Listen");
                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch blocks
                    e.printStackTrace();

                    textListenServerPane.setText("Listen error");
                }//�����˿�8080,��ͬʱ�� ��100������

            }

            public void actionPerformed(ActionEvent arg0) {
                Listener();
            }
        });
        contentPanel.add(btnStartListen);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 10, 404, 533);
        contentPanel.add(scrollPane);
        scrollPane.setViewportView(textListenServerPane);
        {
            JPanel buttonPane = new JPanel();
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setBounds(316, 5, 45, 23);
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                    }
                });
                buttonPane.setLayout(null);
                {
                    JLabel lblNewLabel = new JLabel("New label");
                    lblNewLabel.setBounds(257, 9, 54, 15);
                    buttonPane.add(lblNewLabel);
                }
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setBounds(366, 5, 69, 23);
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }
}
