import base64
import requests
import yadel_report_pb2


class ReportService(object):
    def __init__(self, base_url):
        self.base_url = base_url

	def delete_dag(request):
		base64_request = base64.b64encode(request.SerializeToString())
		response_call = requests.post(self.base_url + '/rest/report/delete-dag', data = base64_request)
		response = yadel_report_pb2.UIYadelResponse()
		response.ParseFromString(base64.b64decode(response_call.text))
		return response

	def current(request):
		base64_request = base64.b64encode(request.SerializeToString())
		response_call = requests.post(self.base_url + '/rest/report/current', data = base64_request)
		response = yadel_report_pb2.UIYadelResponse()
		response.ParseFromString(base64.b64decode(response_call.text))
		return response


