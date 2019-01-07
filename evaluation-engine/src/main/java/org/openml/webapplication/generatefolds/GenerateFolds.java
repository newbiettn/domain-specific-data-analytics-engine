/*
 *  Webapplication - Java library that runs on OpenML servers
 *  Copyright (C) 2014 
 *  @author Jan N. van Rijn (j.n.van.rijn@liacs.leidenuniv.nl)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 */
package org.openml.webapplication.generatefolds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.openml.apiconnector.algorithms.Input;
import org.openml.apiconnector.io.OpenmlConnector;
import org.openml.webapplication.algorithm.InstancesHelper;
import org.openml.webapplication.generatefolds.EstimationProcedure.EstimationProcedureType;
import org.openml.webapplication.io.Md5Writer;
import org.openml.webapplication.io.Output;

import weka.core.Attribute;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;

public class GenerateFolds {
	public static final int MAX_SPLITS_SIZE = 1000000;
	
	private final Instances dataset;
	private final Instances splits;
	private final String splits_name;
	private final Integer splits_size;
	
	private final EstimationProcedure evaluationMethod;
	
	private final ArffMapping am;
	private final Random rand;
	
	public GenerateFolds( OpenmlConnector ac, String datasetName, URL datasetPath, String estimationProcedure, String targetFeature, List<List<List<Integer>>> testset, int random_seed ) throws Exception {
		
		rand = new Random(random_seed);
		dataset = new Instances( new BufferedReader(Input.getURL(datasetPath)));
		evaluationMethod = new EstimationProcedure(estimationProcedure,dataset);
		
		InstancesHelper.setTargetAttribute( dataset, targetFeature );
		
		am = new ArffMapping( evaluationMethod.getEvaluationMethod() == EstimationProcedureType.LEARNINGCURVE);
		
		splits_name = datasetName + "_splits";
		splits_size = evaluationMethod.getSplitsSize(dataset);
		
		// we are not allowed to use official row_id, even if it exist,
		// since there is no guarantee that this runs from 0 -> n - 1
		addRowId(dataset,"rowid");
		
		splits = generateInstances(splits_name, testset);
	}
	
	public void toFile( String splitsPath ) throws IOException {
		FileWriter f = new FileWriter( new File( splitsPath ) );
		Output.instanes2file(splits, f, null );
	}
	
	public void toStdout() throws IOException {
		Output.instanes2file(splits, new OutputStreamWriter( System.out ), null );
	}
	
	public void toStdOutMd5() throws NoSuchAlgorithmException, IOException {
		Output.instanes2file(splits, new Md5Writer(), null );
	}
	
	private Instances generateInstances(String name, List<List<List<Integer>>> testset) throws Exception {
		switch(evaluationMethod.getEvaluationMethod()) {
			case HOLDOUT:	
				return sample_splits_holdout( name );
			case CROSSVALIDATION: 
				return sample_splits_crossvalidation( name );
			case LEAVEONEOUT: 
				return sample_splits_leaveoneout( name );
			case LEARNINGCURVE: 
				return sample_splits_learningcurve( name );
			case HOLDOUT_UNLABELED:
				return sample_splits_holdout_unlabeled( name );
			case CUSTOMHOLDOUT: 
				return sample_splits_holdout_userdefined( name, testset );
			case BOOTSTRAP:
				return sample_splits_bootstrap( name );
			default:
				throw new RuntimeException("Illigal evaluationMethod (GenerateFolds::generateInstances)");
		}
	}
	
	private Instances sample_splits_holdout( String name ) {
		Instances splits = new Instances(name,am.getArffHeader(),splits_size);
		for( int r = 0; r < evaluationMethod.getRepeats(); ++r) {
			dataset.randomize(rand);
			int testSetSize = Math.round(dataset.numInstances()*evaluationMethod.getPercentage()/100);
			
			for( int i = 0; i < dataset.numInstances(); ++i ) {
				int rowid = (int) dataset.instance(i).value(0);
				splits.add(am.createInstance(i >= testSetSize,rowid,r,0));
			}
		}
		return splits;
	}
	
	private Instances sample_splits_crossvalidation( String name ) {
		Instances splits = new Instances(name,am.getArffHeader(),splits_size);
		for( int r = 0; r < evaluationMethod.getRepeats(); ++r) {
			dataset.randomize(rand);
			if (dataset.classAttribute().isNominal())
				dataset.stratify(evaluationMethod.getFolds());
			
			for( int f = 0; f < evaluationMethod.getFolds(); ++f ) {
				Instances train = dataset.trainCV(evaluationMethod.getFolds(), f);
				Instances test = dataset.testCV(evaluationMethod.getFolds(), f);
				
				for( int i = 0; i < train.numInstances(); ++i ) {
					int rowid = (int) train.instance(i).value(0);
					splits.add(am.createInstance(true,rowid,r,f));
				}
				for( int i = 0; i < test.numInstances(); ++i ) {
					int rowid = (int) test.instance(i).value(0);
					splits.add(am.createInstance(false,rowid,r,f));
				}
			}
		}
		return splits;
	}
	
