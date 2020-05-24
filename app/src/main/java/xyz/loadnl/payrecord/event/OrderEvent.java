package xyz.loadnl.payrecord.event;

import xyz.loadnl.payrecord.model.Response;

public class OrderEvent {
    private Response response;


    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
