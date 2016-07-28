"use strict";
/// <reference path="./model.d.ts" />
var _root = dcodeIO.ProtoBuf.newBuilder({})['import']({
    "package": "org.roylance.yadel",
    "messages": [
        {
            "name": "WorkerConfiguration",
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
                    "name": "ip",
                    "id": 2
                },
                {
                    "rule": "optional",
                    "type": "string",
                    "name": "port",
                    "id": 3
                },
                {
                    "rule": "optional",
                    "type": "string",
                    "name": "host",
                    "id": 4
                },
                {
                    "rule": "optional",
                    "type": "uint64",
                    "name": "initialized_time",
                    "id": 5
                },
                {
                    "rule": "optional",
                    "type": "WorkerState",
                    "name": "state",
                    "id": 6
                }
            ]
        },
        {
            "name": "Log",
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
                    "name": "message",
                    "id": 2
                }
            ]
        },
        {
            "name": "Dag",
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
                    "type": "Task",
                    "name": "flattened_tasks",
                    "id": 3
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
                    "rule": "repeated",
                    "type": "Task",
                    "name": "uncompleted_tasks",
                    "id": 8
                },
                {
                    "rule": "repeated",
                    "type": "Task",
                    "name": "processing_tasks",
                    "id": 9
                },
                {
                    "rule": "repeated",
                    "type": "Task",
                    "name": "errored_tasks",
                    "id": 10
                },
                {
                    "rule": "repeated",
                    "type": "Task",
                    "name": "completed_tasks",
                    "id": 11
                }
            ]
        },
        {
            "name": "Task",
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
                    "type": "TaskDependency",
                    "name": "dependencies",
                    "id": 3
                },
                {
                    "rule": "optional",
                    "type": "string",
                    "name": "dag_id",
                    "id": 4
                },
                {
                    "rule": "repeated",
                    "type": "Log",
                    "name": "logs",
                    "id": 5
                },
                {
                    "rule": "optional",
                    "type": "int64",
                    "name": "execution_date",
                    "id": 6
                },
                {
                    "rule": "optional",
                    "type": "int64",
                    "name": "start_date",
                    "id": 7
                },
                {
                    "rule": "optional",
                    "type": "int64",
                    "name": "end_date",
                    "id": 8
                },
                {
                    "rule": "optional",
                    "type": "int64",
                    "name": "duration",
                    "id": 9
                },
                {
                    "rule": "optional",
                    "type": "string",
                    "name": "first_context_base_64",
                    "id": 10
                },
                {
                    "rule": "optional",
                    "type": "string",
                    "name": "second_context_base_64",
                    "id": 11
                },
                {
                    "rule": "optional",
                    "type": "string",
                    "name": "third_context_base_64",
                    "id": 12
                }
            ]
        },
        {
            "name": "TaskDependency",
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
                    "name": "parent_task_id",
                    "id": 2
                }
            ]
        },
        {
            "name": "AddTaskToDag",
            "fields": [
                {
                    "rule": "optional",
                    "type": "string",
                    "name": "id",
                    "id": 1
                },
                {
                    "rule": "optional",
                    "type": "Task",
                    "name": "parent_task",
                    "id": 2
                },
                {
                    "rule": "optional",
                    "type": "Task",
                    "name": "new_task",
                    "id": 3
                },
                {
                    "rule": "optional",
                    "type": "string",
                    "name": "first_context_base_64",
                    "id": 4
                },
                {
                    "rule": "optional",
                    "type": "string",
                    "name": "second_context_base_64",
                    "id": 5
                },
                {
                    "rule": "optional",
                    "type": "string",
                    "name": "third_context_base_64",
                    "id": 6
                }
            ]
        },
        {
            "name": "CompleteTask",
            "fields": [
                {
                    "rule": "optional",
                    "type": "string",
                    "name": "id",
                    "id": 1
                },
                {
                    "rule": "optional",
                    "type": "Task",
                    "name": "task",
                    "id": 2
                },
                {
                    "rule": "optional",
                    "type": "WorkerConfiguration",
                    "name": "worker_configuration",
                    "id": 3
                },
                {
                    "rule": "repeated",
                    "type": "string",
                    "name": "logs",
                    "id": 4
                },
                {
                    "rule": "optional",
                    "type": "bool",
                    "name": "is_error",
                    "id": 5
                }
            ]
        },
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
            "name": "UIResponse",
            "fields": [
                {
                    "rule": "optional",
                    "type": "string",
                    "name": "content",
                    "id": 1
                },
                {
                    "rule": "optional",
                    "type": "UIDagReport",
                    "name": "report",
                    "id": 2
                },
                {
                    "rule": "optional",
                    "type": "UIDag",
                    "name": "dag",
                    "id": 3
                },
                {
                    "rule": "repeated",
                    "type": "UIWorkerConfiguration",
                    "name": "configurations",
                    "id": 4
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
            "name": "WorkerState",
            "values": [
                {
                    "name": "WORKING",
                    "id": 0
                },
                {
                    "name": "IDLE",
                    "id": 1
                }
            ]
        },
        {
            "name": "WorkerToManagerMessageType",
            "values": [
                {
                    "name": "REGISTRATION",
                    "id": 0
                }
            ]
        },
        {
            "name": "ManagerToManagerMessageType",
            "values": [
                {
                    "name": "ENSURE_WORKERS_WORKING",
                    "id": 0
                }
            ]
        },
        {
            "name": "ActorRole",
            "values": [
                {
                    "name": "MANAGER",
                    "id": 0
                },
                {
                    "name": "WORKER",
                    "id": 1
                }
            ]
        },
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
}).build();
exports.YadelModel = _root;
