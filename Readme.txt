Homework 6: Readme.txt
Chandy Lamport

Introduction: The following Java program implements the Chandy Lamport Algorithm. This algorithm is used to save a global snapshot of a system. Snapshot should not interfere with normal application actions, and it should not require application to stop sending messages. For this project we are implemting for a 3 process model.

Chandy Lamport Algorithm:
With N processes in the system, there are two uni-directional communication channels between each ordered process pair: P_j -> P_i and P_i->P_j. Communications channels are FIFO-ordered. 

• Let's say that process P_i initiates the snapshot. 
• P_i records its own state and creates special messages called "Marker" messages. Note an application message, does not interfere with application messages.
• P_i sends the marker message to all other processess on it's outbound channels.
• Start recording all incoming messages from channels C_ji for j not equal to i.
• For all processes Pj (including the initiator), consider a message on channel Ckj
• If we see marker message for the first time
	– Pj records own state and marks Ckj as empty
	– Send the marker message to all other processes  (using N-1 outbound channels)
	– Start recording all incoming messages from channels Clj for l not equal to j or k
• Else add all messages from inbound channels since we began recording to their states P

Files included in Project:
	Buffer.java
	Message.java
	MessageType.java
	Processor.java
	Main.java
	Algorithm.java

Project Design:
	Starting with the skeleton code given by Professor Tanjua we started three separate threads for the three processes in the system so that their execution plan will run in a true distributed style. A process will start the initiation of the snapshot by calling "initiateSnapShot()" which belongs to the Processor class. This will have the process save it's state by printing out a message that says saving state. For every processor there is a predetermined execution plan that is run as a thread. For every processor there is an update method that will respond according to what type of message enters the buffer. The execution plan includes computation, algorithm, send, and receive messages. After initatesnapshot method is called and the initial processor will send out special marker messages to all of the processors in the system. The update method monitors the buffers and any time there is any messages update will react accordingly to the algorithm. It will send out Marker messages to all in channels of that processor except for the channel it received a marker message from. This happens only when a  processor receives a marker message for the first time. It will then call RecoderThread for that channel. RecordThread is a class that is responsible for recording messages for a Processor’s in channels. We have used a thread that will record for each in channel, and will stop an interrupt is thrown. This interrupt is thrown when a processor receives a duplicate marker message.
	
	We ran into issues while recording. Although we have an interrupt, the recorder class doesn’t ever catch it. This leads to our code being stuck in a loop where the if statement to end the loop is never true. The interrupt is never caught. If the interrupt is caught our code would behave so that the recording for a channel would stop and print out the contents that are recorded. 
	
Tester Cases:   
	We tested our code by having each processor in our three system model as the initiator of the snapshot.

