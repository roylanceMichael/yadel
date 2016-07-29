package org.roylance.yadel.rest;

import org.roylance.common.service.IProtoSerializerService;

import org.roylance.yadel.utilities.ServiceLocator;
import org.roylance.yadel.services.IReportService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
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

    private final IReportService reportService;
    private final IProtoSerializerService serializerService;

    public ReportController() {
        this.serializerService = ServiceLocator.INSTANCE.getProtobufSerializerService();
        this.reportService = ServiceLocator.INSTANCE.getReportService();
    }

    @POST
    @Path("/delete-dag")
    public void delete_dag(@Suspended AsyncResponse asyncResponse, String request) throws Exception {
        new Thread(() -> {
            
            final org.roylance.yadel.YadelReport.UIRequest requestActual =
                    this.serializerService.deserializeFromBase64(request, org.roylance.yadel.YadelReport.UIRequest.getDefaultInstance());

            final org.roylance.yadel.YadelReport.UIResponse response = this.reportService.delete_dag(requestActual);
            final String deserializeResponse = this.serializerService.serializeToBase64(response);
            asyncResponse.resume(deserializeResponse);

        }).start();
    }

    @POST
    @Path("/current")
    public void current(@Suspended AsyncResponse asyncResponse, String request) throws Exception {
        new Thread(() -> {
            
            final org.roylance.yadel.YadelReport.UIRequest requestActual =
                    this.serializerService.deserializeFromBase64(request, org.roylance.yadel.YadelReport.UIRequest.getDefaultInstance());

            final org.roylance.yadel.YadelReport.UIResponse response = this.reportService.current(requestActual);
            final String deserializeResponse = this.serializerService.serializeToBase64(response);
            asyncResponse.resume(deserializeResponse);

        }).start();
    }
}