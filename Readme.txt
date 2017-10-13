Homework 6: Readme.txt Chandy Lamport

Introduction: The following Java program implements the Chandy Lamport algorithm to get the global snapshot of a 3 processor system. We are not yet able to implement the whole algorithm, we will be turning in another version that is complete.

The Chandy Lamport algorithm allows a way to see a global snapshot of a system. A global snapshot is a state of a system as a whole which includes all the processors as well as all of the channels that make up the system.

Algorithm approach: For each processor in the system there is an algorithm that is running that will not be disturbed. We are not concerned with what algorithm is running but only concerned with the global state. We begin by having one processor initiate the Chandy Lamport algorithm. This processor will take a snapshot (save all of it's registers, heaps, counter...) and send on all of it's outgoing channels a marker message. This marker message will be received by the processors that are connected to the channel on the other side. 

Upon receiving a marker message for the first time a processor will:

1. Save it's state
2. Mark the channel where it received the marker message as empty 
3. Start recording on all of it's incoming channel
4. Send out a marker mesasge on all its outgoing channel

Upon receving a duplicate marker message (i.e. not receving a marker message for the first time)

1. It will mark the state of the channel as all messages that have arived on it since recording was turned on for that channel. 

Note: We have yet to fully implement the Chandy Lamport Algorithm. We will be finishing the project and submitting the complete code.

Files included in Project: Algorithm.java, Main.java, Message.java, MessageType.java, Processor.java, Buffer.java
