package PLCEmulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Thread {
    
    private static SocketServer socketServer = null;
    private Socket socket = null;
    private ServerSocket server = null;
    private SocketTestServer parent;
    private BufferedReader in;
    private boolean desonnected=false;
    private boolean stop = false;
    private TelegrammHandler th;
    private int tele_length;
    
    //disconnect client
    public synchronized void setDesonnected(boolean cr) {
        if(socket!=null && cr==true) {
            try	{
                socket.close();
            } catch (Exception e) {
                System.err.println("Error closing client : setDesonnected : "+e);
            }
        }
        desonnected=cr;
        //parent.setClientSocket(null);
    }
    
    //stop server
    public synchronized void setStop(boolean cr) {
        stop=cr;
        if(server!=null && cr==true) {
            try	{
                server.close();
            } catch (Exception e) {
                System.err.println("Error closing server : setStop : "+e);
            }
        }
    }
    
    private SocketServer(SocketTestServer parent, ServerSocket s) {
        super("SocketServer");
        this.parent = parent;
        th = new TelegrammHandler(parent);
        tele_length = th.getTeleLenght();
        server=s;
        setStop(false);
        setDesonnected(false);
        start();
    }
    
    
    
    public static synchronized SocketServer handle(SocketTestServer parent,
            ServerSocket s) {
        if(socketServer==null)
            socketServer=new SocketServer(parent, s);
        else {
            if(socketServer.server!=null) {
                try	{
                    socketServer.setDesonnected(true);
                    socketServer.setStop(true);
                    if(socketServer.socket!=null)
                        socketServer.socket.close();
                    if(socketServer.server!=null)
                        socketServer.server.close();
                } catch (Exception e)	{
                    parent.error(e.getMessage());
                }
            }
            socketServer.server = null;
            socketServer.socket = null;
            socketServer=new SocketServer(parent,s);
        }
        return socketServer;
    }
    
    public void run() {
        while(!stop) {
            try	{
                socket = server.accept();
            } catch (Exception e) {
                if(!stop) {
                    parent.error(e.getMessage(),"Error acception connection");
                    stop=true;
                }
                continue;
            }
            startServer();
            if(socket!=null) {
                try	{
                    socket.close();
                } catch (Exception e) {
                    System.err.println("Erro closing client socket : "+e);
                }
                socket=null;
                parent.setClientSocket(socket);
            }
        }//end of while
    }//end of run
    
    private void startServer() {
        parent.setClientSocket(socket);
        InputStream is = null;
        parent.append("> New Client: "+socket.getInetAddress().getHostAddress());
        try {
            is = socket.getInputStream();
            in = new BufferedReader(new InputStreamReader(is));
        } catch(IOException e) {
            parent.append("> Cound't open input stream on Client "+e.getMessage());
            setDesonnected(true);
            return;
        }
        
        String rec=null;
        while(true) {
            rec=null;
            try	{
                rec = readInputStream(in);//in.readLine();
            } catch (Exception e) {
                setDesonnected(true);
                if(!desonnected) {
                    parent.error(e.getMessage(),"Lost Client connection");
                    parent.append("> Server lost Client connection.");
                } else
                    parent.append("> Server closed Client connection.");
                break;
            }
            if (rec != null) {
            	parent.append("Empfangen: "+rec);
            	if(rec.length()>0){
            		th.handle(rec);
            	}
            } else {
                setDesonnected(true);
                parent.append("> Client closed connection.");
                break;
            }
        } //end of while
    } //end of startServer
    
    private String readInputStream(BufferedReader _in)
    throws IOException {
    	char[] buff = new char[tele_length*2];
    	int read = _in.read(buff, 0, tele_length*2);
    	String data = "";
    	
    	if(read != -1){
	    	for(int i =0; i< buff.length; i++)
	    	{
	    		data = data + buff[i];
	    	}
    	}
    	else{
    		return null;
    	}
    	
    	System.out.println("data: "+data);
    	
    	if(read > tele_length){
    		data = data.substring(0, tele_length);
    	}
    	
    	//System.out.println("data: "+data);
        System.out.println("Len got : "+read);
        return data;
    }
    
    public TelegrammHandler getTh(){
    	return th;
    }
}
