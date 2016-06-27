package org.roylance;

import akka.dispatch.OnComplete;
import akka.pattern.Patterns;
import akka.util.Timeout;
import org.apache.commons.codec.binary.Base64;
import org.roylance.yadel.api.models.YadelReports;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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

    @POST
    @Path("/delete/{id}")
    public void delete(@Suspended final AsyncResponse asyncResponse, @PathParam("id") final String id) {
        new Thread(() -> {
            System.out.println(id);
            final YadelReports.UIRequest request = YadelReports.UIRequest.newBuilder()
                    .setRequestType(YadelReports.UIRequests.DELETE_DAG)
                    .setDagId(id).build();

            final Future<Object> future = Patterns.ask(
                    ActorSingleton.getManager(),
                    request,
                    this.create500Seconds());

            future.onComplete(new OnComplete<Object>() {
                @Override
                public void onComplete(Throwable failure, Object success) throws Throwable {
                    System.out.println(failure);
                    System.out.println(success);
                    asyncResponse.resume("complete");
                }
            }, ActorSingleton.getActorSystem().dispatcher());
        }).start();
    }

    @GET
    @Path("/current")
    public void current(@Suspended final AsyncResponse asyncResponse) {
        new Thread(() -> {
            final Future<Object> future = Patterns.ask(
                    ActorSingleton.getManager(),
                    YadelReports.UIRequests.REPORT_DAGS,
                    this.create500Seconds());

            future.onComplete(new OnComplete<Object>() {
                @Override
                public void onComplete(Throwable failure, Object success) throws Throwable {
                    if (success instanceof YadelReports.UIDagReport) {
                        final YadelReports.UIDagReport dagReport = (YadelReports.UIDagReport) success;
                        final String base64String = Base64.encodeBase64String(dagReport.toByteArray());
                        asyncResponse.resume(base64String);
                    }
                    else {
                        asyncResponse.resume(failure);
                    }
                }
            }, ActorSingleton.getActorSystem().dispatcher());
        }).start();
    }

    private Timeout create500Seconds() {
        return new Timeout(Duration.create(500, "seconds"));
    }
}
