package com.acmenxd.frame.basis;

import android.support.annotation.NonNull;

import java.util.Stack;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/6/20 11:45
 * @detail Activity节点管理器
 */
public enum ActivityNodeManager {
    INSTANCE;
    private static Stack<Stack<Class<? extends FrameActivity>>> activityNode = new Stack<>();

    /**
     * 跳到指定节点
     */
    public void skipNode(@NonNull Class<? extends FrameActivity> node) {
        boolean isSkip = false;
        Stack<Stack<Class<? extends FrameActivity>>> skipStack = new Stack<>();
        for (Stack<Class<? extends FrameActivity>> stack : activityNode) {
            if (stack.firstElement() == node) {
                isSkip = true;
            }
            if (isSkip) {
                skipStack.add(stack);
                activityNode.remove(stack);
            }
        }
        for (int i = 0, len = skipStack.size(); i < len; i++) {
            Stack<Class<? extends FrameActivity>> stack = skipStack.lastElement();
            if (stack != null && stack.size() > 0) {
                for (int j = 0, len2 = stack.size(); j < len2; j++) {
                    Class<? extends FrameActivity> temp = stack.lastElement();
                    if (temp != null) {
                        ActivityStackManager.INSTANCE.finishActivity(temp);
                    }
                }
            }
        }
    }

    /**
     * 添加一个节点
     */
    public void addNode(@NonNull Class<? extends FrameActivity> node) {
        Stack<Class<? extends FrameActivity>> stack = new Stack<>();
        stack.add(node);
        activityNode.add(stack);
    }

    /**
     * 添加一个child
     */
    protected void addChild(@NonNull Class<? extends FrameActivity> child) {
        Stack<Class<? extends FrameActivity>> stack = activityNode.lastElement();
        if (stack != null && stack.size() > 0 && stack.firstElement() != child) {
            stack.add(child);
        }
    }

    /**
     * 移除一个节点
     */
    public void removeNode(Class<? extends FrameActivity> node) {
        Stack<Class<? extends FrameActivity>> removeStack = null;
        Stack<Class<? extends FrameActivity>> prevStack = null;
        for (Stack<Class<? extends FrameActivity>> stack : activityNode) {
            if (stack.firstElement() == node) {
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
        Stack<Class<? extends FrameActivity>> stack = activityNode.lastElement();
        if (stack != null && stack.size() > 0) {
            return stack.firstElement();
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
        for (Stack<Class<? extends FrameActivity>> stack : activityNode) {
            if (stack != null && stack.size() > 0 && stack.firstElement() == node && stack != activityNode.firstElement()) {
                Stack<Class<? extends FrameActivity>> prevStack = activityNode.get(activityNode.lastIndexOf(stack) - 1);
                if (prevStack != null && prevStack.size() > 0) {
                    return prevStack.firstElement();
                }
            }
        }
        return null;
    }

    /**
     * 获取下一个节点
     */
    public Class<? extends FrameActivity> nextNode(@NonNull Class<? extends FrameActivity> node) {
        for (Stack<Class<? extends FrameActivity>> stack : activityNode) {
            if (stack != null && stack.size() > 0 && stack.firstElement() == node && stack != activityNode.lastElement()) {
                Stack<Class<? extends FrameActivity>> prevStack = activityNode.get(activityNode.lastIndexOf(stack) + 1);
                if (prevStack != null && prevStack.size() > 0) {
                    return prevStack.firstElement();
                }
            }
        }
        return null;
    }
}
