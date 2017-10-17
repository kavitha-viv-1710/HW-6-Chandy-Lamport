import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Performs all the processor related tasks
 *
 * @author Sample
 * @version 1.0
 */


public class Processor implements Observer {

    Map<Buffer,Thread> recorderThread;
    List<Buffer> inChannels = new ArrayList<>();

    /**
     * List of output channels
     * //TODO: Homework: Use appropriate list implementation and replace null assignment with that
     *
     */
    List<Buffer> outChannels = null;

    /**
     * This is a map that will record the state of each incoming channel and all the messages
     * that have been received by this channel since the arrival of marker and receipt of duplicate marker
     * //TODO: Homework: Use appropriate Map implementation.
     */
    Map<Buffer, List<Message>> channelState = null;

    /**
     * This map can be used to keep track of markers received on a channel. When a marker arrives at a channel
     * put it in this map. If a marker arrives again then this map will have an entry already present from before.
     * Before  doing a put in this map first do a get and check if it is not null. ( to find out if an entry exists
     * or not). If the entry does not exist then do a put. If an entry already exists then increment the integer value
     * and do a put again.
     */
    Map<Buffer, Integer> channelMarkerCount = null;


    /**
     * @param id of the processor
     */
    public Processor(int id, List<Buffer> inChannels, List<Buffer> outChannels) {
        this.inChannels = inChannels;
        this.outChannels = outChannels;
        //TODO: Homework make this processor as the observer for each of its inChannel
        //Hint [loop through each channel and add Observer (this) . Feel free to use java8 style streams if it makes
        // it look cleaner]
        recorderThread = new HashMap<Buffer,Thread>();
        channelMarkerCount = new HashMap<>();
        channelState = new HashMap<>();
        inChannels.stream().forEach(in -> {in.addObserver(this); channelMarkerCount.put(in,0);});
        outChannels.stream().forEach(out -> channelMarkerCount.put(out,0));


    }


    /**
     * This is a dummy implementation which will record current state
     * of this processor
     */
    public void recordMyCurrentState() {
        System.out.println("Recording my registers...");
        System.out.println("Recording my program counters...");
        System.out.println("Recording my local variables...");
    }

    /**
     * THis method marks the channel as empty
     * @param channel
     */
    public void recordChannelAsEmpty(Buffer channel) {

        channelState.put(channel, Collections.emptyList());

    }


    /**
     * Overloaded method, called with single argument
     * This method will add a message to this processors buffer.
     * Other processors will invoke this method to send a message to this Processor
     *
     * @param message Message to be sent
     */
    public void sendMessgeTo(Message message, Buffer channel) {
        channel.saveMessage(message);

    }

    /**
     *
     * @param fromChannel channel where marker has arrived
     * @return true if this is the first marker false otherwise
     */
    public boolean isFirstMarker(Buffer fromChannel) {
        //TODO: Implemetent this method
        //[ Hint : Use the channelMarkerCount]
        if(channelMarkerCount.get(fromChannel) < 1)
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    /**
     * Gets called when a Processor receives a message in its buffer
     * Processes the message received in the buffer
     */
    public void update(Observable observable, Object arg) {

        Message message = (Message) arg;
        if (message.getMessageType().equals(MessageType.MARKER)) {
            Buffer fromChannel = (Buffer) observable;
            //TODO: homework Record from Channel as Empty
            //TODO: add logic here so that if the marker comes back to the initiator then it should stop recording
            if (isFirstMarker(fromChannel)) {
                recordChannelAsEmpty(fromChannel);
                channelMarkerCount.put(fromChannel, channelMarkerCount.get(fromChannel) + 1);
                //From the other incoming Channels (excluding the fromChannel which has sent the marker
                // startrecording messages
                //TODO: homework: Trigger the recorder thread from this processor so that it starts recording for each channel
                // Exclude the "Channel from which marker has arrived.
                Thread rThread = new RecorderThread(this, fromChannel);
                recorderThread.put(fromChannel,rThread);
                recorderThread.get(fromChannel).run();


            } else {
                //Means it isDuplicateMarkerMessage.
                //TODO: Homework Stop the recorder thread.

                Thread.currentThread().interrupt();


            }
            //TODO: Homework Send marker messages to each of the out channels
            // Hint: invoke  sendMessgeTo((Message) arg, outChannel) for each of the out channels
            outChannels.stream().forEach(out -> sendMessgeTo(new Message(MessageType.MARKER),out));

        }
        else{
            if (!message.getMessageType().equals(MessageType.MARKER)) {
                System.out.println("Processing " +  message.getMessageType().toString() + " message....");
            }  //There is no other type
        }


    }

    public void initiateSnapShot(){
        recordMyCurrentState();
        //TODO: Follow steps from Chandy Lamport algorithm. Send out a marker message on outgoing channel
        //[Hint: Use the sendMessgeTo method
        outChannels.stream().forEach(out -> sendMessgeTo(new Message(MessageType.MARKER),out));
        //TODO: homework Start recording on each of the input channels

        inChannels.stream().forEach(in -> {Thread rThread = new RecorderThread(this, in);
        recorderThread.put(in,rThread);
        recorderThread.get(in).run();

        });

    }

    public void printState()
    {
        for(List<Message> messageList : channelState.values())
        {
            for(Message m : messageList)
            {
                System.out.println("Messages in this Processor's State: " + m.getMessageType().toString());
            }
        }

    }



}
