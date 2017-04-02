/*
 * DO NOT EDIT.
 *
 * Generated by the protocol buffer compiler.
 * Source: yadel_report.proto
 *
 */

import Foundation
import SwiftProtobuf

// If the compiler emits an error on this type, it is because this file
// was generated by a version of the `protoc` Swift plug-in that is
// incompatible with the version of SwiftProtobuf to which you are linking.
// Please ensure that your are building against the same version of the API
// that was used to generate this file.
fileprivate struct _GeneratedWithProtocGenSwiftVersion: SwiftProtobuf.ProtobufAPIVersionCheck {
  struct _1: SwiftProtobuf.ProtobufAPIVersion_1 {}
  typealias Version = _1
}

fileprivate let _protobuf_package = "org.roylance.yadel"

public enum Org_Roylance_Yadel_UIYadelRequestType: SwiftProtobuf.Enum, SwiftProtobuf._ProtoNameProviding {
  public typealias RawValue = Int
  case reportDags // = 0
  case deleteDag // = 1
  case getDagStatus // = 2
  case reportDagsActive // = 3
  case UNRECOGNIZED(Int)

  public static let _protobuf_nameMap: SwiftProtobuf._NameMap = [
    0: .same(proto: "REPORT_DAGS"),
    1: .same(proto: "DELETE_DAG"),
    2: .same(proto: "GET_DAG_STATUS"),
    3: .same(proto: "REPORT_DAGS_ACTIVE"),
  ]

  public init() {
    self = .reportDags
  }

  public init?(rawValue: Int) {
    switch rawValue {
    case 0: self = .reportDags
    case 1: self = .deleteDag
    case 2: self = .getDagStatus
    case 3: self = .reportDagsActive
    default: self = .UNRECOGNIZED(rawValue)
    }
  }

  public var rawValue: Int {
    switch self {
    case .reportDags: return 0
    case .deleteDag: return 1
    case .getDagStatus: return 2
    case .reportDagsActive: return 3
    case .UNRECOGNIZED(let i): return i
    }
  }

}

public enum Org_Roylance_Yadel_UIWorkerState: SwiftProtobuf.Enum, SwiftProtobuf._ProtoNameProviding {
  public typealias RawValue = Int
  case currentlyWorking // = 0
  case currentlyIdle // = 1
  case UNRECOGNIZED(Int)

  public static let _protobuf_nameMap: SwiftProtobuf._NameMap = [
    0: .same(proto: "CURRENTLY_WORKING"),
    1: .same(proto: "CURRENTLY_IDLE"),
  ]

  public init() {
    self = .currentlyWorking
  }

  public init?(rawValue: Int) {
    switch rawValue {
    case 0: self = .currentlyWorking
    case 1: self = .currentlyIdle
    default: self = .UNRECOGNIZED(rawValue)
    }
  }

  public var rawValue: Int {
    switch self {
    case .currentlyWorking: return 0
    case .currentlyIdle: return 1
    case .UNRECOGNIZED(let i): return i
    }
  }

}

public struct Org_Roylance_Yadel_UIYadelRequest: SwiftProtobuf.Message, SwiftProtobuf._MessageImplementationBase, SwiftProtobuf._ProtoNameProviding {
  public static let protoMessageName: String = _protobuf_package + ".UIYadelRequest"
  public static let _protobuf_nameMap: SwiftProtobuf._NameMap = [
    1: .standard(proto: "request_type"),
    2: .standard(proto: "dag_id"),
    3: .same(proto: "token"),
    4: .standard(proto: "user_name"),
    5: .same(proto: "password"),
  ]

  public var requestType: Org_Roylance_Yadel_UIYadelRequestType = Org_Roylance_Yadel_UIYadelRequestType.reportDags

  public var dagId: String = ""

  public var token: String = ""

  public var userName: String = ""

  public var password: String = ""

  public var unknownFields = SwiftProtobuf.UnknownStorage()

  public init() {}

