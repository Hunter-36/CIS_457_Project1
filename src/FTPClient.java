import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import javax.swing.*;

class FTPClient {

	public static void main(String argv[]) throws Exception { 
        String sentence; 
        String modifiedSentence; 
        boolean isOpen = true;
        int number=1;
        boolean notEnd = true;
		int port1=1221;
		int port = 1200;
		String statusCode;
		boolean clientgo = true;
	    
		System.out.println("Welcome to the simple FTP App   \n     Commands  \nconnect servername port# connects to a specified server \nlist: lists files on server \nget: fileName.txt downloads that text file to your current directory \nstor: fileName.txt Stores the file on the server \nclose terminates the connection to the server");
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in)); 
        sentence = inFromUser.readLine();
        StringTokenizer tokens = new StringTokenizer(sentence);


		if(sentence.startsWith("connect")) {
			String serverName = tokens.nextToken(); 
			serverName = tokens.nextToken();
			port1 = Integer.parseInt(tokens.nextToken());
        	System.out.println("You are connected to " + serverName);
        	Socket ControlSocket= new Socket(serverName, port1);

        	while(isOpen && clientgo) {      
	  			DataOutputStream outToServer = new DataOutputStream(ControlSocket.getOutputStream()); 
          		DataInputStream inFromServer = new DataInputStream(new BufferedInputStream(ControlSocket.getInputStream()));
          		sentence = inFromUser.readLine();
          
        		if(sentence.equals("list:")) {   
	    			port = port +2;
	    			System.out.println(port);
	    			ServerSocket welcomeData = new ServerSocket(port);

	    			System.out.println("\n \n \nThe files on this server are:");
	    			outToServer.writeBytes (port + " " + sentence + " " + '\n');

	    			Socket dataSocket =welcomeData.accept(); 
 	   				DataInputStream inData = new DataInputStream(new BufferedInputStream(dataSocket.getInputStream()));
            		while(notEnd) {
                		modifiedSentence = inData.readUTF();
                		if(modifiedSentence.equals("eof")) {
                    		break; 
						}
               			System.out.println("	" + modifiedSentence); 
            		}

	 				welcomeData.close();
	 				dataSocket.close();
	 				System.out.println("\nWhat would you like to do next: \nget: file.txt ||  stor: file.txt  || close");
        		}	

    			else if (sentence.startsWith("get: ")) {
					port = port + 2;
					String fileName = sentence.substring(5).trim();
					System.out.println(fileName);
    				ServerSocket welcomeData = new ServerSocket(port);
    				outToServer.writeBytes(port + " " + sentence + " " + '\n'); // Send the command to the server
    				Socket dataSocket = welcomeData.accept();
    				DataInputStream inData = new DataInputStream(new BufferedInputStream(dataSocket.getInputStream()));
					System.out.println("Data connection accepted");
					String response = new BufferedReader(new InputStreamReader(inFromServer)).readLine(); // Response from server: 200 or 550
					System.out.println("Response: " + response);
					if (response.startsWith("200")) {
						System.out.println("Downloading file...");
        				FileOutputStream fileOut = new FileOutputStream(fileName);
        				byte[] buffer = new byte[1024];
        				int bytesRead;
        				while ((bytesRead = inData.read(buffer)) != -1) {
        		    		fileOut.write(buffer, 0, bytesRead);
        				}
        				fileOut.close();

        				System.out.println("File '" + fileName + "' downloaded successfully.");
    				} 
					else if (response.startsWith("550")) {
        				System.out.println("Error: File '" + fileName + "' not found on the server.");
    				}
    				welcomeData.close();
    				dataSocket.close();

    				System.out.println("\nWhat would you like to do next: \nlist: || get: file.txt || stor: file.txt || close");
				}

				else if (sentence.startsWith("stor: ")) {
					port = port + 2;
					String fileToSend = sentence.substring(6).trim();
					System.out.println(fileToSend);
					ServerSocket welcomeData = new ServerSocket(port);
					outToServer.writeBytes(port + " " + sentence + " " + '\n'); 
					Socket dataSocket = welcomeData.accept();
					DataOutputStream outData = new DataOutputStream(dataSocket.getOutputStream());
					System.out.println("Data connection accepted");
					File file = new File(fileToSend);
					if (file.exists()) {
						System.out.println("Uploading file...");
						FileInputStream fileIn = new FileInputStream(file);
						byte[] buffer = new byte[1024];
						int bytesRead;
						while ((bytesRead = fileIn.read(buffer)) != -1) {
							outData.write(buffer, 0, bytesRead);
						}
						fileIn.close();
						System.out.println("File '" + fileToSend + "' uploaded successfully.");
					} else {
						System.out.println("Error: File '" + fileToSend + "' not found on the client.");
					}
					welcomeData.close();
					dataSocket.close();
					System.out.println("\nWhat would you like to do next: \nlist: || get: file.txt || stor: file.txt || close");
				}
				else if(sentence.equals("close")) {
					clientgo = false;
				}

				else{
	     			if(sentence.equals("close")) {
						clientgo = false;
	     			}
	     			System.out.print("No server exists with that name or server not listening on that port try agian");         
    			}
			}
		}
	}
}
