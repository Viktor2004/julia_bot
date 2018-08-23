package messageService;

import java.util.HashMap;
import java.util.Map;

public class Victorina {
    private Map<String, Integer> count = new HashMap<>();
    private int state = 0; //0 - активна, 1- завершена

    public void increaseCount(String userId) {
        changeCount(userId, 1);
    }

    public void decreaseCount(String userId) {
        changeCount(userId, - 1);
    }

    private void changeCount(String userId, Integer changeCount) {
        if(count.containsKey(userId)) {
            Integer currCount = count.get(userId);
            count.put(userId, currCount+changeCount );
        } else {
            count.put(userId, changeCount);
        }
    }

    public Map<String, Integer> getCount() {
        return count;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "" +
                "Счет: " + count +
                '}';
    }
}