  public mutating func decodeMessage<D: SwiftProtobuf.Decoder>(decoder: inout D) throws {
    while let fieldNumber = try decoder.nextFieldNumber() {
      switch fieldNumber {
      case 1: try decoder.decodeSingularEnumField(value: &requestType)
      case 2: try decoder.decodeSingularStringField(value: &dagId)
      case 3: try decoder.decodeSingularStringField(value: &token)
      case 4: try decoder.decodeSingularStringField(value: &userName)
      case 5: try decoder.decodeSingularStringField(value: &password)
      default: break
      }
    }
  }

  public func traverse<V: SwiftProtobuf.Visitor>(visitor: inout V) throws {
    if requestType != Org_Roylance_Yadel_UIYadelRequestType.reportDags {
      try visitor.visitSingularEnumField(value: requestType, fieldNumber: 1)
    }
    if !dagId.isEmpty {
      try visitor.visitSingularStringField(value: dagId, fieldNumber: 2)
    }
    if !token.isEmpty {
      try visitor.visitSingularStringField(value: token, fieldNumber: 3)
    }
    if !userName.isEmpty {
      try visitor.visitSingularStringField(value: userName, fieldNumber: 4)
    }
    if !password.isEmpty {
      try visitor.visitSingularStringField(value: password, fieldNumber: 5)
    }
    try unknownFields.traverse(visitor: &visitor)
  }

  public func _protobuf_generated_isEqualTo(other: Org_Roylance_Yadel_UIYadelRequest) -> Bool {
    if requestType != other.requestType {return false}
    if dagId != other.dagId {return false}
    if token != other.token {return false}
    if userName != other.userName {return false}
    if password != other.password {return false}
    if unknownFields != other.unknownFields {return false}
    return true
  }
}

public struct Org_Roylance_Yadel_UIYadelResponse: SwiftProtobuf.Message, SwiftProtobuf._MessageImplementationBase, SwiftProtobuf._ProtoNameProviding {
  public static let protoMessageName: String = _protobuf_package + ".UIYadelResponse"
  public static let _protobuf_nameMap: SwiftProtobuf._NameMap = [
    1: .same(proto: "content"),
    2: .same(proto: "report"),
    3: .same(proto: "dag"),
    4: .same(proto: "configurations"),
  ]

  private class _StorageClass {
    var _content: String = ""
    var _report: Org_Roylance_Yadel_UIDagReport? = nil
    var _dag: Org_Roylance_Yadel_UIDag? = nil
    var _configurations: [Org_Roylance_Yadel_UIWorkerConfiguration] = []

    init() {}

    init(copying source: _StorageClass) {
      _content = source._content
      _report = source._report
      _dag = source._dag
      _configurations = source._configurations
    }
  }

  private var _storage = _StorageClass()

  private mutating func _uniqueStorage() -> _StorageClass {
    if !isKnownUniquelyReferenced(&_storage) {
      _storage = _StorageClass(copying: _storage)
    }
    return _storage
  }

  public var content: String {
    get {return _storage._content}
    set {_uniqueStorage()._content = newValue}
  }

  public var report: Org_Roylance_Yadel_UIDagReport {
    get {return _storage._report ?? Org_Roylance_Yadel_UIDagReport()}
    set {_uniqueStorage()._report = newValue}
  }
  public var hasReport: Bool {
    return _storage._report != nil
  }
  public mutating func clearReport() {
    return _storage._report = nil
  }

  public var dag: Org_Roylance_Yadel_UIDag {
    get {return _storage._dag ?? Org_Roylance_Yadel_UIDag()}
    set {_uniqueStorage()._dag = newValue}
  }
  public var hasDag: Bool {
    return _storage._dag != nil
  }
  public mutating func clearDag() {
    return _storage._dag = nil
  }

  public var configurations: [Org_Roylance_Yadel_UIWorkerConfiguration] {
    get {return _storage._configurations}
    set {_uniqueStorage()._configurations = newValue}
  }

