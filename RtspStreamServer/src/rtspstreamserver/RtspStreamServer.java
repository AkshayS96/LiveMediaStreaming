/*
this will be the server to stream the rtsp streams 
*/
package rtspstreamserver;

import java.awt.BorderLayout;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_stats_t;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

/**
 *
 * @author Akshay Solanki
 */
public class RtspStreamServer {

  public final JFrame frame;
  public final MediaPlayerFactory mediaPlayerFactory;
  public  EmbeddedMediaPlayer mediaPlayer;
  public JButton pauseButton;
  
  //mediaplayer stats variable 
  private final JLabel readBytesLabel;
  private final JLabel inputBitrateLabel;
  private final JLabel demuxReadBytesLabel;
  private final JLabel demuxBitrateLabel; 
    private final JLabel demuxCorruptedLabel; 
    private final JLabel demuxDiscontinuityLabel; 
    private final JLabel decodedVideoLabel; 
    private final JLabel decodedAudioLabel; 
    private final JLabel displayedPicturesLabel; 
    private final JLabel lostPicturesLabel; 
    private final JLabel playedABuffersLabel; 
    private final JLabel lostABuffersLabel; 
    private final JLabel sentPacketsLabel; 
    private final JLabel sentBytesLabel; 
    private final JLabel sendBitRateLabel; 
 
