/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rtspclientplayer;


 
import java.awt.BorderLayout; 
import java.awt.GridLayout; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService; 
import java.util.concurrent.Executors; 
import javax.swing.JButton;
 
import javax.swing.JFrame; 
import javax.swing.JLabel; 
import javax.swing.JPanel; 
import javax.swing.JTextField;
import javax.swing.SwingUtilities; 
import javax.swing.border.EmptyBorder; 
 
import uk.co.caprica.vlcj.binding.internal.libvlc_media_stats_t; 
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent; 
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.test.VlcjTest; 
 
/**
 * Test to demonstrate media statistics. 
 */ 
public class RtspClientPlayer extends VlcjTest { 
 
    private final JFrame frame; 
    public EmbeddedMediaPlayerComponent mediaPlayerComponent; 
    //server address and port to connect to it 
    String serverAddress="127.0.0.1";
    int port=8888;
    public  String state="pause";
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
 
    //buttons for sending the pause request and play request to the server 
    public JButton playButton;
    public JTextField hostname;
    public JButton connect;
    public JButton stop;
    
    public static void main(String[] args) { 
        //if(args.length != 1) { 
         //   System.err.println("Specify an mrl"); 
           // System.exit(1); 
        //} 
        new NativeDiscovery().discover();
 
        setLookAndFeel(); 
        
        
        //final String mrl = args[0]; 
 
        SwingUtilities.invokeLater(new Runnable() { 
            @Override 
            public void run() { 
                 RtspClientPlayer rtspClientPlayer=new RtspClientPlayer();
        CommandClass commandClass= new CommandClass(rtspClientPlayer);
        commandClass.setDaemon(true);
        commandClass.start();
       
            } 
        }); 
    } 
 
    public RtspClientPlayer() { 
        
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent(); 
 
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
 
        JPanel cp = new JPanel(); 
        cp.setLayout(new BorderLayout()); 
        cp.add(mediaPlayerComponent, BorderLayout.CENTER); 
        cp.add(statsPanel, BorderLayout.WEST); 
        
        JPanel controlPane = new JPanel();
        playButton = new JButton("Pause");
        controlPane.add(playButton);
        connect = new JButton("connect to Stream");
        stop= new JButton("stop");
        hostname = new JTextField("Enter the MRL to playt the stream");
        controlPane.add(hostname);
        controlPane.add(connect);
        controlPane.add(stop);
        //adding the function to the buttons 
        //now here i am adding the sockets connection to play and stop the network stream
        //here you can specify the server to which you can ccnnect through sockers
        
       
       /* playButton.addActionListener(new ActionListener(){
            @Override 
            public void actionPerformed(ActionEvent e){
                state="play";
            }
        });
        connect.addActionListener(new ActionListener(){
            @Override 
            public void actionPerformed(ActionEvent e){
                state="pause";
            }
        });
        stop.addActionListener(new ActionListener(){
            @Override 
            public void actionPerformed(ActionEvent e){
                
            }
        });
        */
        
        
        
        cp.add(controlPane,BorderLayout.SOUTH);
        
 
        frame = new JFrame("vlcj Media Statistics"); 
        frame.setLocation(100, 100); 
        frame.setSize(1000, 400); 
        frame.setContentPane(cp); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.setVisible(true); 
 
        executorService.submit(new Runnable() { 
            @Override 
            public void run() { 
                for(;;) { 
                    if(mediaPlayerComponent.getMediaPlayer().isPlaying()) { 
                        updateStats(mediaPlayerComponent.getMediaPlayer().getMediaStatistics()); 
                    } 
                    try { 
                        // Update statistics every second, choose more/less if you want 
                        Thread.sleep(100); 
                    } 
                    catch(InterruptedException e) { 
                    } 
                } 
            } 
        }); 
       
    
        
    }
 
   
 
    private void updateStats(libvlc_media_stats_t stats) { 
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
class CommandClass extends Thread{
    private String serverAddress;
    private int port;
    RtspClientPlayer rtspClientPlayer;
public CommandClass(RtspClientPlayer rtspClientPlayer){
    this.serverAddress= rtspClientPlayer.serverAddress;
    this.rtspClientPlayer=rtspClientPlayer;
    this.port=rtspClientPlayer.port;   
}
public void run(){
    try{
    Socket client= new Socket(serverAddress,port);
    DataInputStream din= new DataInputStream(client.getInputStream());
    DataOutputStream dout= new DataOutputStream(client.getOutputStream());
    String str="";
    dout.writeUTF("pause");
                    dout.flush();
                    
        rtspClientPlayer.playButton.addActionListener(new ActionListener(){
            @Override 
            public void actionPerformed(ActionEvent e){
                if(rtspClientPlayer.playButton.getText().equals("Play")){
                    rtspClientPlayer.playButton.setText("Pause");
                    try{
                    dout.writeUTF("play");
                    dout.flush();
                    
                    String str = din.readUTF();
                    System.out.println(str);
                    }catch(Exception ee){ee.printStackTrace();}
                }
                else
                {  rtspClientPlayer.playButton.setText("Play");
                try{
                    dout.writeUTF("pause");
                    dout.flush();
                    String str = din.readUTF();
                    System.out.println(str);
                }catch(Exception ee){ee.printStackTrace();}
                }
            }
        });
        rtspClientPlayer.stop.addActionListener(new ActionListener(){
        @Override 
        public void actionPerformed(ActionEvent e){
            try{
                rtspClientPlayer.mediaPlayerComponent.getMediaPlayer().stop();
                dout.writeUTF("stop");
                dout.flush();
             dout.close();             
    din.close();
    client.close();
            }catch(Exception ee){ee.printStackTrace();} 
        }
    });
        rtspClientPlayer.connect.addActionListener(new ActionListener(){
           @Override 
           public void actionPerformed(ActionEvent e){
               try{
                   String hostname = rtspClientPlayer.hostname.getText();
                   rtspClientPlayer.mediaPlayerComponent.getMediaPlayer().playMedia(hostname);
                   
                  
               }catch(Exception ee){ee.printStackTrace();}
           }
        });
   
                }catch(Exception e){
                    e.printStackTrace();
                    
                }
}


}