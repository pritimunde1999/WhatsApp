package com.driver;

import java.util.*;

import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;

    //private HashMap<Integer, Message> messageMap;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository() {
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        //this.messageMap = new HashMap<>();
        this.customGroupCount = 1;
        this.messageId = 0;
    }

    public String createUser(String name, String mobileNo) {
        if (!userMobile.contains(mobileNo)) {
           User user = new User(name, mobileNo);
//            userMap.put(mobileNo, user);
            userMobile.add(mobileNo);
            return "SUCCESS";
         }

        return null;
    }


    public Group createGroup(List<User> users) {
        if (users.size() < 2) return null;

        if (users.size() == 2) {
            Group group = new Group(users.get(1).getName(), 2);
            groupUserMap.put(group, users);
            adminMap.put(group, users.get(0));
            return group;
        }

        if (users.size() > 2) {
            Group group = new Group("Group" + customGroupCount, users.size());
            groupUserMap.put(group, users);
            adminMap.put(group, users.get(0));
            customGroupCount++;
            return group;
        }

        return null;
    }

    public int createMessage(String content) {
        messageId++;
        Message msg = new Message(messageId, content, new Date());
        senderMap.put(msg,null);
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) {

        if (!(groupUserMap.containsKey(group))) return -1;

        if (!(groupUserMap.get(group).contains(sender))) return -2;

        if (groupUserMap.containsKey(group) && groupUserMap.get(group).contains(sender)) {
            if (groupMessageMap.containsKey(group)) {
                groupMessageMap.get(group).add(message);
            } else {
                List<Message> list = new ArrayList<>();
                list.add(message);
                groupMessageMap.put(group, list);
            }
        }

        senderMap.put(message,sender);
        return groupMessageMap.get(group).size();
    }

    public String changeAdmin(User approver, User user, Group group) {
        if (!(groupUserMap.containsKey(group))) {
            return "Group doesnot exist";
        }

        if (!(adminMap.get(group).equals(approver))) {
            return "not admin";
        }

        if (!(groupUserMap.get(group).contains(user))) {
            return "user not in a group";
        }
        else
        {
            adminMap.put(group, user);
        }

        return "SUCCESS";
    }
}
