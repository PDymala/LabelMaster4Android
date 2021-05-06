package com.diplabels.labelmaster4android;

public class StringToArray {
    public StringToArray(){

    }

    public double[] getArrayFromString(String string){
        String[] array = string.split("-", -1);

        double[] tempInt = new double[array.length];
        for(int x = 0; x < array.length; x++){
            tempInt[x] = Integer.parseInt(array[x]);

        }


    return tempInt;
    }



}
