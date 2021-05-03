package GU.Lesson01;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class HWPoints {
    public <T> T[] changeArrayPlacement(T[] array) {
        if (array.length > 2) {
            T firstElement = array[0];
            T replacementElement = array[2];
            array[2] = firstElement;
            array[0] = replacementElement;
        }
        return array;
    }

    public <T>ArrayList<T> arrayToListArray(T[] array){
        return new ArrayList<>(Arrays.asList(array));
    }
}

