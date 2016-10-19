// THIS FILE WAS AUTO-GENERATED. DO NOT ALTER!
using System.Threading.Tasks;
using Google.Protobuf;

namespace Org.Roylance.Yadel.Api
{
    public class ReportService: IReportService
    {
        private readonly IHttpExecute httpExecute;
        public ReportService(IHttpExecute httpExecute)
        {
            this.httpExecute = httpExecute;
        }

        public async Task<Org.Roylance.Yadel.UIYadelResponse> delete_dag(Org.Roylance.Yadel.UIYadelRequest request)
        {
            var base64request = System.Convert.ToBase64String(request.ToByteArray());
            var responseCall = await this.httpExecute.PostAsync("/rest/report/delete-dag", base64request);
            var bytes = System.Convert.FromBase64String(responseCall);
            return Org.Roylance.Yadel.UIYadelResponse.Parser.ParseFrom(bytes);
        }

        public async Task<Org.Roylance.Yadel.UIYadelResponse> current(Org.Roylance.Yadel.UIYadelRequest request)
        {
            var base64request = System.Convert.ToBase64String(request.ToByteArray());
            var responseCall = await this.httpExecute.PostAsync("/rest/report/current", base64request);
            var bytes = System.Convert.FromBase64String(responseCall);
            return Org.Roylance.Yadel.UIYadelResponse.Parser.ParseFrom(bytes);
        }

        public async Task<Org.Roylance.Yadel.UIYadelResponse> get_dag_status(Org.Roylance.Yadel.UIYadelRequest request)
        {
            var base64request = System.Convert.ToBase64String(request.ToByteArray());
            var responseCall = await this.httpExecute.PostAsync("/rest/report/get-dag-status", base64request);
            var bytes = System.Convert.FromBase64String(responseCall);
            return Org.Roylance.Yadel.UIYadelResponse.Parser.ParseFrom(bytes);
        }
	}
}