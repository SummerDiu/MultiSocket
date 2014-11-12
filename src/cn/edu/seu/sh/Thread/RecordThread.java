package cn.edu.seu.sh.Thread;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import cn.edu.seu.sh.Config.CommonConfig;

public class RecordThread extends Thread {
	private boolean keepRunning = true;
    private AudioRecord audioRecord = null;
    private int minSize = 0;
    private MulticastSocket socket = null;
    int n = 0;

    @Override
    public void run() {
        initSocket();
        initAudio();
        while(keepRunning){
            recodeAndSend();
        }
        release();
    }

    private void initSocket(){
        try {
            InetAddress destAddress = InetAddress.getByName(CommonConfig.multicastHost);
            if(!destAddress.isMulticastAddress()){
            	throw new Exception("地址不是多播地址");
            }
            int destPort = CommonConfig.localPort;
            int TTL = CommonConfig.TTLTime;
            socket = new MulticastSocket();
            socket.setTimeToLive(TTL);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void initAudio(){
        minSize = AudioTrack.getMinBufferSize(CommonConfig.freq, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,CommonConfig.freq, AudioFormat.CHANNEL_IN_STEREO,AudioFormat.ENCODING_PCM_16BIT, minSize);
        audioRecord.startRecording();
    }

    private void recodeAndSend(){
        byte [] buffer = new byte[1024];
        int read = audioRecord.read(buffer,0,buffer.length);

        DatagramPacket packet = new DatagramPacket(buffer, read);
        try{
            InetAddress ip = InetAddress.getByName(CommonConfig.multicastHost.trim());
            int port = CommonConfig.localPort;
            
//            InetAddress ip = InetAddress.getByName(CommonConfig.CLIENT_A_IP_ADDRESS.trim());//;;
//            int port = CommonConfig.CLIENT_A_PORT;
            
            packet.setAddress(ip);
            packet.setPort(port);//; 
            socket.send(packet);
            while(n++ <100)
            System.out.println("ClientSendPacket:--->to"+packet.getAddress()+"length:"+packet.getLength()+
            			"content:"+Arrays.toString(packet.getData()));
            
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stopThread(){
        keepRunning = false;
    }

    public void release(){
        if(socket!=null){
            socket.close();
            socket = null;
        }
        if(audioRecord!=null){
            audioRecord.release();
            audioRecord = null;
        }
    }

}
