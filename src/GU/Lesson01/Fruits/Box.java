package GU.Lesson01.Fruits;

import GU.Lesson01.Fruits.Ext.Fruit;

import java.util.ArrayList;


public class Box<T extends Fruit> {

    private ArrayList<T> boxContent = new ArrayList<T>();

    public void putInto(T fruit) {
        boxContent.add(fruit);
    }


    public float getWeight() {
        float result = 0f;
        for (T fruit : boxContent) {
            if (fruit != null) result += fruit.getWeight();
        }
        return result;
    }

    public boolean boxWeightEqualsOf(Box<? extends Fruit> anotherBox) {
        if (anotherBox != null) {
            return this.getWeight() == anotherBox.getWeight();
        }
        return false;
    }

    public void relocateContentTo(Box<T> box){

        box.boxContent = (ArrayList<T>) boxContent.clone(); // Unchecked cast: 'java.lang.Object' to 'java.util.ArrayList<T>'
        boxContent.clear();                              // Как же сделать его проверенным?
    }

}
