import java.util.ArrayList;
import java.util.PriorityQueue;

public class CustomPriorityQueue<E> extends PriorityQueue<E> {

    @Override
    public boolean add(E e) {
        if (size() > 5000) {
            int counter = 0;
            ArrayList<Object> list = new ArrayList<>(2600);
            for (Object o : this) {
                if (counter >= 2500)
                    list.add(o);
                counter++;
            }

            this.removeAll(list);
        }
        return super.add(e);
    }
}