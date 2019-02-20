package com.wie.mas.automotive;

import java.util.ArrayList;

import com.wie.mas.weka.GlobalVehicleList;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;

public class AgentRecharge extends Agent {

	static long t0 = System.currentTimeMillis();
	static int i = 0, j = 0;
	static int chargeValue= 30;
	
	ArrayList<String> al = new ArrayList<String>();
	
	GlobalVehicleList gvl = new GlobalVehicleList();

	protected void setup() {

		addBehaviour(new B1(this));
		
		gvl.recharge_station_list.add("R1");
		gvl.recharge_station_list.add("R2");
		gvl.recharge_station_list.add("R3");
		
		gvl.recharge_station_power.add("30");
		gvl.recharge_station_power.add("30");
		gvl.recharge_station_power.add("30");

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
			// System.out.println(agentID.getLocalName());
			if (!al.contains(agentID.getLocalName()))
				al.add(agentID.getLocalName());
		}

	}

	class B1 extends SimpleBehaviour {
		public B1(Agent a) {
			super(a);
			System.out.println((System.currentTimeMillis() - t0) / 10 * 10 + " : " + " Hello World! My name is "
					+ myAgent.getLocalName());
		}

		public void action() {
			
			searchAMS();

			ACLMessage s_msg = new ACLMessage(ACLMessage.INFORM);
			s_msg.setContent("Connected!!!!");

			// s_msg.addReceiver(new AID("S", AID.ISLOCALNAME));

			ACLMessage r_msg = receive();
			if (r_msg != null) {

				if (!r_msg.getContent().contains("failed to find agent")) {

					s_msg.addReceiver(new AID(r_msg.getSender().getLocalName(), AID.ISLOCALNAME));

					if(al.contains(r_msg.getSender().getLocalName()))
					send(s_msg);

					System.out.println("\n" + (System.currentTimeMillis() - t0) / 10 * 10 + " - Receiver: "
							+ myAgent.getLocalName() + " <- " + r_msg.getContent() + " - Sender: "
							+ r_msg.getSender().getLocalName());

				}
				if (r_msg.getContent().contains("failed to find agent")) {

					System.out.println("\n" + (System.currentTimeMillis() - t0) / 10 * 10 + " - Receiver: "
							+ myAgent.getLocalName() + " <- " + " Agent " + r_msg.getContent().substring(262, 285)
							+ " missing");

				}

			}

			if ((System.currentTimeMillis() - t0) > 15000) {
				// doDelete();
			}

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		private boolean finished = false;

		public boolean done() {
			return finished;
		}

	} // End class B1

}
