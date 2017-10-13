package com.sjsu.chandylamport;

/**
 * This is the simulation of a main algorithm that will run on processors P1, P2, P3
 * This could be a banking application, payroll application or any other distributed application
 */
public class Algorithm {

    /**
     * The processors which will participate in a distributed application
     */
    Processor processor1, processor2, processor3;

    public Algorithm(Processor processor1, Processor processor2, Processor processor3) {
        //TODO: Homework: Initialize processors so that they represent the topology of 3 processor system
    	this.processor1 = processor1;
    	this.processor2 = processor2;
    	this.processor3 = processor3;
    }



    public void executionPlan1 (){
        compute(processor1);
        compute(processor1);
        compute(processor1);
        compute(processor1);
        compute(processor1);
        compute(processor1);
        compute(processor1);
        
        
/**
 * TODO: Homework: Implement send message from processor1 to different processors. Add a time gap between two different
 *                send events. Add computation events between two different sends.
 *      [Hint: Create a loop that kills time, sleep , wait on some value etc..]
 *
 */
        for(Buffer i : processor1.outChannels)
        {
        	Message m = new Message(MessageType.SEND);
        	processor1.sendMessgeTo(m,i);
        	Thread.sleep(300);
        
        }
    }

    // Write hard coded execution plan for processors
    public void executionPlanP2() {
    	 compute(processor2);
    	 compute(processor2);
    	 compute(processor2);
    	 for (Buffer i : processor2.outChannels)
    	 {
    		processor2.sendMessageTo(new Message(MessageType.ALGORITHM), i);
 			processor2.sendMessageTo(new Message(MessageType.COMPUTATION), i);
 			processor2.sendMessageTo(new Message(MessageType.ALGORITHM), i);
 			processor2.sendMessageTo(new Message(MessageType.RECEIVE), i);
 			processor2.sendMessageTo(new Message(MessageType.SEND), i);
 		
    	 }
    	 


    }

    // Write hard coded execution plan for processors
    public void executionPlanP3() {
    	 compute(processor3);
    	 compute(processor3);
    	 for (Buffer i : processor3.outChannels)
    	 	processor3.sendMessageTo(new Message(MessageType.ALGORITHM), i);
			processor3.sendMessageTo(new Message(MessageType.COMPUTATION), i);
			processor3.sendMessageTo(new Message(MessageType.RECEIVE), i);
			processor3.sendMessageTo(new Message(MessageType.SEND), i);
		
    }

    /**
     * A dummy computation.
     * @param p
     */
    public void compute(Processor p) {
        System.out.println("Doing some computation on " + p.getClass().getSimpleName());
    }

    /**
     *
     * @param to processor to which message is sent
     * @param channel the incoming channel on the to processor that will receive this message
     */
    public void send(Processor to, Buffer channel) {
        to.sendMessgeTo(new Message(MessageType.ALGORITHM), channel); // ALGORITHM
    }

}