  public var unknownFields = SwiftProtobuf.UnknownStorage()

  public init() {}

  public mutating func decodeMessage<D: SwiftProtobuf.Decoder>(decoder: inout D) throws {
    _ = _uniqueStorage()
    try withExtendedLifetime(_storage) { (_storage: _StorageClass) in
      while let fieldNumber = try decoder.nextFieldNumber() {
        switch fieldNumber {
        case 1: try decoder.decodeSingularStringField(value: &_storage._content)
        case 2: try decoder.decodeSingularMessageField(value: &_storage._report)
        case 3: try decoder.decodeSingularMessageField(value: &_storage._dag)
        case 4: try decoder.decodeRepeatedMessageField(value: &_storage._configurations)
        default: break
        }
      }
    }
  }

  public func traverse<V: SwiftProtobuf.Visitor>(visitor: inout V) throws {
    try withExtendedLifetime(_storage) { (_storage: _StorageClass) in
      if !_storage._content.isEmpty {
        try visitor.visitSingularStringField(value: _storage._content, fieldNumber: 1)
      }
      if let v = _storage._report {
        try visitor.visitSingularMessageField(value: v, fieldNumber: 2)
      }
      if let v = _storage._dag {
        try visitor.visitSingularMessageField(value: v, fieldNumber: 3)
      }
      if !_storage._configurations.isEmpty {
        try visitor.visitRepeatedMessageField(value: _storage._configurations, fieldNumber: 4)
      }
    }
    try unknownFields.traverse(visitor: &visitor)
  }

  public func _protobuf_generated_isEqualTo(other: Org_Roylance_Yadel_UIYadelResponse) -> Bool {
    if _storage !== other._storage {
      let storagesAreEqual: Bool = withExtendedLifetime((_storage, other._storage)) { (_storage, other_storage) in
        if _storage._content != other_storage._content {return false}
        if _storage._report != other_storage._report {return false}
        if _storage._dag != other_storage._dag {return false}
        if _storage._configurations != other_storage._configurations {return false}
        return true
      }
      if !storagesAreEqual {return false}
    }
    if unknownFields != other.unknownFields {return false}
    return true
  }
}

public struct Org_Roylance_Yadel_UIDagReport: SwiftProtobuf.Message, SwiftProtobuf._MessageImplementationBase, SwiftProtobuf._ProtoNameProviding {
  public static let protoMessageName: String = _protobuf_package + ".UIDagReport"
  public static let _protobuf_nameMap: SwiftProtobuf._NameMap = [
    1: .same(proto: "workers"),
    2: .same(proto: "dags"),
    3: .standard(proto: "used_manager_memory"),
    4: .standard(proto: "total_manager_memory"),
    5: .standard(proto: "active_dags"),
    6: .standard(proto: "unprocessed_dags"),
    7: .standard(proto: "saved_dags"),
  ]

  public var workers: [Org_Roylance_Yadel_UIWorkerConfiguration] = []

  public var dags: [Org_Roylance_Yadel_UIDag] = []

  public var usedManagerMemory: String = ""

  public var totalManagerMemory: String = ""

  public var activeDags: Int32 = 0

  public var unprocessedDags: Int32 = 0

  public var savedDags: Int32 = 0

  public var unknownFields = SwiftProtobuf.UnknownStorage()

  public init() {}

  public mutating func decodeMessage<D: SwiftProtobuf.Decoder>(decoder: inout D) throws {
    while let fieldNumber = try decoder.nextFieldNumber() {
      switch fieldNumber {
      case 1: try decoder.decodeRepeatedMessageField(value: &workers)
      case 2: try decoder.decodeRepeatedMessageField(value: &dags)
      case 3: try decoder.decodeSingularStringField(value: &usedManagerMemory)
      case 4: try decoder.decodeSingularStringField(value: &totalManagerMemory)
      case 5: try decoder.decodeSingularInt32Field(value: &activeDags)
      case 6: try decoder.decodeSingularInt32Field(value: &unprocessedDags)
      case 7: try decoder.decodeSingularInt32Field(value: &savedDags)
      default: break
      }
    }
  }

