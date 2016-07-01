"use strict";
/// <reference path="yadelreports.d.ts" />
exports.YadelFactory = dcodeIO.ProtoBuf.newBuilder({})['import']({
    "package": "org.roylance.yadel.api.models",
    "messages": [
        {
            "name": "UIRequest",
            "fields": [
                {
                    "rule": "optional",
                    "type": "UIRequests",
                    "name": "request_type",
                    "id": 1
                },
                {
                    "rule": "optional",
                    "type": "string",
                    "name": "dag_id",
                    "id": 2
                }
            ]
        },
        {
            "name": "UIDagReport",
            "fields": [
                {
                    "rule": "repeated",
                    "type": "UIWorkerConfiguration",
                    "name": "workers",
                    "id": 1
                },
                {
                    "rule": "repeated",
                    "type": "UIDag",
                    "name": "dags",
                    "id": 2
                }
            ]
        },
        {
            "name": "UIDag",
            "fields": [
                {
                    "rule": "optional",
                    "type": "string",
                    "name": "id",
                    "id": 1
                },
                {
                    "rule": "optional",
                    "type": "string",
                    "name": "display",
                    "id": 2
                },
                {
                    "rule": "repeated",
                    "type": "UINode",
                    "name": "nodes",
                    "id": 3
                },
                {
                    "rule": "repeated",
                    "type": "UIEdge",
                    "name": "edges",
                    "id": 4
                },
                {
                    "rule": "optional",
                    "type": "bool",
                    "name": "is_completed",
                    "id": 5
                },
                {
                    "rule": "optional",
                    "type": "bool",
                    "name": "is_processing",
                    "id": 6
                },
                {
                    "rule": "optional",
                    "type": "bool",
                    "name": "is_error",
                    "id": 7
                },
                {
                    "rule": "repeated",
                    "type": "string",
                    "name": "logs",
                    "id": 8
                },
                {
                    "rule": "optional",
                    "type": "int32",
                    "name": "number_completed",
                    "id": 9
                },
                {
                    "rule": "optional",
                    "type": "int32",
                    "name": "number_processing",
                    "id": 10
                },
                {
                    "rule": "optional",
                    "type": "int32",
                    "name": "number_errored",
                    "id": 11
                },
                {
                    "rule": "optional",
                    "type": "int32",
                    "name": "number_unprocessed",
                    "id": 12
                }
            ]
        },
        {
            "name": "UIEdge",
            "fields": [
                {
                    "rule": "optional",
                    "type": "string",
                    "name": "node_id_1",
                    "id": 1
                },
                {
                    "rule": "optional",
                    "type": "string",
                    "name": "node_id_2",
                    "id": 2
                }
            ]
        },
        {
            "name": "UINode",
            "fields": [
                {
                    "rule": "optional",
                    "type": "string",
                    "name": "id",
                    "id": 1
                },
                {
                    "rule": "optional",
                    "type": "string",
                    "name": "display",
                    "id": 2
                },
                {
                    "rule": "optional",
                    "type": "int64",
                    "name": "execution_date",
                    "id": 4
                },
                {
                    "rule": "optional",
                    "type": "int64",
                    "name": "start_date",
                    "id": 5
                },
                {
                    "rule": "optional",
                    "type": "int64",
                    "name": "end_date",
                    "id": 6
                },
                {
                    "rule": "optional",
                    "type": "int64",
                    "name": "duration",
                    "id": 7
                },
                {
                    "rule": "optional",
                    "type": "bool",
                    "name": "is_completed",
                    "id": 8
                },
                {
                    "rule": "optional",
                    "type": "bool",
                    "name": "is_processing",
                    "id": 9
                },
                {
                    "rule": "optional",
                    "type": "bool",
                    "name": "is_error",
                    "id": 10
                },
                {
                    "rule": "repeated",
                    "type": "string",
                    "name": "logs",
                    "id": 11
                }
            ]
        },
        {
            "name": "UIWorkerConfiguration",
            "fields": [
                {
                    "rule": "optional",
                    "type": "string",
                    "name": "ip",
                    "id": 1
                },
                {
                    "rule": "optional",
                    "type": "string",
                    "name": "port",
                    "id": 2
                },
                {
                    "rule": "optional",
                    "type": "string",
                    "name": "host",
                    "id": 3
                },
                {
                    "rule": "optional",
                    "type": "uint64",
                    "name": "initialized_time",
                    "id": 4
                },
                {
                    "rule": "optional",
                    "type": "UIWorkerState",
                    "name": "state",
                    "id": 5
                }
            ]
        }
    ],
    "enums": [
        {
            "name": "UIRequests",
            "values": [
                {
                    "name": "REPORT_DAGS",
                    "id": 0
                },
                {
                    "name": "DELETE_DAG",
                    "id": 1
                }
            ]
        },
        {
            "name": "UIWorkerState",
            "values": [
                {
                    "name": "CURRENTLY_WORKING",
                    "id": 0
                },
                {
                    "name": "CURRENTLY_IDLE",
                    "id": 1
                }
            ]
        }
    ]
}).build().org.roylance.yadel.api.models;
//# sourceMappingURL=index.js.map