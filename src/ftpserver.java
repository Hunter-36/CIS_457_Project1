import java.io.*; 
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import javax.swing.*;

    public class ftpserver extends Thread {
        private Socket connectionSocket;
        int port;
        int count = 1;

        public ftpserver(Socket connectionSocket) {
            this.connectionSocket = connectionSocket;
        }


        public void run() {
            if (count == 1)
                System.out.println("User connected" + connectionSocket.getInetAddress());
            count++;

            try {
                processRequest();

            } catch (Exception e) {
                System.out.println(e);
            }

        }


        private void processRequest() throws Exception {
            String fromClient;
            String clientCommand;
            byte[] data;
            String frstln;

            while (true) {
                if (count == 1)
                    System.out.println("User connected" + connectionSocket.getInetAddress());
                count++;

                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                fromClient = inFromClient.readLine();

                //System.out.println(fromClient);
                StringTokenizer tokens = new StringTokenizer(fromClient);

                frstln = tokens.nextToken();
                port = Integer.parseInt(frstln);
                clientCommand = tokens.nextToken();
                //System.out.println(clientCommand);


                if (clientCommand.equals("list:")) {
                    String curDir = System.getProperty("user.dir");
                    
                    Socket dataSocket = new Socket(connectionSocket.getInetAddress(), port);
                    DataOutputStream dataOutToClient =
                            new DataOutputStream(dataSocket.getOutputStream());
                    File dir = new File(curDir);
                    String[] children = dir.list();
                    if (children == null) {
                        // Either dir does not exist or is not a directory
                    } else {
                        for (int i = 0; i < children.length; i++) {
                            String filename = children[i];
                            if (filename.endsWith(".txt")) {
                                dataOutToClient.writeUTF(filename);
                            }
                        }
                        // Send "eof" after sending all files
                        dataOutToClient.writeUTF("eof");
                    }
                    dataSocket.close();
                }//else
                if(clientCommand.equals("stor:")) {
                    String filename = tokens.nextToken();
                    Socket dataSocket = new Socket(connectionSocket.getInetAddress(), port);
                    DataInputStream dataInFromClient = new DataInputStream(dataSocket.getInputStream());
                    FileOutputStream fileOut = new FileOutputStream(filename);
                    int content;
                    data = new byte[1024];
                    while ((content = dataInFromClient.read(data)) != -1) {
                        fileOut.write(data, 0, content);
                    }
                    System.out.println("received from client: " + filename);
                    fileOut.close();
                    dataSocket.close();
                }


               //get function (RETR Command)
                if (clientCommand.equals("get:")) {
                    String filesend = tokens.nextToken();
                    Socket dataSocket = new Socket(connectionSocket.getInetAddress(), port);
                    DataOutputStream dataOutToClient = new DataOutputStream(dataSocket.getOutputStream());
                    FileInputStream fileSent = new FileInputStream(filesend);
                    int content = 0;
                    data = new byte[1024];
                    while ((content = fileSent.read(data)) != -1) {
                        dataOutToClient.write(data, 0, content);
                    }
                    System.out.println("sent to client: " + filesend);
                    fileSent.close();
                    dataSocket.close();
                } 
            }   
            
        }
            public static void main(String[] args) {
                try {
                    ServerSocket welcomeSocket = new ServerSocket(12000); 

                    while (true) {
                        Socket connectionSocket = welcomeSocket.accept();
                        ftpserver server = new ftpserver(connectionSocket);
                        server.start();
                    }
                } catch (IOException e) {
                    System.out.println("server error");
                }
            }
        }


