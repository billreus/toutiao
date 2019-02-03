package com.nowcode.toutiao.async;

import java.util.HashMap;
import java.util.Map;

public class EventModel {
    private EventType type;
    private int actorId;//触发id
    private int entiyType;//触发对象
    private int entiyId;//触发对象
    private int entityOwnerId;//对象拥有者
    private Map<String, String> exts = new HashMap<String, String>();

    public String getExt(String key){
        return exts.get(key);
    }
    public EventModel setExt(String key, String value){
        exts.put(key, value);
        return this;
    }

    public EventModel(EventType type){
        this.type = type;
    }
    public EventModel(){//json需要默认构造函数

    }


    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntiyType() {
        return entiyType;
    }

    public EventModel setEntiyType(int entiyType) {
        this.entiyType = entiyType;
        return this;
    }

    public int getEntiyId() {
        return entiyId;
    }

    public EventModel setEntiyId(int entiyId) {
        this.entiyId = entiyId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public void setExts(Map<String, String> exts) {
        this.exts = exts;
    }
}