  public func traverse<V: SwiftProtobuf.Visitor>(visitor: inout V) throws {
    if !workers.isEmpty {
      try visitor.visitRepeatedMessageField(value: workers, fieldNumber: 1)
    }
    if !dags.isEmpty {
      try visitor.visitRepeatedMessageField(value: dags, fieldNumber: 2)
    }
    if !usedManagerMemory.isEmpty {
      try visitor.visitSingularStringField(value: usedManagerMemory, fieldNumber: 3)
    }
    if !totalManagerMemory.isEmpty {
      try visitor.visitSingularStringField(value: totalManagerMemory, fieldNumber: 4)
    }
    if activeDags != 0 {
      try visitor.visitSingularInt32Field(value: activeDags, fieldNumber: 5)
    }
    if unprocessedDags != 0 {
      try visitor.visitSingularInt32Field(value: unprocessedDags, fieldNumber: 6)
    }
    if savedDags != 0 {
      try visitor.visitSingularInt32Field(value: savedDags, fieldNumber: 7)
    }
    try unknownFields.traverse(visitor: &visitor)
  }

  public func _protobuf_generated_isEqualTo(other: Org_Roylance_Yadel_UIDagReport) -> Bool {
    if workers != other.workers {return false}
    if dags != other.dags {return false}
    if usedManagerMemory != other.usedManagerMemory {return false}
    if totalManagerMemory != other.totalManagerMemory {return false}
    if activeDags != other.activeDags {return false}
    if unprocessedDags != other.unprocessedDags {return false}
    if savedDags != other.savedDags {return false}
    if unknownFields != other.unknownFields {return false}
    return true
  }
}

public struct Org_Roylance_Yadel_UIDag: SwiftProtobuf.Message, SwiftProtobuf._MessageImplementationBase, SwiftProtobuf._ProtoNameProviding {
  public static let protoMessageName: String = _protobuf_package + ".UIDag"
  public static let _protobuf_nameMap: SwiftProtobuf._NameMap = [
    1: .same(proto: "id"),
    2: .same(proto: "display"),
    3: .same(proto: "nodes"),
    4: .same(proto: "edges"),
    5: .standard(proto: "is_completed"),
    6: .standard(proto: "is_processing"),
    7: .standard(proto: "is_error"),
    8: .same(proto: "logs"),
    9: .standard(proto: "number_completed"),
    10: .standard(proto: "number_processing"),
    11: .standard(proto: "number_errored"),
    12: .standard(proto: "number_unprocessed"),
    13: .same(proto: "children"),
  ]

  public var id: String = ""

  public var display: String = ""

  public var nodes: [Org_Roylance_Yadel_UINode] = []

  public var edges: [Org_Roylance_Yadel_UIEdge] = []

  public var isCompleted: Bool = false

  public var isProcessing: Bool = false

  public var isError: Bool = false

  public var logs: [String] = []

  public var numberCompleted: Int32 = 0

  public var numberProcessing: Int32 = 0

  public var numberErrored: Int32 = 0

  public var numberUnprocessed: Int32 = 0

  public var children: [Org_Roylance_Yadel_UIDag] = []

  public var unknownFields = SwiftProtobuf.UnknownStorage()

  public init() {}

