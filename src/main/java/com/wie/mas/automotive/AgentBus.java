package com.wie.mas.automotive;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.wie.mas.weka.GlobalVehicleList;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class AgentBus extends Agent {

	static long t0 = System.currentTimeMillis();
	static int d = 0, f = 0;

	int counter = 0;

	ArrayList<String> al = new ArrayList<String>();

	Set<String> linkedHashSet = new LinkedHashSet<>();

	GlobalVehicleList gvl = new GlobalVehicleList();

	protected void setup() {
		addBehaviour(new B1(this));
		addBehaviour(new B2(this));
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

	public void createAgent() {

		if ((System.currentTimeMillis() - t0) > 10000 && d < 5) {
			String name = "B" + d;
			AID alice = new AID(name, AID.ISLOCALNAME);

			AgentContainer c = getContainerController();

			try {
				AgentController at = c.createNewAgent(name, "com.wie.mas.automotive.AgentBus", null);
				at.start();
				d++;
				System.out.println(alice.getLocalName() + " Created");

				ACLMessage s_msg = new ACLMessage(ACLMessage.INFORM);
				s_msg.setContent("ping_weight_70");

				searchAMS();
				// System.out.println(al);

				if (al.contains("R"))
					s_msg.addReceiver(new AID("R", AID.ISLOCALNAME));
				else
					System.out.println("Bus Recharge Station not found!!!!!!");

				send(s_msg);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	class B1 extends SimpleBehaviour {
		public B1(Agent a) {
			super(a);
			System.out.println((System.currentTimeMillis() - t0) / 10 * 10 + " : " + " Hello World! My name is "
					+ myAgent.getLocalName());
		}

		public void action() {

			ACLMessage s_msg = new ACLMessage(ACLMessage.INFORM);
			s_msg.setContent("ping_weight_70");

			searchAMS();
			// System.out.println(al);

			if (al.contains("R"))
				s_msg.addReceiver(new AID("R", AID.ISLOCALNAME));
			else
				System.out.println("Bus Recharge Station not found!!!!!!");

			send(s_msg);

			ACLMessage r_msg = receive();
			if (r_msg != null) {

				if (!r_msg.getContent().contains("failed to find agent"))
					System.out.println("\n" + (System.currentTimeMillis() - t0) / 10 * 10 + " - Receiver: "
							+ myAgent.getLocalName() + " <- " + r_msg.getContent() + " - Sender: "
							+ r_msg.getSender().getLocalName());
				if (r_msg.getContent().contains("failed to find agent")) {

					System.out.println("\n" + (System.currentTimeMillis() - t0) / 10 * 10 + " - Receiver: "
							+ myAgent.getLocalName() + " <- " + " Agent " + r_msg.getContent().substring(262, 285)
							+ " missing");

				}

			}

			createAgent();

			if ((System.currentTimeMillis() - t0) > 15000) {
				// doDelete();

				ContainerController cc = getContainerController();
				AgentController ac;
				try {
					ac = cc.getAgent("B2");
					ac.suspend();
				} catch (ControllerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			if ((System.currentTimeMillis() - t0) > 25000) {
				// doDelete();

				ContainerController cc = getContainerController();
				AgentController ac;
				try {
					ac = cc.getAgent("B4");
					ac.suspend();
				} catch (ControllerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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

	class B2 extends OneShotBehaviour {
		public B2(Agent a) {

		}

		public void action() {

			createAgent2();

		}

		private void createAgent2() {
			// TODO Auto-generated method stub

			if ((System.currentTimeMillis() - t0) > 20000 && f <= 2) {

				ConverterUtils.DataSource source1;
				try {
					source1 = new ConverterUtils.DataSource("ev_bus_train.arff");
					Instances train = source1.getDataSet();
					// setting class attribute if the data format does not provide this information
					// For example, the XRFF format saves the class attribute information as well
					if (train.classIndex() == -1)
						train.setClassIndex(train.numAttributes() - 1);

					ConverterUtils.DataSource source2 = new ConverterUtils.DataSource("ev_bus_test.arff");
					Instances test = source2.getDataSet();
					// setting class attribute if the data format does not provide this information
					// For example, the XRFF format saves the class attribute information as well
					if (test.classIndex() == -1)
						test.setClassIndex(train.numAttributes() - 1);

					// model

					NaiveBayes naiveBayes = new NaiveBayes();
					naiveBayes.buildClassifier(train);

					for (int i = 0; i < test.numInstances(); i++) {
						// this does the trick
						double label = naiveBayes.classifyInstance(test.instance(i));
						test.instance(i).setClassValue(label);

						System.out.println(test.instance(i).stringValue(1) + ":" + test.instance(i).stringValue(4));

						if (test.instance(i).stringValue(4).equals("yes")) {
							gvl.vehile_stack_list.add(test.instance(i).stringValue(2) + gvl.vehicle_number);
							gvl.vehicle_number++;
							linkedHashSet.add(test.instance(i).stringValue(2) + gvl.vehicle_number);
						}

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println(gvl.vehile_list);
				System.out.println(linkedHashSet.size());

				Iterator<String> itr = linkedHashSet.iterator();

				while (itr.hasNext()) {

					System.out.println(itr.next());

					if (counter <= 1) {
						String name = itr.next();
						AID alice = new AID(name, AID.ISLOCALNAME);

						AgentContainer c = getContainerController();

						try {
							AgentController a = c.createNewAgent(name, "com.wie.mas.automotive.AgentBus", null);
							a.start();
							System.out.println(alice.getLocalName() + " Created");

							ACLMessage s_msg = new ACLMessage(ACLMessage.INFORM);
							s_msg.setContent("ping_weight_70");

							searchAMS();
							// System.out.println(al);

							if (al.contains("R"))
								s_msg.addReceiver(new AID("R", AID.ISLOCALNAME));
							else
								System.out.println("Bus Recharge Station not found!!!!!!");

							send(s_msg);

						} catch (Exception e) {
							e.printStackTrace();
						}

						counter++;
					}
				}
			}

			f++;

		}

	} // End class B2
}
