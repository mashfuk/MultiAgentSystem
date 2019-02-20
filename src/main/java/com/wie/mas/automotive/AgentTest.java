package com.wie.mas.automotive;

import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;

//-gui 
//BR:com.wie.mas.automotive.AgentBusRecharge;TR:com.wie.mas.automotive.AgentTruckRecharge;SUVR:com.wie.mas.automotive.AgentSUVRecharge;SR:com.wie.mas.automotive.AgentSedanRecharge;B:com.wie.mas.automotive.AgentBus;S:com.wie.mas.automotive.AgentSedan;T:com.wie.mas.automotive.AgentTruck;SUV:com.wie.mas.automotive.AgentSUV;

import jade.core.behaviours.SimpleBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;

public class AgentTest extends Agent {

	static long t0 = System.currentTimeMillis();
	static int i = 0, j = 0;
	
	public static ArrayList<String> arraylist = new ArrayList<String>();

	protected void setup() {
		searchAMS();
		addBehaviour(new B1(this));
	}

	public void searchAMS() {
		AMSAgentDescription[] agents = null;
		try {
			SearchConstraints c = new SearchConstraints();// object to searh //the container exist on the System
			c.setMaxResults(new Long(-1));// define infinity result to C
			agents = AMSService.search(this, new AMSAgentDescription(), c);// putt all agent found on the system to
																			// the agents list
		} catch (Exception e) {
			System.out.println("Problem searching AMS: " + e);
			e.printStackTrace();
		}

		for (int i = 0; i < agents.length; i++) {
			AID agentID = agents[i].getName();
			System.out.println(agentID.getLocalName());
		}
	}
	
	
	
	public void listAMS() {
		AMSAgentDescription[] agents = null;
		try {
			SearchConstraints c = new SearchConstraints();// object to searh //the container exist on the System
			c.setMaxResults(new Long(-1));// define infinity result to C
			agents = AMSService.search(this, new AMSAgentDescription(), c);// putt all agent found on the system to
																			// the agents list
		} catch (Exception e) {
			System.out.println("Problem searching AMS: " + e);
			e.printStackTrace();
		}

		for (int i = 0; i < agents.length; i++) {
			AID agentID = agents[i].getName();
			System.out.println(agentID.getLocalName());
			arraylist.add(agentID.getLocalName());
		}
	}

	class B1 extends SimpleBehaviour {
		public B1(Agent a) {
			super(a);
			System.out.println((System.currentTimeMillis() - t0) / 10 * 10 + " : " + " Hello World! My name is "
					+ myAgent.getLocalName());
		}

		public void action() {
			
		}

		private boolean finished = false;

		public boolean done() {
			return finished;
		}

	} // End class B1
}
