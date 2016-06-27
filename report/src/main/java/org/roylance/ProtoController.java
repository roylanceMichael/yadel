package org.roylance;

import org.roylance.yadel.api.services.GetYadelReportsModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;

@Path("/proto")
public class ProtoController {
    @Context
    private ServletContext context;
    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;

    @GET
    @Path("/message")
    public void single(@Suspended final AsyncResponse asyncResponse) {
        new Thread(() -> {
            asyncResponse.resume(new GetYadelReportsModel().build());
        }).start();
    }
}
