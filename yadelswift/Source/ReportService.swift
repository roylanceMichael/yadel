// THIS FILE WAS AUTO-GENERATED. DO NOT ALTER!
import Foundation
import Alamofire
import SwiftProtobuf

public class ReportService: IReportService {
    let baseUrl: String
    public init(baseUrl: String) {
        self.baseUrl = baseUrl
    }
	public func delete_dag(request: Org_Roylance_Yadel_UIYadelRequest, onSuccess: @escaping (_ response: Org_Roylance_Yadel_UIYadelResponse) -> Void, onError: @escaping (_ response: String) -> Void) {

            do {
                let serializedRequest = try request.serializedData()
                var urlRequest = URLRequest(url: URL(string: self.baseUrl + "/rest/report/delete-dag")!)
                urlRequest.httpMethod = HTTPMethod.post.rawValue
                urlRequest.setValue("application/json", forHTTPHeaderField: "Content-Type")
                urlRequest.httpBody = serializedRequest.base64EncodedData()

                Alamofire.request(urlRequest)
                    .response { alamoResponse in
                        let base64String = String(data: alamoResponse.data!, encoding: String.Encoding.utf8)
                        let decodedData = Data(base64Encoded: base64String!)!
                        do {
                            let actualResponse = try Org_Roylance_Yadel_UIYadelResponse(serializedData: decodedData)
                            onSuccess(actualResponse)
                        }
                        catch {
                            onError("\(error)")
                        }
                    }
                }
            catch {
                onError("\(error)")
            }
	}
	public func current(request: Org_Roylance_Yadel_UIYadelRequest, onSuccess: @escaping (_ response: Org_Roylance_Yadel_UIYadelResponse) -> Void, onError: @escaping (_ response: String) -> Void) {

            do {
                let serializedRequest = try request.serializedData()
                var urlRequest = URLRequest(url: URL(string: self.baseUrl + "/rest/report/current")!)
                urlRequest.httpMethod = HTTPMethod.post.rawValue
                urlRequest.setValue("application/json", forHTTPHeaderField: "Content-Type")
                urlRequest.httpBody = serializedRequest.base64EncodedData()

                Alamofire.request(urlRequest)
                    .response { alamoResponse in
                        let base64String = String(data: alamoResponse.data!, encoding: String.Encoding.utf8)
                        let decodedData = Data(base64Encoded: base64String!)!
                        do {
                            let actualResponse = try Org_Roylance_Yadel_UIYadelResponse(serializedData: decodedData)
                            onSuccess(actualResponse)
                        }
                        catch {
                            onError("\(error)")
                        }
                    }
                }
            catch {
                onError("\(error)")
            }
	}
	public func get_dag_status(request: Org_Roylance_Yadel_UIYadelRequest, onSuccess: @escaping (_ response: Org_Roylance_Yadel_UIYadelResponse) -> Void, onError: @escaping (_ response: String) -> Void) {

            do {
                let serializedRequest = try request.serializedData()
                var urlRequest = URLRequest(url: URL(string: self.baseUrl + "/rest/report/get-dag-status")!)
                urlRequest.httpMethod = HTTPMethod.post.rawValue
                urlRequest.setValue("application/json", forHTTPHeaderField: "Content-Type")
                urlRequest.httpBody = serializedRequest.base64EncodedData()

                Alamofire.request(urlRequest)
                    .response { alamoResponse in
                        let base64String = String(data: alamoResponse.data!, encoding: String.Encoding.utf8)
                        let decodedData = Data(base64Encoded: base64String!)!
                        do {
                            let actualResponse = try Org_Roylance_Yadel_UIYadelResponse(serializedData: decodedData)
                            onSuccess(actualResponse)
                        }
                        catch {
                            onError("\(error)")
                        }
                    }
                }
            catch {
                onError("\(error)")
            }
	}
}