  public mutating func decodeMessage<D: SwiftProtobuf.Decoder>(decoder: inout D) throws {
    while let fieldNumber = try decoder.nextFieldNumber() {
      switch fieldNumber {
      case 1: try decoder.decodeSingularStringField(value: &id)
      case 2: try decoder.decodeSingularStringField(value: &display)
      case 3: try decoder.decodeRepeatedMessageField(value: &nodes)
      case 4: try decoder.decodeRepeatedMessageField(value: &edges)
      case 5: try decoder.decodeSingularBoolField(value: &isCompleted)
      case 6: try decoder.decodeSingularBoolField(value: &isProcessing)
      case 7: try decoder.decodeSingularBoolField(value: &isError)
      case 8: try decoder.decodeRepeatedStringField(value: &logs)
      case 9: try decoder.decodeSingularInt32Field(value: &numberCompleted)
      case 10: try decoder.decodeSingularInt32Field(value: &numberProcessing)
      case 11: try decoder.decodeSingularInt32Field(value: &numberErrored)
      case 12: try decoder.decodeSingularInt32Field(value: &numberUnprocessed)
      case 13: try decoder.decodeRepeatedMessageField(value: &children)
      default: break
      }
    }
  }

  public func traverse<V: SwiftProtobuf.Visitor>(visitor: inout V) throws {
    if !id.isEmpty {
      try visitor.visitSingularStringField(value: id, fieldNumber: 1)
    }
    if !display.isEmpty {
      try visitor.visitSingularStringField(value: display, fieldNumber: 2)
    }
    if !nodes.isEmpty {
      try visitor.visitRepeatedMessageField(value: nodes, fieldNumber: 3)
    }
    if !edges.isEmpty {
      try visitor.visitRepeatedMessageField(value: edges, fieldNumber: 4)
    }
    if isCompleted != false {
      try visitor.visitSingularBoolField(value: isCompleted, fieldNumber: 5)
    }
    if isProcessing != false {
      try visitor.visitSingularBoolField(value: isProcessing, fieldNumber: 6)
    }
    if isError != false {
      try visitor.visitSingularBoolField(value: isError, fieldNumber: 7)
    }
    if !logs.isEmpty {
      try visitor.visitRepeatedStringField(value: logs, fieldNumber: 8)
    }
    if numberCompleted != 0 {
      try visitor.visitSingularInt32Field(value: numberCompleted, fieldNumber: 9)
    }
    if numberProcessing != 0 {
      try visitor.visitSingularInt32Field(value: numberProcessing, fieldNumber: 10)
    }
    if numberErrored != 0 {
      try visitor.visitSingularInt32Field(value: numberErrored, fieldNumber: 11)
    }
    if numberUnprocessed != 0 {
      try visitor.visitSingularInt32Field(value: numberUnprocessed, fieldNumber: 12)
    }
    if !children.isEmpty {
      try visitor.visitRepeatedMessageField(value: children, fieldNumber: 13)
    }
    try unknownFields.traverse(visitor: &visitor)
  }

  public func _protobuf_generated_isEqualTo(other: Org_Roylance_Yadel_UIDag) -> Bool {
    if id != other.id {return false}
    if display != other.display {return false}
    if nodes != other.nodes {return false}
    if edges != other.edges {return false}
    if isCompleted != other.isCompleted {return false}
    if isProcessing != other.isProcessing {return false}
    if isError != other.isError {return false}
    if logs != other.logs {return false}
    if numberCompleted != other.numberCompleted {return false}
    if numberProcessing != other.numberProcessing {return false}
    if numberErrored != other.numberErrored {return false}
    if numberUnprocessed != other.numberUnprocessed {return false}
    if children != other.children {return false}
    if unknownFields != other.unknownFields {return false}
    return true
  }
}

public struct Org_Roylance_Yadel_UIEdge: SwiftProtobuf.Message, SwiftProtobuf._MessageImplementationBase, SwiftProtobuf._ProtoNameProviding {
  public static let protoMessageName: String = _protobuf_package + ".UIEdge"
  public static let _protobuf_nameMap: SwiftProtobuf._NameMap = [
    1: .standard(proto: "node_id_1"),
    2: .standard(proto: "node_id_2"),
  ]

  public var nodeId1: String = ""

  public var nodeId2: String = ""

  public var unknownFields = SwiftProtobuf.UnknownStorage()

  public init() {}

