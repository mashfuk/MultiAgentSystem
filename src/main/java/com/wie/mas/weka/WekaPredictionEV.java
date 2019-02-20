package com.wie.mas.weka;

import java.util.ArrayList;
import java.util.Stack;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class WekaPredictionEV {

	public static ArrayList<String> main(String[] args) throws Exception {

		GlobalVehicleList gvl = new GlobalVehicleList();

		ConverterUtils.DataSource source1 = new ConverterUtils.DataSource("ev_train2.arff");
		Instances train = source1.getDataSet();
		// setting class attribute if the data format does not provide this information
		// For example, the XRFF format saves the class attribute information as well
		if (train.classIndex() == -1)
			train.setClassIndex(train.numAttributes() - 1);

		ConverterUtils.DataSource source2 = new ConverterUtils.DataSource("ev_test2.arff");
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

			System.out.println(test.instance(i).stringValue(4));

			if (test.instance(i).stringValue(4).equals("no")) {
				gvl.vehile_list.add(test.instance(i).stringValue(2) + gvl.vehicle_number);
				gvl.vehicle_number++;
			}

		}

		System.out.println(gvl.vehile_list);

		return gvl.vehile_list;

	}

	public static Stack<String> hello() {

		GlobalVehicleList gvl = new GlobalVehicleList();

		ConverterUtils.DataSource source1;
		try {
			source1 = new ConverterUtils.DataSource("ev_train2.arff");
			Instances train = source1.getDataSet();
			// setting class attribute if the data format does not provide this information
			// For example, the XRFF format saves the class attribute information as well
			if (train.classIndex() == -1)
				train.setClassIndex(train.numAttributes() - 1);

			ConverterUtils.DataSource source2 = new ConverterUtils.DataSource("ev_test2.arff");
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

				System.out.println(test.instance(i).stringValue(1)+":"+test.instance(i).stringValue(4));

				if (test.instance(i).stringValue(4).equals("yes")) {
					gvl.vehile_stack_list.add(test.instance(i).stringValue(2) + gvl.vehicle_number);
					gvl.vehicle_number++;
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(gvl.vehile_list);

		return gvl.vehile_stack_list;

	}

}
