package PLCEmulator;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import java.net.*;
import java.io.*;

public class SocketTestServer extends JPanel /*JFrame*/ {
    
	private static final long serialVersionUID = 1L;

	private final String NEW_LINE = "\r\n";
    
    public ImageIcon logo = null;
    
    private JPanel topPanel;
    private JPanel toPanel;
    
    private JPanel centerPanel;
    private JPanel textPanel;
    private JPanel buttonPanel;
    private JPanel sendPanel;
    
    private JPanel rightPanel;
    private JPanel rightTopPanel;
    
    private JPanel leftPanel;
    private JPanel leftTopPanel;
    
    //TopPanel
    	//ToPanel
    private JLabel ipLabel = new JLabel("IP Address");
    private JLabel portLabel = new JLabel("Port");
    private JTextField ipField = new JTextField("178.212.112.147",20);
    private JTextField portField = new JTextField("5001",10);
    private JButton connectButton = new JButton("Start Listening");
    
    //Center Panel
    	//Text Panel
    private JLabel convLabel = new JLabel("Conversation with Client");
    private Border connectedBorder = BorderFactory.createTitledBorder(new EtchedBorder(),"Connected Client : < NONE >");
    private JTextArea messagesField = new JTextArea();
    	//Send Panel
    private JButton sendWtcoButton = new JButton("Confirm WT");
    private JButton sendStateButton = new JButton("State");
    private JButton sendSPButton = new JButton("SP");
    private JButton disconnectButton = new JButton("Disconnect");
    	//ButtonPanel
    private JButton clearButton = new JButton("Clear");
    
    //Right Panel
    String[] data = {""};
    private JList<String> wt_list = new JList<String>(data);
    private JScrollPane wt_scroll_list = new JScrollPane(wt_list);
    	//Right top panel
    private JButton confirmButton = new JButton("Confirm");
    private JButton showButton = new JButton("Show");
    
    //Left Panel
    private JList<String> cp_list = new JList<String>(data);
    private JScrollPane cp_scroll_list = new JScrollPane(cp_list);
    	//Left top panel
    private JButton errorButton = new JButton("Set Error");
    private JButton cpshowButton = new JButton("Show");

    private GridBagConstraints gbc = new GridBagConstraints();
    
    private Socket socket;
    private ServerSocket server;
    private SocketServer socketServer;
    private BufferedWriter out;
    private TelegrammHandler th;
    
    protected final JFrame parent;
    
