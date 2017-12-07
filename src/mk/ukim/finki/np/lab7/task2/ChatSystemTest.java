package mk.ukim.finki.np.lab7.task2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static java.util.Collections.singletonList;


class NoSuchRoomException extends Exception {
    public NoSuchRoomException(String message) {
        super(message);
    }
}

class NoSuchUserException extends Exception {
    public NoSuchUserException(String message) {
        super(message);
    }
}

class ChatRoom {
    private String name;
    private TreeSet<String> users;

    public ChatRoom(String name) {
        this.name = name;
        users = new TreeSet<>();
    }

    public void addUser(String username) {
        users.add(username);
    }

    public void removeUser(String username) {
        users.remove(username);
    }

    public boolean hasUser(String username) {
        return users.contains(username);
    }

    public int numUsers() {
        return users.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        if (users.isEmpty()) {
            sb.append("EMPTY").append("\n");
            return sb.toString();
        }

        users.forEach(user -> sb.append(user).append("\n"));
        return sb.toString();
    }

    public String getName() {
        return name;
    }
}

class ChatSystem {
    private TreeMap<String, ChatRoom> chatRoomMap;
    private HashMap<String, HashSet<String>> usersMap;  // key: username, value: roomNames

    public ChatSystem() {
        this.chatRoomMap = new TreeMap<>();
        usersMap = new HashMap<>();
    }

    public void addRoom(String roomName) {
        chatRoomMap.put(roomName, new ChatRoom(roomName));
    }

    public void removeRoom(String roomName) {
        chatRoomMap.remove(roomName);
        usersMap.values().forEach(hs -> hs.remove(roomName));
    }

    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        if (!chatRoomMap.containsKey(roomName)) throw new NoSuchRoomException(roomName);
        return chatRoomMap.get(roomName);
    }

    public void register(String userName) {
        if (chatRoomMap.isEmpty()) {
            usersMap.put(userName, new HashSet<>());
            return;
        }

        ChatRoom minChatRoom = chatRoomMap.lastEntry().getValue();
        int minUsers = minChatRoom.numUsers();
        String roomName = null;
        for (ChatRoom room : chatRoomMap.descendingMap().values()) {
            if (room.numUsers() <= minUsers) {
                minUsers = room.numUsers();
                minChatRoom = room;
                roomName = room.getName();
            }
        }

        minChatRoom.addUser(userName);
        usersMap.put(userName, new HashSet<>(singletonList(roomName)));
    }

    public void registerAndJoin(String username, String roomName) {
        chatRoomMap.get(roomName).addUser(username);
        usersMap.put(username, new HashSet<>(singletonList(roomName)));
    }

    public void joinRoom(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if (!chatRoomMap.containsKey(roomName)) throw new NoSuchRoomException(roomName);
        if (!usersMap.containsKey(userName)) throw new NoSuchUserException(userName);
        chatRoomMap.get(roomName).addUser(userName);
        usersMap.get(userName).add(roomName);
    }

    public void leaveRoom(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if (!chatRoomMap.containsKey(roomName)) throw new NoSuchRoomException(roomName);
        if (!usersMap.containsKey(userName)) throw new NoSuchUserException(userName);
        chatRoomMap.get(roomName).removeUser(userName);
        usersMap.get(userName).remove(roomName);
    }

    public void followFriend(String userName, String friendUserName) throws NoSuchUserException {
        if (!usersMap.containsKey(userName)) throw new NoSuchUserException(userName);
        usersMap.get(friendUserName).forEach(room -> {
            chatRoomMap.get(room).addUser(userName);
            usersMap.get(userName).add(room);
        });
    }
}

public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr.addUser(jin.next());
                if (k == 1) cr.removeUser(jin.next());
                if (k == 2) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if (n == 0) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr2.addUser(jin.next());
                if (k == 1) cr2.removeUser(jin.next());
                if (k == 2) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if (k == 1) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while (true) {
                String cmd = jin.next();
                if (cmd.equals("stop")) break;
                if (cmd.equals("print")) {
                    System.out.println(cs.getRoom(jin.next()) + "\n");
                    continue;
                }
                for (Method m : mts) {
                    if (m.getName().equals(cmd)) {
                        String params[] = new String[m.getParameterTypes().length];
                        for (int i = 0; i < params.length; ++i) params[i] = jin.next();
                        m.invoke(cs, params);
                    }
                }
            }
        }
    }

}
