import java.util.ArrayList;
import java.util.List;

public class RecorderThread extends Thread implements Runnable {
    Processor p;
    Buffer inChannel;

    @Override
    public void run() {
        try {
            recordChannel(inChannel);
        }
        catch(InterruptedException e)
        {
            System.out.println("Received Duplicate Marker");
        }
    }

    public RecorderThread(Processor p)
    {
        this.p = p;
    }

    public RecorderThread(Processor p, Buffer inChannel)
    {
        this.p = p;
        this.inChannel = inChannel;
    }

    public void setInChannel(Buffer inChannel) {
        this.inChannel = inChannel;
    }

    /**
     * You should send a message to this recording so that recording is stopped
     * //TODO: Homework: Move this method recordChannel(..) out of this class. Start this method in a
     *                  separate thread. This thread will start when the marker arrives and it will stop
     *                  when a duplicate marker is received. This means that each processor will have the
     *                  capability to start and stop this channel recorder thread. The processor manages the
     *                  record Channel thread. Processor will have the ability to stop the thread.
     *                  Feel free to use any java concurrency  and thread classes to implement this method
     *
     *
     * @param channel The input channel which has to be monitered
     */

    public void recordChannel(Buffer channel) throws InterruptedException{
        //Here print the value stored in the inChannels to stdout or file
        //TODO:Homework: Channel will have messages from before a marker has arrived. Record messages only after a
        //               marker has arrived.
        //               [hint: Use the getTotalMessageCount () method to get the messages received so far.
        int lastIdx = channel.getTotalMessageCount();
        List<Message> recordedMessagesSinceMarker = new ArrayList<>();

        //TODO: Homework: Record messages
        // [Hint: Get the array that is storing the messages from the channel. Remember that the Buffer class
        // has a member     private List<Message> messages;  which stores all the messages received.
        // When a marker has arrived sample this List of messages and copy only those messages that
        // are received since the marker arrived. Copy these messages into recordedMessagesSinceMarker
        // and put it in the channelState map.
        //
        // ]
        while(true)
        {
            if(!this.isInterrupted())
            {
                if (lastIdx < channel.getTotalMessageCount()-1 && !channel.getMessage(lastIdx + 1).equals(null))
                {
                    System.out.println("Adding Message " + channel.getMessage(lastIdx+1).getMessageType().toString());
                    recordedMessagesSinceMarker.add(channel.getMessage(lastIdx++));
                    //Thread.sleep(1000);
                }
                p.channelState.put(channel, recordedMessagesSinceMarker);
                for(Message m : recordedMessagesSinceMarker)
                {
                    System.out.println("Messages in this channel: " + m.getMessageType().toString());
                }
            }
            else
            {
                p.printState();
                return;
            }
        }



    }

}
