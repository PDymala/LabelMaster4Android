package com.diplabels.labelmaster4android;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.InputStream;
import java.io.ObjectInputStream;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class SingleTestOnModel {
	Context context;

	public SingleTestOnModel(Context context){
		this.context = context;
	}
	double prediction;

	public static String TAG = "WEKA";
	public boolean singleTest(String testString){

		try {
			int standardStringLenght = 9; // standard number
			int secureStringLenght = 32;

			AssetManager am = context.getAssets();
			InputStream is = am.open("Trainingset_hash_50000_9_32mixOfHashes.model");

			ObjectInputStream ois = new ObjectInputStream(is);


			Classifier cls = (Classifier) ois.readObject();
			ois.close();

			Log.i(TAG, "WEKA TOOL: ");

			// for new weka
			// https://stackoverflow.com/questions/29460216/i-would-like-to-classify-new-instance-but-i-dont-want-to-load-the-dataset-to-cl
			// this is very old implementation, required by 3.4.3

			// Create vector to hold string values
			// for some reason , it always give the first value YES, even if not specified
			// in FAST VECTOR
			FastVector my_nominal_values = new FastVector(2);
			my_nominal_values.addElement("YES");

			my_nominal_values.addElement("NO");

			// Create nominal attribute
			Attribute isCorrect = new Attribute("IsCorrect", my_nominal_values);

			FastVector attss = new FastVector();
			for (int y = 0; y < standardStringLenght; y++) {

				attss.addElement(new Attribute("Standard_string" + y));

			}
			// secure number
			for (int y = 0; y < secureStringLenght; y++) {

				attss.addElement(new Attribute("Secure_string" + y));
			}

			attss.addElement(isCorrect);

			Instances datatest = new Instances("Value_to_be_checked", attss, 0);

			double[] instanceValue1 = new double[datatest.numAttributes()];

			// tested value

//			double[] value = { 1, 4, 9, 7, 2, 1, 9, 7, 5, 107, 66, 114, 105, 78, 106, 112, 64, 53, 33, 90, 107, 87, 55,
//					116, 112, 68, 108, 90, 35, 119, 76, 85, 115, 112, 87, 112, 50, 107, 78, 68, 83};
//			// 0 as YES, 1 as NO - those are labels from Instance
			// 1,4,9,7,2,1,9,7,5,107,66,114,105,78,106,112,64,53,33,90,107,87,55,116,112,68,108,90,35,119,76,85,115,112,87,112,50,107,78,68,83,NO

			StringToArray sta = new StringToArray();
			double[] value = sta.getArrayFromString(testString);


			for (int u = 0; u < value.length; u++) {
				double temp = value[u];
				instanceValue1[u] = temp;

			}

			datatest.add(new Instance(1.0, instanceValue1));

			// without this, it shows exception
			// https://stackoverflow.com/questions/40318420/weka-core-unassignedclassexception-class-index-is-negative-not-set

			if (datatest.classIndex() == -1) {
				Log.i(TAG, "reset index...");
				datatest.setClassIndex(datatest.numAttributes() - 1);
			}

			// PREDICTING !!!!
			 prediction = cls.classifyInstance(datatest.firstInstance());



		} catch (Exception e) {

			e.printStackTrace();
			Log.i(TAG, "dupa");
			return false;
		}

		if (prediction == 0.0){
			return true;
		}
		else {
			return false;
		}

	}

}
