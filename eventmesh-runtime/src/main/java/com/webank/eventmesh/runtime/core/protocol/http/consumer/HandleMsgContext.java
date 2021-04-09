/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webank.eventmesh.runtime.core.protocol.http.consumer;

import com.webank.eventmesh.api.AbstractContext;
import com.webank.eventmesh.runtime.boot.EventMeshHTTPServer;
import com.webank.eventmesh.runtime.core.consumergroup.ConsumerGroupConf;
import com.webank.eventmesh.runtime.core.consumergroup.ConsumerGroupTopicConf;
import com.webank.eventmesh.common.Constants;
import io.openmessaging.api.Message;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HandleMsgContext {

    public Logger messageLogger = LoggerFactory.getLogger("message");

    private String msgRandomNo;

    private String consumerGroup;

    private EventMeshConsumer eventMeshConsumer;

    private String bizSeqNo;

    private String uniqueId;

    private String topic;

    private Message msg;

    private int ttl;

    private long createTime = System.currentTimeMillis();

    private AbstractContext context;

    private ConsumerGroupConf consumerGroupConfig;

    private EventMeshHTTPServer eventMeshHTTPServer;

    private ConsumerGroupTopicConf consumeTopicConfig;

    private Map<String, String> props;

    public HandleMsgContext(String msgRandomNo, String consumerGroup, EventMeshConsumer eventMeshConsumer,
                            String topic, Message msg,
                            AbstractContext context, ConsumerGroupConf consumerGroupConfig,
                            EventMeshHTTPServer eventMeshHTTPServer, String bizSeqNo, String uniqueId, ConsumerGroupTopicConf consumeTopicConfig) {
        this.msgRandomNo = msgRandomNo;
        this.consumerGroup = consumerGroup;
        this.eventMeshConsumer = eventMeshConsumer;
        this.topic = topic;
        this.msg = msg;
        this.context = context;
        this.consumerGroupConfig = consumerGroupConfig;
        this.eventMeshHTTPServer = eventMeshHTTPServer;
        this.bizSeqNo = bizSeqNo;
        this.uniqueId = uniqueId;
        this.consumeTopicConfig = consumeTopicConfig;
        this.ttl = Integer.parseInt(msg.getUserProperties(Constants.PROPERTY_MESSAGE_TIMEOUT));
    }

    public void addProp(String key, String val) {
        if (props == null) {
            props = new HashMap<>();
        }
        props.put(key, val);
    }

    public String getProp(String key) {
        return props.get(key);
    }

    public String getMsgRandomNo() {
        return msgRandomNo;
    }

    public void setMsgRandomNo(String msgRandomNo) {
        this.msgRandomNo = msgRandomNo;
    }

    public ConsumerGroupTopicConf getConsumeTopicConfig() {
        return consumeTopicConfig;
    }

    public void setConsumeTopicConfig(ConsumerGroupTopicConf consumeTopicConfig) {
        this.consumeTopicConfig = consumeTopicConfig;
    }

    public String getBizSeqNo() {
        return bizSeqNo;
    }

    public void setBizSeqNo(String bizSeqNo) {
        this.bizSeqNo = bizSeqNo;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public EventMeshConsumer getEventMeshConsumer() {
        return eventMeshConsumer;
    }

    public void setEventMeshConsumer(EventMeshConsumer eventMeshConsumer) {
        this.eventMeshConsumer = eventMeshConsumer;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Message getMsg() {
        return msg;
    }

    public void setMsg(Message msg) {
        this.msg = msg;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public AbstractContext getContext() {
        return context;
    }

    public void setContext(AbstractContext context) {
        this.context = context;
    }

    public ConsumerGroupConf getConsumerGroupConfig() {
        return consumerGroupConfig;
    }

    public void setConsumerGroupConfig(ConsumerGroupConf consumerGroupConfig) {
        this.consumerGroupConfig = consumerGroupConfig;
    }

    public EventMeshHTTPServer getEventMeshHTTPServer() {
        return eventMeshHTTPServer;
    }

    public void finish() {
        if (eventMeshConsumer != null && context != null && msg != null) {
            if (messageLogger.isDebugEnabled()) {
//                messageLogger.debug("messageAcked|topic={}|msgId={}|cluster={}|broker={}|queueId={}|queueOffset={}", topic,
//                        msg.getMsgId(), msg.getProperty(DeFiBusConstant.PROPERTY_MESSAGE_CLUSTER),
//                        msg.getProperty(DeFiBusConstant.PROPERTY_MESSAGE_BROKER),
//                        msg.getQueueId(), msg.getQueueOffset());
            }
            eventMeshConsumer.updateOffset(topic, Arrays.asList(msg), context);
        }
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("handleMsgContext={")
                .append("consumerGroup=").append(consumerGroup)
                .append(",topic=").append(topic)
                .append(",consumeTopicConfig=").append(consumeTopicConfig)
                .append(",bizSeqNo=").append(bizSeqNo)
                .append(",uniqueId=").append(uniqueId)
                .append(",ttl=").append(ttl)
                .append(",createTime=").append(DateFormatUtils.format(createTime, Constants.DATE_FORMAT)).append("}");
        return sb.toString();
    }

}