    private final JLabel readBytesValueLabel; 
    private final JLabel inputBitrateValueLabel; 
    private final JLabel demuxReadBytesValueLabel; 
    private final JLabel demuxBitrateValueLabel; 
    private final JLabel demuxCorruptedValueLabel; 
    private final JLabel demuxDiscontinuityValueLabel; 
    private final JLabel decodedVideoValueLabel; 
    private final JLabel decodedAudioValueLabel; 
    private final JLabel displayedPicturesValueLabel; 
    private final JLabel lostPicturesValueLabel; 
    private final JLabel playedABuffersValueLabel; 
    private final JLabel lostABuffersValueLabel; 
    private final JLabel sentPacketsValueLabel; 
    private final JLabel sentBytesValueLabel; 
    private final JLabel sendBitRateValueLabel; 
 
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    
    //if native discovery doesn't work then 
    //use the explicit search path
    //private static final String NATIVE_LIBRARY_SEARCH_PATH="path to the libvlc.dll";
    public static void main(String[] args)throws Exception {
       
       new NativeDiscovery().discover();
       //use in the case of explicit search path
       //NativeLibrary.addSearchPath(RuntimeUtils.getLibVlcLibraryName(,NATIVE_LIBRARY_SEARCH_PATH);
      SwingUtilities.invokeLater(new Runnable(){
          @Override 
          public void run(){
              new RtspStreamServer();
          }
      });
    }
    public RtspStreamServer(){
        
      ThreadedServer server = new ThreadedServer(8888,this);
      //now calling the class the implement the ThreadedServer with the port no 8888
      new Thread(server).start();
     // try{
       //   Thread.sleep(20*1000);
          
      //}catch(Exception e){
      //    e.printStackTrace();
      //}
      //System.out.println("Stopping Server");
      //server.stop();
        frame = new JFrame("Media Live Streaming server");
        frame.setBounds(100,100,800,600);
        // ----------------------------------------------//
        //----------stats for the mediaplayer --------------------//
        readBytesLabel = new JLabel("Read Bytes:"); 
        inputBitrateLabel = new JLabel("Input Bitrate:"); 
        demuxReadBytesLabel = new JLabel("Deumx Read Bytes:"); 
        demuxBitrateLabel = new JLabel("Demux Bitrate:"); 
        demuxCorruptedLabel = new JLabel("Demux Corrupted:"); 
        demuxDiscontinuityLabel = new JLabel("Demux Discontinuity:"); 
        decodedVideoLabel = new JLabel("Decoded Video:"); 
        decodedAudioLabel = new JLabel("Decoded Audio:"); 
        displayedPicturesLabel = new JLabel("Displayed Pictures:"); 
        lostPicturesLabel = new JLabel("Lost Pictures:"); 
        playedABuffersLabel = new JLabel("Played ABuffers:"); 
        lostABuffersLabel = new JLabel("Lost ABuffers"); 
        sentPacketsLabel = new JLabel("Sent Packets"); 
        sentBytesLabel = new JLabel("Sent Bytes"); 
        sendBitRateLabel = new JLabel("Send Bitrate"); 
 
        readBytesValueLabel = new JLabel(); 
        inputBitrateValueLabel = new JLabel(); 
        demuxReadBytesValueLabel = new JLabel(); 
        demuxBitrateValueLabel = new JLabel(); 
        demuxCorruptedValueLabel = new JLabel(); 
        demuxDiscontinuityValueLabel = new JLabel(); 
        decodedVideoValueLabel = new JLabel(); 
        decodedAudioValueLabel = new JLabel(); 
        displayedPicturesValueLabel = new JLabel(); 
        lostPicturesValueLabel = new JLabel(); 
        playedABuffersValueLabel = new JLabel(); 
        lostABuffersValueLabel = new JLabel(); 
        sentPacketsValueLabel = new JLabel(); 
        sentBytesValueLabel = new JLabel(); 
        sendBitRateValueLabel = new JLabel(); 
 
        JPanel statsComponent = new JPanel(); 
        statsComponent.setLayout(new GridLayout(0, 2, 8, 8)); 
 
        statsComponent.add(readBytesLabel); 
        statsComponent.add(readBytesValueLabel); 
        statsComponent.add(inputBitrateLabel); 
        statsComponent.add(inputBitrateValueLabel); 
        statsComponent.add(demuxReadBytesLabel); 
        statsComponent.add(demuxReadBytesValueLabel); 
        statsComponent.add(demuxBitrateLabel); 
        statsComponent.add(demuxBitrateValueLabel); 
        statsComponent.add(demuxCorruptedLabel); 
        statsComponent.add(demuxCorruptedValueLabel); 
        statsComponent.add(demuxDiscontinuityLabel); 
        statsComponent.add(demuxDiscontinuityValueLabel); 
        statsComponent.add(decodedVideoLabel); 
        statsComponent.add(decodedVideoValueLabel); 
        statsComponent.add(decodedAudioLabel); 
        statsComponent.add(decodedAudioValueLabel); 
        statsComponent.add(displayedPicturesLabel); 
        statsComponent.add(displayedPicturesValueLabel); 
        statsComponent.add(lostPicturesLabel); 
        statsComponent.add(lostPicturesValueLabel); 
        statsComponent.add(playedABuffersLabel); 
        statsComponent.add(playedABuffersValueLabel); 
        statsComponent.add(lostABuffersLabel); 
        statsComponent.add(lostABuffersValueLabel); 
        statsComponent.add(sentPacketsLabel); 
        statsComponent.add(sentPacketsValueLabel); 
        statsComponent.add(sentBytesLabel); 
        statsComponent.add(sentBytesValueLabel); 
        statsComponent.add(sendBitRateLabel); 
        statsComponent.add(sendBitRateValueLabel); 
 
        JPanel statsPanel = new JPanel(); 
        statsPanel.setLayout(new BorderLayout()); 
        statsPanel.add(statsComponent, BorderLayout.NORTH); 
 
        statsPanel.setBorder(new EmptyBorder(8, 8, 8, 8)); 
 
        //----------------------------------------------------------//
        mediaPlayerFactory= new MediaPlayerFactory();
        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
        Canvas canvas = new Canvas();
        canvas.setBackground(Color.black);
        CanvasVideoSurface videoSurface= mediaPlayerFactory.newVideoSurface(canvas);
        mediaPlayer.setVideoSurface(videoSurface);
        JPanel contentPane=new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(canvas,BorderLayout.CENTER);
        JPanel controlsPane=new JPanel();
        pauseButton = new JButton("pause");
        controlsPane.add(pauseButton);
        JPanel chatPanel= new JPanel();
        contentPane.add(chatPanel,BorderLayout.EAST);
        contentPane.add(controlsPane,BorderLayout.SOUTH);
        contentPane.add(statsPanel,BorderLayout.WEST);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                mediaPlayer.release();
                System.exit(0);
            
        }});
        frame.setVisible(true);
        //different action listener to the buttons 
        pauseButton.addActionListener(new ActionListener(){
            @Override 
            public void actionPerformed(ActionEvent e){
                if(pauseButton.getText()=="pause"){
                mediaPlayer.pause();
                pauseButton.setText("Play");}
                else
                {
                    mediaPlayer.play();
                    pauseButton.setText("pause");
                }
            }
        });
        mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter(){
        @Override 
        public void playing(MediaPlayer mediaPlayer){
            SwingUtilities.invokeLater(new Runnable(){
              @Override 
              public void run(){
               frame.setTitle(String.format("Streaming media File -%s",mediaPlayer.getMediaMeta().getTitle()));
              }
            });
        }
        @Override
        public void finished(MediaPlayer mediaPlayer){
            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run(){
                    System.out.println("stream another media");
                }
            });
        }
        @Override 
        public void error(MediaPlayer mediaPlayer){
            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run(){
                    JOptionPane.showMessageDialog(frame,"Failes to Stream Media","Error",JOptionPane.ERROR_MESSAGE);
                }
            });
            
        }
        
        });
        String options = formatRtpStream("230.0.0.1", 5555);
        frame.setContentPane(contentPane);
        mediaPlayer.playMedia("f:\\bahubali.mp4",
            options,
            ":no-sout-rtp-sap",
            ":no-sout-standard-sap",
            ":sout-all",
            ":sout-keep"
        );
        
        //executor service function submit 
        executorService.submit(new Runnable(){
           @Override 
           public void run(){
               while(true){
                   if(mediaPlayer.isPlaying()){
                       updateStats(mediaPlayer.getMediaStatistics());                   }
               
               try{
                   Thread.sleep(100);
               }catch(InterruptedException e){}
           }}
        });
        
    }
    private static String formatRtpStream(String serverAddress, int serverPort) {
        StringBuilder sb = new StringBuilder(60);
        sb.append(":sout=#duplicate{dst=display,dst=rtp{dst=");
        sb.append(serverAddress);
        sb.append(",port=");
        sb.append(serverPort);
        sb.append(",mux=ts}}");
        return sb.toString();
    }
    //updateStats function 
    public void updateStats(libvlc_media_stats_t stats){
        readBytesValueLabel.setText(String.valueOf(stats.i_read_bytes)); 
        inputBitrateValueLabel.setText(String.valueOf(stats.f_input_bitrate)); 
        demuxReadBytesValueLabel.setText(String.valueOf(stats.i_demux_read_bytes)); 
        demuxBitrateValueLabel.setText(String.valueOf(stats.f_demux_bitrate)); 
        demuxCorruptedValueLabel.setText(String.valueOf(stats.i_demux_corrupted)); 
        demuxDiscontinuityValueLabel.setText(String.valueOf(stats.i_demux_discontinuity)); 
        decodedVideoValueLabel.setText(String.valueOf(stats.i_decoded_video)); 
        decodedAudioValueLabel.setText(String.valueOf(stats.i_decoded_audio)); 
        displayedPicturesValueLabel.setText(String.valueOf(stats.i_displayed_pictures)); 
        lostPicturesValueLabel.setText(String.valueOf(stats.i_lost_pictures)); 
        playedABuffersValueLabel.setText(String.valueOf(stats.i_played_abuffers)); 
        lostABuffersValueLabel.setText(String.valueOf(stats.i_lost_abuffers)); 
        sentPacketsValueLabel.setText(String.valueOf(stats.i_sent_packets)); 
        sentBytesValueLabel.setText(String.valueOf(stats.i_sent_bytes)); 
        sendBitRateValueLabel.setText(String.valueOf(stats.f_send_bitrate)); 
    }
}
    //ths class will recieve the command on the sockets and send the response to the client
    class ThreadedServer implements Runnable{
        private int serverPort;
        private ServerSocket serverSocket;
        private boolean isStopped = false;
        private Thread runningThread = null;
        RtspStreamServer rtspStreamServer1;
        public ThreadedServer(int port,RtspStreamServer rtspStreamServer ){
            this.serverPort  =port;
            rtspStreamServer1=rtspStreamServer;
        }
        public void run(){
            synchronized(this){
                this.runningThread=Thread.currentThread();
            }
            openServerSocket();
            
            while(!isStopped()){
                Socket clientSocket = null;
                try{
                    clientSocket= this.serverSocket.accept();
                }catch(Exception e){
                    if(isStopped()){
                System.out.println("Server Stopped");
                    return ;
                }
                throw new RuntimeException("Error accepting client connection",e);
            }
                new Thread(new WorkerRunnable(clientSocket,"multithreaded Server",rtspStreamServer1)).start();
        
            
            }
        }
        private synchronized boolean isStopped(){
            return this.isStopped;
        }
        public synchronized void stop(){
            this.isStopped=true;
            try{
                this.serverSocket.close();
            }catch(Exception e){
                throw new RuntimeException("Error closing server",e);
            }
        }
        public void openServerSocket(){
            try{
                this.serverSocket= new ServerSocket(this.serverPort);
            }catch(Exception e){
                throw new RuntimeException("Cannot open port 8888",e);
            }
        }
    }
