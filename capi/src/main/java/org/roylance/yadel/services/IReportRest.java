package org.roylance.yadel.services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IReportRest {

    @POST("/rest/report/delete-dag")
    Call<String> delete_dag(@Body String request);

    @POST("/rest/report/current")
    Call<String> current(@Body String request);
}