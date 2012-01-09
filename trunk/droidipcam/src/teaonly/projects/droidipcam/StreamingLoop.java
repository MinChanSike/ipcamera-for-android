package teaonly.projects.droidipcam;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.SystemClock;
import android.util.Log;


public class StreamingLoop
{
	//Local data loopback
	private LocalSocket receiver,sender;			
	private LocalServerSocket lss;		
	private String localAddress;

	public StreamingLoop (String addr)	
	{
		localAddress = addr;
	}
	
	public FileDescriptor getEncoderFileDescriptor()
	{
		return sender.getFileDescriptor();
	}
    
    public FileDescriptor getNativeFileDescriptor()
    {
        return receiver.getFileDescriptor();
    }
        
	public void ReleaseLoop()
	{
		try {
			if ( lss != null){
				lss.close();
			}
			if ( receiver != null){
				receiver.close();
			}
			if ( sender != null){
				sender.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			Log.d("MVRS", e1.toString());			
		}
		
		lss = null;
		sender = null;
		receiver = null;
	}

	public boolean InitLoop()
	{		
        receiver = new LocalSocket();
		try {
			lss = new LocalServerSocket(localAddress);
			receiver.connect(new LocalSocketAddress(localAddress));
			receiver.setReceiveBufferSize(1000);
			receiver.setSendBufferSize(1000);
			sender = lss.accept();
			sender.setReceiveBufferSize(1000);
			sender.setSendBufferSize(1000);
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		

		return true;
	}

}