package com.acmenxd.frame.basis;

import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/6/20 11:45
 * @detail Activity节点管理器
 */
public enum ActivityNodeManager {
    INSTANCE;
    private static Stack<Stack<Map<Class<? extends FrameActivity>, Bundle>>> activityNode = new Stack<>();
    private Class<? extends FrameActivity> firstNode;

    /**
     * 跳到栈顶
     *
     * @return
     */
    public synchronized void skipTop() {
        skipNode(firstNode);
    }

    /**
     * 跳到指定节点
     */
    public synchronized Bundle skipNode(@NonNull Class<? extends FrameActivity> node) {
        boolean isSkip = false;
        Bundle bundle = null;
        Stack<Stack<Map<Class<? extends FrameActivity>, Bundle>>> skipStack = new Stack<>();
        Iterator<Stack<Map<Class<? extends FrameActivity>, Bundle>>> iterator = activityNode.iterator();
        while (iterator.hasNext()) {
            Stack<Map<Class<? extends FrameActivity>, Bundle>> stack = iterator.next();
            if (stack.firstElement().containsKey(node)) {
                isSkip = true;
                bundle = stack.firstElement().get(node);
            }
            if (isSkip) {
                skipStack.add(stack);
            }
        }
        for (int i = skipStack.size() - 1; i >= 0; i--) {
            Stack<Map<Class<? extends FrameActivity>, Bundle>> stack = skipStack.get(i);
            if (stack != null && stack.size() > 0) {
                for (int j = stack.size() - 1; j >= 0; j--) {
                    Map<Class<? extends FrameActivity>, Bundle> map = stack.get(j);
                    if (map != null && map.size() > 0) {
                        for (Class<? extends FrameActivity> n : map.keySet()) {
                            if (n != firstNode) {
                                ActivityStackManager.INSTANCE.finishActivity(n, false);
                            }
                        }
                    }
                }
            }
            if (stack != null && stack.size() > 0 && stack.firstElement().containsKey(firstNode)) {
                Map<Class<? extends FrameActivity>, Bundle> onlyOne = stack.firstElement();
                stack.clear();
                stack.add(onlyOne);
            } else {
                activityNode.remove(stack);
            }
        }
        return bundle;
    }

    /**
     * 跳到指定节点,并启动节点的Activity
     */
    public synchronized void skipAndStartNode(@NonNull Class<? extends FrameActivity> node) {
        Bundle bundle = skipNode(node);
        if (node != firstNode) {
            ActivityStackManager.INSTANCE.currentActivity().startActivity(node, bundle);
        }
    }

    /**
     * 添加一个节点
     */
    public synchronized void addFirstNode(@NonNull Class<? extends FrameActivity> node, Bundle pBundle) {
        activityNode.clear();
        addNode(node, pBundle);
        firstNode = node;
    }

    /**
     * 添加一个节点
     */
    public synchronized void addNode(@NonNull Class<? extends FrameActivity> node, Bundle pBundle) {
        if (pBundle == null) {
            pBundle = new Bundle();
        }
        if (node == firstNode) {
            activityNode.firstElement().firstElement().put(node, pBundle);
        } else {
            Stack<Map<Class<? extends FrameActivity>, Bundle>> stack = new Stack<>();
            Map<Class<? extends FrameActivity>, Bundle> map = new ConcurrentHashMap<>();
            map.put(node, pBundle);
            stack.add(map);
            activityNode.add(stack);
        }
    }

    /**
     * 添加一个child
     */
    protected synchronized void addChild(@NonNull Class<? extends FrameActivity> child, @NonNull Bundle pBundle) {
        if (pBundle == null) {
            pBundle = new Bundle();
        }
        if (activityNode != null && activityNode.size() > 0) {
            Stack<Map<Class<? extends FrameActivity>, Bundle>> stack = activityNode.lastElement();
            if (stack != null && stack.size() > 0 && !stack.firstElement().containsKey(child)) {
                Map<Class<? extends FrameActivity>, Bundle> map = new ConcurrentHashMap<>();
                map.put(child, pBundle);
                stack.add(map);
            }
        }
    }

    /**
     * 移除一个节点
     */
    public synchronized void removeNode(@NonNull Class<? extends FrameActivity> node) {
        if (node == firstNode) {
            return;
        }
        Stack<Map<Class<? extends FrameActivity>, Bundle>> removeStack = null;
        Stack<Map<Class<? extends FrameActivity>, Bundle>> prevStack = null;
        for (Stack<Map<Class<? extends FrameActivity>, Bundle>> stack : activityNode) {
            if (stack.firstElement().containsKey(node)) {
                removeStack = stack;
                if (prevStack != null && prevStack.size() > 0) {
                    prevStack.addAll(stack);
                }
                break;
            }
            prevStack = stack;
        }
        activityNode.remove(removeStack);
    }

    /**
     * 获取当前节点
     */
    public synchronized Class<? extends FrameActivity> currentNode() {
        Stack<Map<Class<? extends FrameActivity>, Bundle>> stack = activityNode.lastElement();
        if (stack != null && stack.size() > 0) {
            Map<Class<? extends FrameActivity>, Bundle> map = stack.firstElement();
            for (Class<? extends FrameActivity> n : map.keySet()) {
                return n;
            }
        }
        return null;
    }

    /**
     * 判断Node是否在栈顶
     */
    public synchronized boolean isCurrentNode(@NonNull Class<? extends FrameActivity> node) {
        return node == currentNode();
    }

    /**
     * 获取前一个节点
     */
    public synchronized Class<? extends FrameActivity> prevNode(@NonNull Class<? extends FrameActivity> node) {
        if (node == firstNode || activityNode.size() <= 1) {
            return null;
        }
        for (Stack<Map<Class<? extends FrameActivity>, Bundle>> stack : activityNode) {
            if (stack != null && stack.size() > 0 && stack.firstElement().containsKey(node)) {
                int currStackIndex = activityNode.indexOf(stack);
                if (currStackIndex - 1 >= 0) {
                    Stack<Map<Class<? extends FrameActivity>, Bundle>> prevStack = activityNode.get(currStackIndex - 1);
                    if (prevStack != null && prevStack.size() > 0) {
                        Map<Class<? extends FrameActivity>, Bundle> map = prevStack.firstElement();
                        for (Class<? extends FrameActivity> n : map.keySet()) {
                            return n;
                        }
                    }
                }
                break;
            }
        }
        return null;
    }

    /**
     * 获取下一个节点
     */
    public synchronized Class<? extends FrameActivity> nextNode(@NonNull Class<? extends FrameActivity> node) {
        if (activityNode.size() <= 1) {
            return null;
        }
        for (Stack<Map<Class<? extends FrameActivity>, Bundle>> stack : activityNode) {
            if (stack != null && stack.size() > 0 && stack.firstElement().containsKey(node)) {
                int currStackIndex = activityNode.indexOf(stack);
                if (currStackIndex + 1 < activityNode.size()) {
                    Stack<Map<Class<? extends FrameActivity>, Bundle>> nextStack = activityNode.get(currStackIndex + 1);
                    if (nextStack != null && nextStack.size() > 0) {
                        Map<Class<? extends FrameActivity>, Bundle> map = nextStack.firstElement();
                        for (Class<? extends FrameActivity> n : map.keySet()) {
                            return n;
                        }
                    }
                }
            }
        }
        return null;
    }
}
