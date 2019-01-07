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
package org.openml.webapplication.algorithm;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import weka.classifiers.CostMatrix;
import weka.core.Utils;
import weka.core.Instance;
import weka.core.Instances;

public class InstancesHelper {

	public static void setTargetAttribute( Instances instances, String classAttribute ) throws Exception {
		for(int i = 0; i < instances.numAttributes(); ++i ) {
			if(instances.attribute(i).name().equals(classAttribute)) {
				instances.setClassIndex(i);
				return;
			}
		}
		throw new Exception("classAttribute " + classAttribute + " non-existant on dataset. ");
	}
	
	public static int getRowIndex( String[] names, Instances instances ) throws Exception {
		for( String name : names ) {
			int probe = getRowIndex(name, instances);
			if( probe >= 0 ) return probe;
		}
		throw new Exception( "Arff file contains none of the specified attributes: " + Arrays.toString( names ) );
	}
	
	public static int getRowIndex( String name, Instances instances ) {
		return (instances.attribute( name ) != null) ? instances.attribute( name ).index() : -1;
	}
	
	public static int[] classCounts( Instances dataset ) {
		int[] count = new int[dataset.classAttribute().numValues()];
		for( int i = 0; i < dataset.numInstances(); ++i ) {
			count[(int)dataset.instance(i).classValue()]++;
		}
		return count;
	}
	
	public static double[] classRatios( Instances dataset ) {
		double[] result = new double[dataset.classAttribute().numValues()];
		int[] count = classCounts( dataset );
		
		for( int i = 0; i < result.length; ++i ) {
			result[i] = count[i] * 1.0 / dataset.numInstances();
		}
		
		return result;
	}
	
	// can only be used for classification
	public static double[] predictionToConfidences( Instances dataset, Instance prediction, int[] att_prediction_confidence, int att_prediction ) throws Exception {
		double[] confidences = new double[dataset.numClasses()];
		boolean nonNullValue = false;
		for( int i = 0; i < dataset.numClasses(); i++ ) {
			if( Utils.isMissingValue( prediction.value( att_prediction_confidence[i] ) ) ) {
				throw new Exception("Prediction file contains missing values for important attribute (" + prediction.attribute( att_prediction_confidence[i] ).name() + "). ");
			}
			confidences[i] = prediction.value( att_prediction_confidence[i] );
			if( confidences[i] > 0 ) {
				nonNullValue = true;
			}
		}
		
		if( nonNullValue == false ) {
			confidences[(int) prediction.value(att_prediction)] = 1;
		}
		
		return confidences;
	}
	
	public static double[] toProbDist( double[]  d ) {
		double total = 0;
		double[] result = new double[d.length];
		for( int i = 0; i < d.length; ++i ) { // scan for infinity
			if( Double.isInfinite( d[i] ) ) {
				result[i] = 1.0;
				return result;
			}
		}
		
		for( int i = 0; i < d.length; ++i ) { 
			if( Double.isNaN( d[i] ) == false ) // only if it is a legal nr. 
				total += d[i];
		}
		
		if( total == 0.0 ) { // If none of the classes were predicted we go for the first class, by default.
			result[0] = 1.0;
			return result;
		}
		
		for( int i = 0; i < d.length; ++i ) {
			if( Double.isNaN( d[i] ) )
				result[i] = 0.0D;
			else if( total > 0.0 )
				result[i] = d[i] / total;
			else 
				result[i] = d[i];
		}
		return result;
	}
	
	public static CostMatrix doubleToCostMatrix( double[][] cm ) {
		CostMatrix costmatrix = new CostMatrix(cm.length);
		for( int i = 0; i < cm.length; ++i ) {
			for( int j = 0; j < cm[i].length; ++j ) {
				costmatrix.setElement( i, j, cm[i][j]);
			}
		}
		return costmatrix;
	}
	
	@SuppressWarnings("unchecked")
	public static void stratify( Instances dataset ) {
		int numClasses = dataset.classAttribute().numValues();
		int numInstances = dataset.numInstances();
		double[] classRatios = classRatios( dataset );
		double[] currentRatios = new double[numClasses];
		int[] currentCounts = new int[numClasses];
		List<Instance>[] instancesSorted = new LinkedList[numClasses];
		
		for( int i = 0; i < numClasses; ++i ) {
			instancesSorted[i] = new LinkedList<Instance>();
		}
		
		// first, sort all instances based on class in different lists
		for( int i = 0; i < numInstances; ++i ) {
			Instance current = dataset.instance(i);
			instancesSorted[(int) current.classValue()].add( current );
		}
		
		// now empty the original dataset, all instances are stored in the L.L.
		for( int i = 0; i < numInstances; i++ ) {
			dataset.delete( dataset.numInstances() - 1 );
		}
		
		for( int i = 0; i < numInstances; ++i ) {
			int idx = biggestDifference( classRatios, currentRatios );
			dataset.add( instancesSorted[idx].remove( 0 ) );
			currentCounts[idx]++;
			
			for( int j = 0; j < currentRatios.length; ++j ) {
				currentRatios[j] = (currentCounts[j] * 1.0) / (i+1);
			}
		}
	}
	
	private static int biggestDifference( double[] target, double[] current ) {
		int biggestIdx = -1;
		double biggestValue = Integer.MIN_VALUE;
		for( int i = 0; i < target.length; ++i ) {
			double currentValue = target[i] - current[i];
			if( currentValue > biggestValue ) {
				biggestIdx = i;
				biggestValue = currentValue;
			}
		}
		return biggestIdx;
	}
}