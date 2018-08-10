package messenger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ServerNode {
    private final Map<Long, Map<Integer, List<String>>> messageQueues = new HashMap<Long, Map<Integer, List<String>>>();

    public void send(String message, Long owner, int id) {
        Map<Integer, List<String>> ownerMap = messageQueues.get(owner);
        if (ownerMap == null) {
            return;
        }
        List<String> reciverList = ownerMap.get(id);
        if (reciverList == null) {
            return;
        }
        reciverList.parallelStream().forEach(new Consumer<String>() {
            @Override
            public void accept(String reciver) {
            }
        });
    }
}
