package com.example.mycomic.data.entity;



public class HttpResult<T>{
    public int status;
    public T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DBSearchResult{" +
                "status=" + status +
                ", data=" + data +
                '}';
    }
}