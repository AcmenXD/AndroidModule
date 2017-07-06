package com.acmenxd.frame.basis;

import android.os.Bundle;
import android.support.annotation.NonNull;

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
    public void skipTop() {
        skipNode(firstNode);
    }

    /**
     * 跳到指定节点
     */
    public Bundle skipNode(@NonNull Class<? extends FrameActivity> node) {
        boolean isSkip = false;
        Bundle bundle = null;
        Stack<Stack<Map<Class<? extends FrameActivity>, Bundle>>> skipStack = new Stack<>();
        for (Stack<Map<Class<? extends FrameActivity>, Bundle>> stack : activityNode) {
            if (stack.firstElement().containsKey(node)) {
                isSkip = true;
                bundle = stack.firstElement().get(node);
            }
            if (isSkip) {
                skipStack.add(stack);
                if (stack != null && stack.size() > 0 && stack.firstElement().containsKey(firstNode)) {

                } else {
                    activityNode.remove(stack);
                }
            }
        }
        for (int i = 0, len = skipStack.size(); i < len; i++) {
            Stack<Map<Class<? extends FrameActivity>, Bundle>> stack = skipStack.lastElement();
            if (stack != null && stack.size() > 0) {
                for (int j = 0, len2 = stack.size(); j < len2; j++) {
                    Map<Class<? extends FrameActivity>, Bundle> map = stack.lastElement();
                    if (map != null && map.size() > 0) {
                        for (Class<? extends FrameActivity> n : map.keySet())
                            if (n != firstNode) {
                                ActivityStackManager.INSTANCE.finishActivity(n);
                            }
                    }
                }
            }
            skipStack.remove(stack);
        }
        return bundle;
    }

    /**
     * 跳到指定节点,并启动节点的Activity
     */
    public void skipAndStartNode(@NonNull Class<? extends FrameActivity> node) {
        Bundle bundle = skipNode(node);
        if (node != firstNode) {
            ActivityStackManager.INSTANCE.currentActivity().startActivity(node, bundle);
        }
    }

    /**
     * 添加一个节点
     */
    public void addFirstNode(@NonNull Class<? extends FrameActivity> node) {
        firstNode = node;
        addNode(node, null);
    }

    /**
     * 添加一个节点
     */
    public void addNode(@NonNull Class<? extends FrameActivity> node, Bundle pBundle) {
        if (pBundle == null) {
            pBundle = new Bundle();
        }
        Stack<Map<Class<? extends FrameActivity>, Bundle>> stack = new Stack<>();
        Map<Class<? extends FrameActivity>, Bundle> map = new ConcurrentHashMap<>();
        map.put(node, pBundle);
        stack.add(map);
        activityNode.add(stack);
    }

    /**
     * 添加一个child
     */
    protected void addChild(@NonNull Class<? extends FrameActivity> child, @NonNull Bundle pBundle) {
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
    public void removeNode(@NonNull Class<? extends FrameActivity> node) {
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
    public Class<? extends FrameActivity> currentNode() {
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
    public boolean isCurrentNode(@NonNull Class<? extends FrameActivity> node) {
        return node == currentNode();
    }

    /**
     * 获取前一个节点
     */
    public Class<? extends FrameActivity> prevNode(@NonNull Class<? extends FrameActivity> node) {
        for (Stack<Map<Class<? extends FrameActivity>, Bundle>> stack : activityNode) {
            if (stack != null && stack.size() > 0 && stack.firstElement().containsKey(node) && stack != activityNode.firstElement()) {
                Stack<Map<Class<? extends FrameActivity>, Bundle>> prevStack = activityNode.get(activityNode.lastIndexOf(stack) - 1);
                if (prevStack != null && prevStack.size() > 0) {
                    Map<Class<? extends FrameActivity>, Bundle> map = prevStack.firstElement();
                    for (Class<? extends FrameActivity> n : map.keySet()) {
                        return n;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取下一个节点
     */
    public Class<? extends FrameActivity> nextNode(@NonNull Class<? extends FrameActivity> node) {
        for (Stack<Map<Class<? extends FrameActivity>, Bundle>> stack : activityNode) {
            if (stack != null && stack.size() > 0 && stack.firstElement().containsKey(node) && stack != activityNode.lastElement()) {
                Stack<Map<Class<? extends FrameActivity>, Bundle>> prevStack = activityNode.get(activityNode.lastIndexOf(stack) + 1);
                if (prevStack != null && prevStack.size() > 0) {
                    Map<Class<? extends FrameActivity>, Bundle> map = prevStack.firstElement();
                    for (Class<? extends FrameActivity> n : map.keySet()) {
                        return n;
                    }
                }
            }
        }
        return null;
    }
}