	private Instances sample_splits_leaveoneout( String name ) {
		Instances splits = new Instances(name,am.getArffHeader(),splits_size);
		for( int f = 0; f < dataset.numInstances(); ++f ) {
			for( int i = 0; i < dataset.numInstances(); ++i ) {
				int rowid = (int) dataset.instance(i).value(0);
				splits.add(am.createInstance(f!=i,rowid,0,f));
			}
		}
		return splits;
	}
	
	private Instances sample_splits_learningcurve( String name ) {
		Instances splits = new Instances(name,am.getArffHeader(),splits_size);
		for( int r = 0; r < evaluationMethod.getRepeats(); ++r ) {
			dataset.randomize(rand);
			if (dataset.classAttribute().isNominal())
				InstancesHelper.stratify(dataset); // do our own stratification
			
			for( int f = 0; f < evaluationMethod.getFolds(); ++f ) {
				Instances train = dataset.trainCV(evaluationMethod.getFolds(), f);
				Instances test = dataset.testCV(evaluationMethod.getFolds(), f);
				
				for( int s = 0; s < EstimationProcedure.getNumberOfSamples( train.numInstances() ); ++s ) {
					for( int i = 0; i < EstimationProcedure.sampleSize( s, train.numInstances() ); ++i ) {
						int rowid = (int) train.instance(i).value(0);
						splits.add(am.createInstance(true,rowid,r,f,s));
					}
					for( int i = 0; i < test.numInstances(); ++i ) {
						int rowid = (int) test.instance(i).value(0);
						splits.add(am.createInstance(false,rowid,r,f,s));
					}
				}
			}
		}
		return splits;
	}
	
	private Instances sample_splits_bootstrap( String name ) throws Exception {
		Instances splits = new Instances(name,am.getArffHeader(),splits_size);
		for( int r = 0; r < evaluationMethod.getRepeats(); ++r) {
			Resample resample = new Resample();
			String[] resampleOptions = { "-B", "0.0", "-Z", "100.0", "-S", r + "" };
			resample.setOptions(resampleOptions);
			resample.setInputFormat(dataset);
			Instances trainingsset = Filter.useFilter(dataset, resample);
			
			// create training set, consisting of instances from 
			for( int i = 0; i < trainingsset.numInstances(); ++i ) {
				int rowid = (int) trainingsset.instance(i).value(0);
				splits.add(am.createInstance(true,rowid,r,0));
			}
			for( int i = 0; i < dataset.numInstances(); ++i ) {
				int rowid = (int) dataset.instance(i).value(0);
				splits.add(am.createInstance(false,rowid,r,0));
			}
		}
		return splits;
	}
	
	private Instances sample_splits_holdout_unlabeled( String name ) {
		Instances splits = new Instances(name,am.getArffHeader(),splits_size);
		
		// do not randomize data set, as this method is based on user defined splits
		for( int i = 0; i < dataset.size(); ++i ) {
			if( dataset.get(i).classIsMissing() ) {
				splits.add(am.createInstance(false,i,0,0));
			} else {
				splits.add(am.createInstance(true,i,0,0));
			}
		}
		
		return splits;
	}
	
	private Instances sample_splits_holdout_userdefined( String name, List<List<List<Integer>>> testset ) {
		Instances splits = new Instances(name,am.getArffHeader(),splits_size);
		if( testset == null ) {
			throw new RuntimeException("Option -test not set correctly. ");
		}
		
		for( int r = 0; r < evaluationMethod.getRepeats(); ++r ) {
			for( int f = 0; f < evaluationMethod.getFolds(); ++f ) {
				Collections.sort(testset.get(r).get(f));
				// do not randomize data set, as this method is based on user defined splits
				for( int i = 0; i < dataset.size(); ++i ) {
					if( Collections.binarySearch(testset.get(r).get(f), i) >= 0 ) {
						splits.add(am.createInstance(false,i,r,f));
					} else {
						splits.add(am.createInstance(true,i,r,f));
					}
				}
			}
		}
		
		return splits;
	}
	
	private static Instances addRowId( Instances instances, String name ) {
		instances.insertAttributeAt(new Attribute(name), 0);
		for( int i = 0; i < instances.numInstances(); ++i )
			instances.instance(i).setValue(0, i);
		return instances;
	}
}
