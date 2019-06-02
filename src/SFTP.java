
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;

public class SFTP {
	   private static String BENUTZERNAME = "p254491";
	   private static String PASSWORT     = "A7jNhd7okd";
	   private static String HOST         = "home254491.1and1-data.host";
	   private static int    PORT         = 22;
	   private static ChannelSftp sftpChannel=null;
	   private static Session session=null;
	   private static Channel channel=null;
	   
	   public static void init(String _BENUTZERNAME, String _PASSWORT, String _HOST, int    _PORT){
		   BENUTZERNAME = _BENUTZERNAME;
		   PASSWORT = _PASSWORT;
		   HOST = _HOST;
		   PORT = _PORT;
		   
	   }
	   
	   public static void connect(){
		   try {
				JSch jsch = new JSch();

				//String knownHostsFilename = "/home/username/.ssh/known_hosts";
				//jsch.setKnownHosts( knownHostsFilename );
				java.util.Properties config = new java.util.Properties(); 
				config.put("StrictHostKeyChecking", "no");
				
				session =  jsch.getSession( BENUTZERNAME, HOST );    
				{
				  // "interactive" version
				  // can selectively update specified known_hosts file 
				  // need to implement UserInfo interface
				  // MyUserInfo is a swing implementation provided in 
				  //  examples/Sftp.java in the JSch dist
				  session.setPassword(PASSWORT);
				  session.setConfig(config);
				  // OR non-interactive version. Relies in host key being in known-hosts file
				 
				}

				session.connect();

				channel = session.openChannel( "sftp" );
				channel.connect();

				sftpChannel = (ChannelSftp) channel;
				System.out.println("SFTP connect");
				} catch (JSchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e){
				e.printStackTrace();
			}
		   
	   }
	   
	   public static void disconnect(){
			try {
		

				sftpChannel.exit();
				session.disconnect();
				System.out.println("SFTP disconnect");
				} catch (Exception e){
				e.printStackTrace();
			}
	   }
	   
	   public static void transfer(){
		   try {
				sftpChannel.get("Com2u/Infoscreen/background0.jpg", "background0.jpg" );
				// OR
				InputStream in = sftpChannel.get( "Com2u/Infoscreen/background0.jpg" );
				  // process inputstream as needed
				
				ChannelSftp channelSftp = (ChannelSftp) channel;
		        channelSftp.cd("Com2u/Infoscreen/");
		        File f = new File("media/background0.jpg");

		        channelSftp.put(new FileInputStream(f), f.getName());
				
				System.out.println("SFTP File received");
			} catch (SftpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e){
				e.printStackTrace();
			}
	   }
	   
	   public static void putFile(String sourceFile, String targetFolder){
		   try {
				
				ChannelSftp channelSftp = (ChannelSftp) channel;
		        channelSftp.cd(targetFolder);
		        File f = new File(sourceFile);

		        channelSftp.put(new FileInputStream(f), f.getName());
		        f.delete();
				System.out.println("SFTP File received");
			} catch (SftpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e){
				e.printStackTrace();
			}
	   }

	public static void main(String[] args)  {
		connect();
		transfer();
		disconnect();
		/*try {
			
		
			
			JSch jsch = new JSch();

			//String knownHostsFilename = "/home/username/.ssh/known_hosts";
			//jsch.setKnownHosts( knownHostsFilename );
			java.util.Properties config = new java.util.Properties(); 
			config.put("StrictHostKeyChecking", "no");
			
			Session session =  jsch.getSession( BENUTZERNAME, HOST );    
			{
			  // "interactive" version
			  // can selectively update specified known_hosts file 
			  // need to implement UserInfo interface
			  // MyUserInfo is a swing implementation provided in 
			  //  examples/Sftp.java in the JSch dist
			  session.setPassword(PASSWORT);
			  session.setConfig(config);
			  // OR non-interactive version. Relies in host key being in known-hosts file
			 
			}

			session.connect();

			Channel channel = session.openChannel( "sftp" );
			channel.connect();

			ChannelSftp sftpChannel = (ChannelSftp) channel;

			sftpChannel.get("Com2u/Infoscreen/background0.jpg", "background0.jpg" );
			// OR
			InputStream in = sftpChannel.get( "Com2u/Infoscreen/background0.jpg" );
			  // process inputstream as needed
			
			ChannelSftp channelSftp = (ChannelSftp) channel;
	        channelSftp.cd("Com2u/Infoscreen/");
	        File f = new File("media/background0.jpg");

	        channelSftp.put(new FileInputStream(f), f.getName());
			
			

			sftpChannel.exit();
			session.disconnect();
			System.out.println("File received");
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		*/

	}

	

}