    public SocketTestServer(final JFrame parent) {
        this.parent = parent;
        Container cp = this;
        
        topPanel = new JPanel();
        toPanel = new JPanel();
        toPanel.setLayout(new GridBagLayout());
        gbc.insets = new Insets( 2, 2, 2, 2 );
        gbc.weighty = 0.0;
        gbc.weightx = 0.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        toPanel.add(ipLabel, gbc);
        
        gbc.weightx = 1.0; //streach
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        ActionListener ipListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                portField.requestFocus();
            }
        };
        ipField.addActionListener(ipListener);
        toPanel.add(ipField, gbc);
        
        gbc.weightx = 0.0;
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        toPanel.add(portLabel, gbc);
        
        gbc.weightx = 1.0;
        gbc.gridy = 1;
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        ActionListener connectListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connect();
            }
        };
        portField.addActionListener(connectListener);
        toPanel.add(portField, gbc);
        
        gbc.weightx = 0.0;
        gbc.gridy = 1;
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.gridy = 1;
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        connectButton.setMnemonic('S');
        connectButton.setToolTipText("Start Listening");
        connectButton.addActionListener(connectListener);
        toPanel.add(connectButton, gbc);
        
        toPanel.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(),"Listen On"));
        topPanel.setLayout(new BorderLayout(10,0));
        topPanel.add(toPanel);
        /*
        logoLabel.setVerticalTextPosition(JLabel.BOTTOM);
        logoLabel.setHorizontalTextPosition(JLabel.CENTER);
        topPanel.add(logoLabel,BorderLayout.EAST);
        */
        topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,5,10));
        
        textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout(0,5));
        textPanel.add(convLabel,BorderLayout.NORTH);
        messagesField.setEditable(false);
        JScrollPane jsp = new JScrollPane(messagesField);
        textPanel.add(jsp);
        textPanel.setBorder(BorderFactory.createEmptyBorder(3,3,0,3));
        
        sendPanel = new JPanel();
        sendPanel.setLayout(new GridBagLayout());
        gbc.weighty = 0.0;
        gbc.weightx = 0.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        
        sendWtcoButton.setEnabled(false);
        sendWtcoButton.setToolTipText("Confirm warehouse task");
        ActionListener sendWtcoListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	JTextField hu = new JTextField();
            	JTextField hutype = new JTextField();
            	JTextField source = new JTextField();
        		JTextField dest = new JTextField();
                        Object[] message = {"HU-ID", hu, 
                		"HU type", hutype, "Source", source, "Destination", dest};
                        hutype.setText("E1");
         
                        JOptionPane pane = new JOptionPane( message, 
                                                        JOptionPane.PLAIN_MESSAGE, 
                                                        JOptionPane.OK_CANCEL_OPTION);
                        pane.createDialog(null, "Confirm warehouse task").setVisible(true);
                        
                        int value = ((Integer)pane.getValue()).intValue();	                        
    	                if(value == JOptionPane.OK_OPTION) {	
    	                	th.confirmWt(hu.getText(), hutype.getText(), source.getText(), dest.getText(), "");
    	                }
            }
        };
        sendWtcoButton.addActionListener(sendWtcoListener);
        sendPanel.add(sendWtcoButton, gbc);
        /*
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        sendField.setEditable(false);
        sendPanel.add(sendField, gbc);
        */
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        sendStateButton.setEnabled(false);
        sendStateButton.setToolTipText("Send state message");
        ActionListener sendStateListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	JTextField cp = new JTextField();
        		JTextField fehler = new JTextField();
                        Object[] message = {"CP", cp, 
                		"Fehler", fehler};
         
                        JOptionPane pane = new JOptionPane( message, 
                                                        JOptionPane.PLAIN_MESSAGE, 
                                                        JOptionPane.OK_CANCEL_OPTION);
                        pane.createDialog("State message").setVisible(true);

                        int value = ((Integer)pane.getValue()).intValue();	                        
    	                if(value == JOptionPane.OK_OPTION) {	
    	                	th.sendStateMsg(cp.getText(), fehler.getText());
    	                }
            }
        };
        sendStateButton.addActionListener(sendStateListener);
        //sendField.addActionListener(sendListener);
        sendPanel.add(sendStateButton, gbc);
        
        gbc.gridx = 3;
        sendSPButton.setEnabled(false);
        sendSPButton.setToolTipText("Send SP message");
        ActionListener spListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	JTextField hu = new JTextField();
        		JTextField hutype = new JTextField();
                        Object[] message = {"HU", hu, 
                		"HU Type", hutype};
         
                        JOptionPane pane = new JOptionPane( message, 
                                                        JOptionPane.PLAIN_MESSAGE, 
                                                        JOptionPane.OK_CANCEL_OPTION);
                        pane.createDialog("Start Point").setVisible(true);

                        int value = ((Integer)pane.getValue()).intValue();	                        
    	                if(value == JOptionPane.OK_OPTION) {	
    	                	th.sendSPMsg(hu.getText(), hutype.getText(), "CP01");
    	                }
            }
        };
        sendSPButton.addActionListener(spListener);
        sendPanel.add(sendSPButton, gbc);
        
        ActionListener disconnectListener = new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		disconnect();
        	}
        };
        
        gbc.gridx = 4;
        disconnectButton.addActionListener(disconnectListener);
        disconnectButton.setEnabled(false);
        sendPanel.add(disconnectButton, gbc);
        
        sendPanel.setBorder(
                new CompoundBorder(
                BorderFactory.createEmptyBorder(0,0,0,3),
                BorderFactory.createTitledBorder("Send")));
        
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        gbc.weighty = 0.0;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.BOTH;
        buttonPanel.add(sendPanel, gbc);
        gbc.weighty = 0.0;
        gbc.weightx = 0.0;
        gbc.gridx = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1;
        clearButton.setToolTipText("Clear conversation with client");
        clearButton.setMnemonic('C');
        ActionListener clearListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                messagesField.setText("");
            }
        };
        clearButton.addActionListener(clearListener);
        buttonPanel.add(clearButton, gbc);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(3,0,0,3));
        
        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(0,10));
        centerPanel.add(buttonPanel,BorderLayout.SOUTH);
        centerPanel.add(textPanel,BorderLayout.CENTER);
        
        CompoundBorder cb=new CompoundBorder(BorderFactory.createEmptyBorder(5,0,10,0),connectedBorder);
        //CompoundBorder cb=new CompoundBorder(connectedBorder, BorderFactory.createEmptyBorder(10,10,10,10));
        centerPanel.setBorder(cb);
        
        //right top panel
        rightTopPanel = new JPanel();
        rightTopPanel.setLayout(new BorderLayout(5,0));
        ActionListener showWtListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int i = wt_list.getSelectedIndex();
                if(i != -1){
	                String[] s = th.getWtInfo(i);
	                if(s[1]!=null){
		                JOptionPane.showConfirmDialog(null,                       
		                        "HU-Number: "+s[0]+NEW_LINE+"HU Type: "+s[1]+NEW_LINE+"Source: "+s[2]+NEW_LINE+"Destination: "+s[3],
		                        "Warehouse Task",
		                        JOptionPane.OK_CANCEL_OPTION);
	                }
                }
            }
        };
        showButton.addActionListener(showWtListener);
        showButton.setEnabled(false);
        rightTopPanel.add(showButton,BorderLayout.WEST);
        ActionListener confirmWtListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int i = wt_list.getSelectedIndex();
                if(i != -1){
                	String[] s = th.getWtInfo(i);
	                if(s!=null){
		                int value = JOptionPane.showConfirmDialog(null,                       
		                        "HU-Number: "+s[0]+NEW_LINE+"HU Type: "+s[1]+NEW_LINE+"Source: "+s[2]+NEW_LINE+"Destination: "+s[3]+NEW_LINE+
		                        "Do you really want to confirm this warehouse task?",
		                        "Are you sure?",
		                        JOptionPane.YES_NO_OPTION);
		                if(i != -1 && value == JOptionPane.YES_OPTION){
		                	th.confirmWt(i);
		                }
	                }
                }
            }
        };
        confirmButton.addActionListener(confirmWtListener);
        confirmButton.setEnabled(false);
        rightTopPanel.add(confirmButton,BorderLayout.EAST);
        rightTopPanel.setBorder(BorderFactory.createEmptyBorder(3,0,3,0) );
        
        //right panel
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout(5,0));
        rightPanel.add(rightTopPanel, BorderLayout.NORTH);
        rightPanel.add(wt_scroll_list,BorderLayout.CENTER);
        rightPanel.setBorder(
        		new CompoundBorder(
        				BorderFactory.createEmptyBorder(5,5,10,10), 
        				BorderFactory.createTitledBorder(new EtchedBorder(),"Warehousetasks")));
        
        //left top panel
        leftTopPanel = new JPanel();
        leftTopPanel.setLayout(new BorderLayout(5,0));
        ActionListener showCpListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int i = cp_list.getSelectedIndex();
                if(i != -1){
	                String[] s = th.getCpInfo(i);
	                JOptionPane.showConfirmDialog(null,                       
	                        "Name: "+s[0]+NEW_LINE+"Error: "+s[1]+NEW_LINE+"HU: "+s[2],
	                        "Communication Point",
	                        JOptionPane.OK_CANCEL_OPTION);
                }
            }
        };
        cpshowButton.setEnabled(false);
        cpshowButton.addActionListener(showCpListener);
        leftTopPanel.add(cpshowButton,BorderLayout.WEST);
        ActionListener cpErrorListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int i = cp_list.getSelectedIndex();
                if(i != -1){
	        		JTextField fehler = new JTextField();
	                Object[] message = {"Fehler", fehler};
	         
	                JOptionPane pane = new JOptionPane( message, 
	                                                    JOptionPane.PLAIN_MESSAGE, 
	                                                    JOptionPane.OK_CANCEL_OPTION);
	                pane.createDialog("Set Error").setVisible(true);

	                int value = ((Integer)pane.getValue()).intValue();	                        
	                if(value == JOptionPane.OK_OPTION) {	
	                   th.setCpError(i,fehler.getText());
	                }
	                
	            }
            }
        };
        errorButton.addActionListener(cpErrorListener);
        errorButton.setEnabled(false);
        leftTopPanel.add(errorButton,BorderLayout.EAST);
        leftTopPanel.setBorder(BorderFactory.createEmptyBorder(3,0,3,0) );
        
        //left panel
        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout(5,0));
        leftPanel.add(leftTopPanel, BorderLayout.NORTH);
        leftPanel.add(cp_scroll_list,BorderLayout.CENTER);
        leftPanel.setBorder(
        		new CompoundBorder(
        				BorderFactory.createEmptyBorder(5,10,10,5),
        				BorderFactory.createTitledBorder(new EtchedBorder(),"Check Points")));
        
        //content pane
        cp.setLayout(new BorderLayout(10,0));
        cp.add(topPanel,BorderLayout.NORTH);
        cp.add(centerPanel,BorderLayout.CENTER);
        cp.add(rightPanel,BorderLayout.EAST);
        cp.add(leftPanel,BorderLayout.WEST);
    }
    

    private void connect() {
        if(server!=null) {
            stop();
            return;
        }
        String ip=ipField.getText();
        String port=portField.getText();
        if(ip==null || ip.equals("")) {
            JOptionPane.showMessageDialog(SocketTestServer.this,
                    "No IP Address. Please enter IP Address",
                    "Error connecting", JOptionPane.ERROR_MESSAGE);
            ipField.requestFocus();
            ipField.selectAll();
            return;
        }
        if(port==null || port.equals("")) {
            JOptionPane.showMessageDialog(SocketTestServer.this,
                    "No Port number. Please enter Port number",
                    "Error connecting", JOptionPane.ERROR_MESSAGE);
            portField.requestFocus();
            portField.selectAll();
            return;
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(!Util.checkHost(ip)) {
            JOptionPane.showMessageDialog(SocketTestServer.this,
                    "Bad IP Address",
                    "Error connecting", JOptionPane.ERROR_MESSAGE);
            ipField.requestFocus();
            ipField.selectAll();
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            return;
        }
        int portNo = 0;
        try	{
            portNo=Integer.parseInt(port);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(SocketTestServer.this,
                    "Bad Port number. Please enter Port number",
                    "Error connecting", JOptionPane.ERROR_MESSAGE);
            portField.requestFocus();
            portField.selectAll();
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            return;
        }
        try {
            InetAddress bindAddr=null;
            if(!ip.equals("0.0.0.0"))
                bindAddr = InetAddress.getByName(ip);
            else
                bindAddr = null;
            server = new ServerSocket(portNo,1,bindAddr);
            
            ipField.setEditable(false);
            portField.setEditable(false);
            
            connectButton.setText("Stop Listening");
            connectButton.setMnemonic('S');
            connectButton.setToolTipText("Stop Listening");

        } catch (Exception e) {
            error(e.getMessage(), "Starting Server at "+portNo);
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            return;
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        messagesField.setText("> Server Started on Port: "+portNo+NEW_LINE);
        writeTrace("> Server Started on Port: "+portNo);
        append("> ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        socketServer=SocketServer.handle(this,server);
        th = socketServer.getTh();
        //sendField.requestFocus();
    }
    //disconnect a client
    public synchronized void disconnect() {
        try {
            socketServer.setDesonnected(true);
        } catch (Exception e) {}
    }
    
    public synchronized void stop() {
        try {
            disconnect(); //close any client
            socketServer.setStop(true);
        } catch (Exception e) {}
        server=null;
        ipField.setEditable(true);
        portField.setEditable(true);
        connectButton.setText("Start Listening");
        connectButton.setMnemonic('S');
        connectButton.setToolTipText("Start Listening");
        append("> Server stopped");
        append("> ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
    
    public synchronized void setClientSocket(Socket s) {
        
        if(s==null) {
            out=null;
            socket = null;
            changeBorder(null);
            sendStateButton.setEnabled(false);
            sendWtcoButton.setEnabled(false);
            //sendField.setEditable(false);
            disconnectButton.setEnabled(false);
            showButton.setEnabled(false);
            confirmButton.setEnabled(false);
            cpshowButton.setEnabled(false);
            errorButton.setEnabled(false);
            sendSPButton.setEnabled(false);
        } 
        else {
            socket = s;
            changeBorder(" "+socket.getInetAddress().getHostName()+
                    " ["+socket.getInetAddress().getHostAddress()+"] ");
            sendStateButton.setEnabled(true);
            sendWtcoButton.setEnabled(true);
            //sendField.setEditable(true);
            disconnectButton.setEnabled(true);
            showButton.setEnabled(true);
            confirmButton.setEnabled(true);
            cpshowButton.setEnabled(true);
            errorButton.setEnabled(true);
            sendSPButton.setEnabled(true);
        }
    }
    
    public void error(String error) {
        if(error==null || error.equals(""))
            return;
        JOptionPane.showMessageDialog(SocketTestServer.this,
                error, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void error(String error, String heading) {
        if(error==null || error.equals(""))
            return;
        JOptionPane.showMessageDialog(SocketTestServer.this,
                error, heading, JOptionPane.ERROR_MESSAGE);
    }
    
    public void append(String msg) {
        messagesField.append(msg+NEW_LINE);
        writeTrace(msg);
        messagesField.setCaretPosition(messagesField.getText().length());
    }
    
    public void appendnoNewLine(String msg) {
        messagesField.append(msg);
        writeTrace(msg);
        messagesField.setCaretPosition(messagesField.getText().length());
    }
    
    public void sendMessage(String s) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try	{
            if(out==null) {
            	out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            }
            append("Gesendet: "+s);
            out.write(s);
            out.flush();
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } catch (Exception e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            JOptionPane.showMessageDialog(SocketTestServer.this,
                    e.getMessage(),"Error Sending Message",
                    JOptionPane.ERROR_MESSAGE);
            disconnect();
        }
    }
    
    private void changeBorder(String ip) {
        if(ip==null || ip.equals(""))
            connectedBorder = BorderFactory.createTitledBorder(
                    new EtchedBorder(), "Connected Client : < NONE >");
        else
            connectedBorder = BorderFactory.createTitledBorder(
                    new EtchedBorder(), "Connected Client : < "+ip+" >");
        CompoundBorder cb=new CompoundBorder(BorderFactory.createEmptyBorder(5,0,10,0),connectedBorder);
        centerPanel.setBorder(cb);
        invalidate();
        repaint();
    }    
    
    private static void writeTrace(String DataIn){
    	java.util.Date now = new java.util.Date();
    	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy HH.mm.ss");
    	String trace = sdf.format(now) + ": \r\n" + DataIn;
    	Util.writeTrace(trace);
    }
    
    public void setWtListData(String[] data){
    	wt_list.setListData(data);
    }
    
    public void setCpListData(String[] data){
    	cp_list.setListData(data);
    }
}