  public mutating func decodeMessage<D: SwiftProtobuf.Decoder>(decoder: inout D) throws {
    while let fieldNumber = try decoder.nextFieldNumber() {
      switch fieldNumber {
      case 1: try decoder.decodeSingularStringField(value: &nodeId1)
      case 2: try decoder.decodeSingularStringField(value: &nodeId2)
      default: break
      }
    }
  }

  public func traverse<V: SwiftProtobuf.Visitor>(visitor: inout V) throws {
    if !nodeId1.isEmpty {
      try visitor.visitSingularStringField(value: nodeId1, fieldNumber: 1)
    }
    if !nodeId2.isEmpty {
      try visitor.visitSingularStringField(value: nodeId2, fieldNumber: 2)
    }
    try unknownFields.traverse(visitor: &visitor)
  }

  public func _protobuf_generated_isEqualTo(other: Org_Roylance_Yadel_UIEdge) -> Bool {
    if nodeId1 != other.nodeId1 {return false}
    if nodeId2 != other.nodeId2 {return false}
    if unknownFields != other.unknownFields {return false}
    return true
  }
}

public struct Org_Roylance_Yadel_UINode: SwiftProtobuf.Message, SwiftProtobuf._MessageImplementationBase, SwiftProtobuf._ProtoNameProviding {
  public static let protoMessageName: String = _protobuf_package + ".UINode"
  public static let _protobuf_nameMap: SwiftProtobuf._NameMap = [
    1: .same(proto: "id"),
    2: .same(proto: "display"),
    4: .standard(proto: "execution_date"),
    5: .standard(proto: "start_date"),
    6: .standard(proto: "end_date"),
    7: .same(proto: "duration"),
    8: .standard(proto: "is_completed"),
    9: .standard(proto: "is_processing"),
    10: .standard(proto: "is_error"),
    11: .same(proto: "logs"),
    12: .standard(proto: "is_waiting_for_another_dag_task"),
  ]

  public var id: String = ""

  public var display: String = ""

  public var executionDate: Int64 = 0

  public var startDate: Int64 = 0

  public var endDate: Int64 = 0

  public var duration: Int64 = 0

  public var isCompleted: Bool = false

  public var isProcessing: Bool = false

  public var isError: Bool = false

  public var logs: [Org_Roylance_Yadel_UILog] = []

  public var isWaitingForAnotherDagTask: Bool = false

  public var unknownFields = SwiftProtobuf.UnknownStorage()

  public init() {}

  public mutating func decodeMessage<D: SwiftProtobuf.Decoder>(decoder: inout D) throws {
    while let fieldNumber = try decoder.nextFieldNumber() {
      switch fieldNumber {
      case 1: try decoder.decodeSingularStringField(value: &id)
      case 2: try decoder.decodeSingularStringField(value: &display)
      case 4: try decoder.decodeSingularInt64Field(value: &executionDate)
      case 5: try decoder.decodeSingularInt64Field(value: &startDate)
      case 6: try decoder.decodeSingularInt64Field(value: &endDate)
      case 7: try decoder.decodeSingularInt64Field(value: &duration)
      case 8: try decoder.decodeSingularBoolField(value: &isCompleted)
      case 9: try decoder.decodeSingularBoolField(value: &isProcessing)
      case 10: try decoder.decodeSingularBoolField(value: &isError)
      case 11: try decoder.decodeRepeatedMessageField(value: &logs)
      case 12: try decoder.decodeSingularBoolField(value: &isWaitingForAnotherDagTask)
      default: break
      }
    }
  }

