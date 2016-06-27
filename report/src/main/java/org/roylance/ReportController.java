package org.roylance;

import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import akka.util.Timeout;
import org.roylance.yadel.api.models.YadelReports;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;

@Path("/report")
public class ReportController {
    @Context
    private ServletContext context;
    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;

    @GET
    @Path("/current")
    public void current(@Suspended final AsyncResponse asyncResponse) {
        new Thread(() -> {
            final Timeout timeout = new Timeout(Duration.create(500, "seconds"));
            final Future<Object> future = Patterns.ask(
                    ActorSingleton.getManager("127.0.0.1"),
                    YadelReports.UIRequests.REPORT_DAGS,
                    timeout);

            future.onSuccess(new OnSuccess<Object>() {
                                 @Override
                                 public void onSuccess(Object result) throws Throwable {
                                    asyncResponse.resume(result);
                                 }
                             },
                    ActorSingleton.getActorSystem().dispatcher());
        }).start();
    }
}
