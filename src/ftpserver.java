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
                            // Get filename of file or directory
                            String filename = children[i];

                            if (filename.endsWith(".txt"))
                                dataOutToClient.writeUTF(children[i]);
                            //System.out.println(filename);
                            if (i - 1 == children.length - 2) {
                                dataOutToClient.writeUTF("eof");
                                // System.out.println("eof");
                            }//if(i-1)


                        }//for

                        dataSocket.close();
                        //System.out.println("Data Socket closed");
                    }//else


                }if(clientCommand.equals("stor:")) {
                    String file = tokens.nextToken();
                    Socket dataSocket = new Socket(connectionSocket.getInetAddress(), port);
                    DataInputStream dataIntake = new DataInputStream(dataSocket.getInputStream());
                    FileOutputStream fileSent = new FileOutputStream(file);
                    int content = 0;
                    data = new byte[1024];
                    System.out.println("debugging 1 file sent: " + file);
                    while ((content = dataIntake.read(data)) != -1) {
                        fileSent.write(data, 0, content);
                    }
                    System.out.println("debugging 2 file sent: " + file);
                    fileSent.close();
                    dataSocket.close();
                }
                if (clientCommand.equals("close: ")) {
                    connectionSocket.close();
                }




                    /*            ....................................
                if (clientCommand.equals("get:")) {
            /*            ....................................
			....................................
			....................................

*/

                }//main

            }
        }
    }
	