  public func traverse<V: SwiftProtobuf.Visitor>(visitor: inout V) throws {
    if !id.isEmpty {
      try visitor.visitSingularStringField(value: id, fieldNumber: 1)
    }
    if !display.isEmpty {
      try visitor.visitSingularStringField(value: display, fieldNumber: 2)
    }
    if executionDate != 0 {
      try visitor.visitSingularInt64Field(value: executionDate, fieldNumber: 4)
    }
    if startDate != 0 {
      try visitor.visitSingularInt64Field(value: startDate, fieldNumber: 5)
    }
    if endDate != 0 {
      try visitor.visitSingularInt64Field(value: endDate, fieldNumber: 6)
    }
    if duration != 0 {
      try visitor.visitSingularInt64Field(value: duration, fieldNumber: 7)
    }
    if isCompleted != false {
      try visitor.visitSingularBoolField(value: isCompleted, fieldNumber: 8)
    }
    if isProcessing != false {
      try visitor.visitSingularBoolField(value: isProcessing, fieldNumber: 9)
    }
    if isError != false {
      try visitor.visitSingularBoolField(value: isError, fieldNumber: 10)
    }
    if !logs.isEmpty {
      try visitor.visitRepeatedMessageField(value: logs, fieldNumber: 11)
    }
    if isWaitingForAnotherDagTask != false {
      try visitor.visitSingularBoolField(value: isWaitingForAnotherDagTask, fieldNumber: 12)
    }
    try unknownFields.traverse(visitor: &visitor)
  }

  public func _protobuf_generated_isEqualTo(other: Org_Roylance_Yadel_UINode) -> Bool {
    if id != other.id {return false}
    if display != other.display {return false}
    if executionDate != other.executionDate {return false}
    if startDate != other.startDate {return false}
    if endDate != other.endDate {return false}
    if duration != other.duration {return false}
    if isCompleted != other.isCompleted {return false}
    if isProcessing != other.isProcessing {return false}
    if isError != other.isError {return false}
    if logs != other.logs {return false}
    if isWaitingForAnotherDagTask != other.isWaitingForAnotherDagTask {return false}
    if unknownFields != other.unknownFields {return false}
    return true
  }
}

public struct Org_Roylance_Yadel_UILog: SwiftProtobuf.Message, SwiftProtobuf._MessageImplementationBase, SwiftProtobuf._ProtoNameProviding {
  public static let protoMessageName: String = _protobuf_package + ".UILog"
  public static let _protobuf_nameMap: SwiftProtobuf._NameMap = [
    1: .same(proto: "id"),
    2: .same(proto: "message"),
  ]

  public var id: String = ""

  public var message: String = ""

  public var unknownFields = SwiftProtobuf.UnknownStorage()

  public init() {}

  public mutating func decodeMessage<D: SwiftProtobuf.Decoder>(decoder: inout D) throws {
    while let fieldNumber = try decoder.nextFieldNumber() {
      switch fieldNumber {
      case 1: try decoder.decodeSingularStringField(value: &id)
      case 2: try decoder.decodeSingularStringField(value: &message)
      default: break
      }
    }
  }

  public func traverse<V: SwiftProtobuf.Visitor>(visitor: inout V) throws {
    if !id.isEmpty {
      try visitor.visitSingularStringField(value: id, fieldNumber: 1)
    }
    if !message.isEmpty {
      try visitor.visitSingularStringField(value: message, fieldNumber: 2)
    }
    try unknownFields.traverse(visitor: &visitor)
  }

  public func _protobuf_generated_isEqualTo(other: Org_Roylance_Yadel_UILog) -> Bool {
    if id != other.id {return false}
    if message != other.message {return false}
    if unknownFields != other.unknownFields {return false}
    return true
  }
}

public struct Org_Roylance_Yadel_UIWorkerConfiguration: SwiftProtobuf.Message, SwiftProtobuf._MessageImplementationBase, SwiftProtobuf._ProtoNameProviding {
  public static let protoMessageName: String = _protobuf_package + ".UIWorkerConfiguration"
  public static let _protobuf_nameMap: SwiftProtobuf._NameMap = [
    1: .same(proto: "ip"),
    2: .same(proto: "port"),
    3: .same(proto: "host"),
    4: .standard(proto: "initialized_time"),
    5: .same(proto: "state"),
    6: .standard(proto: "task_display"),
    7: .standard(proto: "dag_display"),
    8: .standard(proto: "minutes_before_task_reset"),
    9: .standard(proto: "task_start_time"),
    10: .standard(proto: "task_working_time_display"),
  ]