//class in the multiThreadedServer environment to listen to the client requests
class WorkerRunnable implements Runnable{
    protected Socket clientSocket = null;
    protected String serverText = null;
    RtspStreamServer rtspStreamServer1;
    public WorkerRunnable(Socket clientSocket, String serverText,RtspStreamServer rtspStreamServer){
        this.clientSocket = clientSocket;
        this.serverText= serverText;
        rtspStreamServer1= rtspStreamServer;
    }
    public void run(){
        //RtspStreamServer rtspStreamServer= new RtspStreamServer();
        try{
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out= new DataOutputStream(clientSocket.getOutputStream());
            long time = System.currentTimeMillis();
            String str="";
            InetAddress ip=clientSocket.getInetAddress();
            str=in.readUTF();
                System.out.println(str);
                
                out.flush();
                while(!str.equals("stop")){
                str=in.readUTF();
               System.out.println("Command From:"+clientSocket.getLocalAddress()+clientSocket.getLocalPort()+"\t"+str);
                if(str.equals("play"))
                {//for playing the paused media stream
                   rtspStreamServer1.mediaPlayer.play();
                   out.writeUTF("Request Status: 200 Ok \nStream is playing");
                   out.flush();
                }else if(str.equals("pause"))
                {//for pause the stream media
              rtspStreamServer1.mediaPlayer.pause();
              out.writeUTF("Request Status: 200 Ok \nStream is paused");
                   out.flush();
                }
                else if(str.equals("stop")){
                    //for stoping the rtsp streaming 
                    rtspStreamServer1.mediaPlayer.stop();
                    out.close();
                    in.close();
                    
                }
                else if(str.equals("connect")){
                    //for connecting to the server to start streming
                }
                else
                {
                }
                
               
                
            }
            
            out.close();
            in.close();
            System.out.println("request processed");
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

