
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jcraft.jsch.*;






public class TwitterClient {
	private static PropertiesLoader prop = new PropertiesLoader();
	private static String SFTPHOST = "home59235323.1and1-data.host";
	private static int SFTPPORT = 22;
	private static String SFTPUSER = "p59235323";
	private static String SFTPPASS = "az&ka856fdA";
	private static String SFTPWORKINGDIR = "Com2u/Infoscreen";
	private static String hashtag = "Trump";
	private static String setOAuthConsumerKey = "0vPHibZTQ0jZOM9prKWxjR4CJ";
	private static String setOAuthConsumerSecret = "LGIgnzRnKEIyRta0rAy5V953SzPFL3lKXtDa5GMBliwb986X1F";
	private static String setOAuthAccessToken = "2879614206-32FAS3aOHL2d3qlR9lV0Nsnsta01noo2DjeRnEj";
	private static String setOAuthAccessTokenSecret = "aexOSRACEeM650GMG0vswlkqqePWJtP1O7vySO20YCW7C";
	
    
    public static void main(String[] args) throws Exception {
    	
    	hashtag = prop.get("TwitterHashTag");
    	
    	SFTPHOST = prop.get("SFTPHOST");
    	SFTPPORT = new Integer(prop.get("SFTPPORT"));
    	SFTPUSER = prop.get("SFTPUSER");
    	SFTPPASS = prop.get("SFTPPASS");
    	SFTPWORKINGDIR = prop.get("SFTPWORKINGDIR");
    	setOAuthConsumerKey = prop.get("TwitterOAuthConsumerKey");
    	setOAuthConsumerSecret = prop.get("TwitterOAuthConsumerSecret");
    	setOAuthAccessToken = prop.get("TwitterOAuthAccessToken");
    	setOAuthAccessTokenSecret = prop.get("TwitterOAuthAccessTokenSecret");

        ConfigurationBuilder cb = new ConfigurationBuilder();
        System.out.println("Start "+hashtag);
        SFTP ftp = new SFTP();
        ftp.init(SFTPUSER, SFTPPASS, SFTPHOST, SFTPPORT);
        ftp.connect();
        
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(setOAuthConsumerKey)
                .setOAuthConsumerSecret(setOAuthConsumerSecret)
                .setOAuthAccessToken(setOAuthAccessToken)
                .setOAuthAccessTokenSecret(setOAuthAccessTokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        Query query = new Query(hashtag);
        query.count(100);
        QueryResult result = twitter.search(query);
        //Pattern pattern = Pattern.compile("http://t.co/\\w{10}");
        //Pattern imagePattern = Pattern.compile("https\\:\\/\\/pbs\\.twimg\\.com/media/\\w+\\.(png | jpg | gif)(:large)?");
        int count = 0;
        for (Status status : result.getTweets()) {
        	System.out.println(status.getText());
            /*if (status.isRetweet())
                continue;
                
            System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
            Matcher matcher = pattern.matcher(status.getText());

                if (matcher.find()) {
                    System.out.println("found a t.co url");
                    URL oracle = new URL(matcher.group());
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(oracle.openStream()));
                    
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        matcher = imagePattern.matcher(inputLine);
                        System.out.println("img:"+inputLine);
                        if (matcher.find())
                            System.out.println("YAYAAYAYAYYAYAYAYAYAYAYAYAYAAYAYYAYAAYYAYAYAYA: " + matcher.group());
                    }

                    in.close();

            }*/
        	MediaEntity[] media = status.getMediaEntities(); //get the media entities from the status
        	for(MediaEntity m : media){ //search trough your entities
        	    System.out.println(m.getMediaURL()); //get your url!
        	}
        	
        	for (MediaEntity m : media) {
                try {
                	String fileName = "";
                    URL url = new URL(m.getMediaURL());
                    InputStream in = new BufferedInputStream(url.openStream());
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    int n = 0;
                    while (-1 != (n = in.read(buf))) {
                        out.write(buf, 0, n);
                    }
                    out.close();
                    in.close();
                    byte[] response = out.toByteArray();
                    File file  = new File("");;
                    //FileOutputStream fos = new FileOutputStream(file.getAbsolutePath() + "\\media\\" + m.getId() + "." + getExtension(m.getType()));
                    fileName = file.getAbsolutePath() + "\\media\\background" + count + "." + getExtension(m.getType());
                    FileOutputStream fos = new FileOutputStream(fileName);
					System.out.println( m.getId() + "." + getExtension(m.getType()));
                    fos.write(response);
                    fos.close();
                    ftp.putFile(fileName, "/Com2u/Infoscreen/");
                    count++;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
            }
        	

        }
        ftp.disconnect();
        System.out.println("End.");
        send("D:\\Data\\Projects\\Netstat\\media\\background0.jpg");
    }
    
    private static String getExtension(String type) {
        if (type.equals("photo")) {
            return "jpg";
        } else if (type.equals("video")) {
            return "mp4";
        } else if (type.equals("animated_gif")) {
            return "gif";
        } else {
            return "err";
        }
    }
    
    public static  void send (String fileName) {


        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        System.out.println("preparing the host information for sftp.");
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setPassword(SFTPPASS);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            System.out.println("Host connected.");
            channel = session.openChannel("sftp");
            channel.connect();
            System.out.println("sftp channel opened and connected.");
            channelSftp = (ChannelSftp) channel;
            channelSftp.cd(SFTPWORKINGDIR);
            File f = new File(fileName);
            channelSftp.put(new FileInputStream(f), f.getName());
            System.out.println("sftp file transfered");
        } catch (Exception ex) {
             System.out.println("Exception found while tranfer the response.");
        }
        finally{

            channelSftp.exit();
            System.out.println("sftp Channel exited.");
            channel.disconnect();
            System.out.println("Channel disconnected.");
            session.disconnect();
            System.out.println("Host Session disconnected.");
        }
    }   
}