  public var ip: String = ""

  public var port: String = ""

  public var host: String = ""

  public var initializedTime: String = ""

  public var state: Org_Roylance_Yadel_UIWorkerState = Org_Roylance_Yadel_UIWorkerState.currentlyWorking

  public var taskDisplay: String = ""

  public var dagDisplay: String = ""

  public var minutesBeforeTaskReset: UInt64 = 0

  public var taskStartTime: String = ""

  public var taskWorkingTimeDisplay: String = ""

  public var unknownFields = SwiftProtobuf.UnknownStorage()

  public init() {}

  public mutating func decodeMessage<D: SwiftProtobuf.Decoder>(decoder: inout D) throws {
    while let fieldNumber = try decoder.nextFieldNumber() {
      switch fieldNumber {
      case 1: try decoder.decodeSingularStringField(value: &ip)
      case 2: try decoder.decodeSingularStringField(value: &port)
      case 3: try decoder.decodeSingularStringField(value: &host)
      case 4: try decoder.decodeSingularStringField(value: &initializedTime)
      case 5: try decoder.decodeSingularEnumField(value: &state)
      case 6: try decoder.decodeSingularStringField(value: &taskDisplay)
      case 7: try decoder.decodeSingularStringField(value: &dagDisplay)
      case 8: try decoder.decodeSingularUInt64Field(value: &minutesBeforeTaskReset)
      case 9: try decoder.decodeSingularStringField(value: &taskStartTime)
      case 10: try decoder.decodeSingularStringField(value: &taskWorkingTimeDisplay)
      default: break
      }
    }
  }

  public func traverse<V: SwiftProtobuf.Visitor>(visitor: inout V) throws {
    if !ip.isEmpty {
      try visitor.visitSingularStringField(value: ip, fieldNumber: 1)
    }
    if !port.isEmpty {
      try visitor.visitSingularStringField(value: port, fieldNumber: 2)
    }
    if !host.isEmpty {
      try visitor.visitSingularStringField(value: host, fieldNumber: 3)
    }
    if !initializedTime.isEmpty {
      try visitor.visitSingularStringField(value: initializedTime, fieldNumber: 4)
    }
    if state != Org_Roylance_Yadel_UIWorkerState.currentlyWorking {
      try visitor.visitSingularEnumField(value: state, fieldNumber: 5)
    }
    if !taskDisplay.isEmpty {
      try visitor.visitSingularStringField(value: taskDisplay, fieldNumber: 6)
    }
    if !dagDisplay.isEmpty {
      try visitor.visitSingularStringField(value: dagDisplay, fieldNumber: 7)
    }
    if minutesBeforeTaskReset != 0 {
      try visitor.visitSingularUInt64Field(value: minutesBeforeTaskReset, fieldNumber: 8)
    }
    if !taskStartTime.isEmpty {
      try visitor.visitSingularStringField(value: taskStartTime, fieldNumber: 9)
    }
    if !taskWorkingTimeDisplay.isEmpty {
      try visitor.visitSingularStringField(value: taskWorkingTimeDisplay, fieldNumber: 10)
    }
    try unknownFields.traverse(visitor: &visitor)
  }

  public func _protobuf_generated_isEqualTo(other: Org_Roylance_Yadel_UIWorkerConfiguration) -> Bool {
    if ip != other.ip {return false}
    if port != other.port {return false}
    if host != other.host {return false}
    if initializedTime != other.initializedTime {return false}
    if state != other.state {return false}
    if taskDisplay != other.taskDisplay {return false}
    if dagDisplay != other.dagDisplay {return false}
    if minutesBeforeTaskReset != other.minutesBeforeTaskReset {return false}
    if taskStartTime != other.taskStartTime {return false}
    if taskWorkingTimeDisplay != other.taskWorkingTimeDisplay {return false}
    if unknownFields != other.unknownFields {return false}
    return true
  }
}