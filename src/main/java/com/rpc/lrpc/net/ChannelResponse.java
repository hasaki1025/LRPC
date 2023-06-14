package com.rpc.lrpc.net;

import com.rpc.lrpc.message.Content.Response.ResponseContent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ChannelResponse {

    private final Map<Integer,ResponseAction> waitingMap=new ConcurrentHashMap<>();
    private final Map<Integer, ResponseContent> resultMap=new ConcurrentHashMap<>();


    /**
     * 获取正在等待响应的action
     * @param seq 请求序号
     * @return 响应action
     */
    public ResponseAction getResponseAction(int seq)
    {
        return waitingMap.get(seq);
    }

    /**
     * 等待response并得到response后返回结果
     * @param seq 等待的request的序号
     * @param waitingTime 最长等待时间
     * @return 返回responseContent
     */
    public ResponseContent lockAndGetResponse(int seq, long waitingTime)  {
        try
        {
            addWaitRequest(seq,new CallResponseAction<>());
            synchronized (waitingMap.get(seq)) {
                waitingMap.get(seq).wait(waitingTime);
                ResponseContent content = resultMap.get(seq);
                //唤醒后自动删除,此时action已被删除
                resultMap.remove(seq);
                return content;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新增等待请求
     * @param seq 请求序号
     */
    public void addWaitRequest(int seq)
    {
        waitingMap.putIfAbsent(seq,new ResponseAction());
    }

    public void addWaitRequest(int seq,ResponseAction action)
    {
        waitingMap.putIfAbsent(seq,action);
    }

    /**
     * 检查某个请求是否仍在等待
     * @param seq 等待序号
     * @return bool值返回
     */
    public boolean stillWaiting(int seq)
    {
        return waitingMap.containsKey(seq);
    }

    /**
     * 获取响应的内容
     * @param seq 请求序号
     * @return ResponseContent
     */
    public ResponseContent getResponseContent(int seq)
    {
        return resultMap.get(seq);
    }

    public void putResponseContent(int seq,ResponseContent content)
    {
        resultMap.put(seq,content);
    }

    public void removeResponseContent(int seq)
    {
        resultMap.remove(seq);
    }

    public void removeResponseAction(int seq)
    {
        ResponseAction action = waitingMap.get(seq);
        if (action.hasThreadWaiting())
        {
            synchronized (waitingMap.get(seq))
            {
                action.notifyAll();
            }
        }
        waitingMap.remove(seq);
    }





}
