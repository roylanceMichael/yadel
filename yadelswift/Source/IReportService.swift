// THIS FILE WAS AUTO-GENERATED. DO NOT ALTER!
import Foundation

public protocol IReportService {
	func delete_dag(request: Org_Roylance_Yadel_UIYadelRequest, onSuccess: @escaping (_ response: Org_Roylance_Yadel_UIYadelResponse) -> Void, onError: @escaping (_ response: String) -> Void)
	func current(request: Org_Roylance_Yadel_UIYadelRequest, onSuccess: @escaping (_ response: Org_Roylance_Yadel_UIYadelResponse) -> Void, onError: @escaping (_ response: String) -> Void)
	func get_dag_status(request: Org_Roylance_Yadel_UIYadelRequest, onSuccess: @escaping (_ response: Org_Roylance_Yadel_UIYadelResponse) -> Void, onError: @escaping (_ response: String) -> Void)
}