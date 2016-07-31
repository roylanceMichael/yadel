// generated by Proto2Typescript. do not touch!

declare module org.roylance.yadel {
	interface ProtoBufModel {
		toArrayBuffer(): ArrayBuffer;
		//toBuffer(): NodeBuffer;
		//encode(): ByteBuffer;
		toBase64(): string;
		toString(): string;
	}

	export interface ProtoBufBuilder {
		UIYadelRequest: UIYadelRequestBuilder;
		UIYadelResponse: UIYadelResponseBuilder;
		UIDagReport: UIDagReportBuilder;
		UIDag: UIDagBuilder;
		UIEdge: UIEdgeBuilder;
		UINode: UINodeBuilder;
		UIWorkerConfiguration: UIWorkerConfigurationBuilder;
		CommonAction: CommonActionBuilder;
		ReportController: ReportControllerBuilder;
		WorkerConfiguration: WorkerConfigurationBuilder;
		Log: LogBuilder;
		Dag: DagBuilder;
		Task: TaskBuilder;
		TaskDependency: TaskDependencyBuilder;
		AddTaskToDag: AddTaskToDagBuilder;
		CompleteTask: CompleteTaskBuilder;
		UIYadelRequestType: UIYadelRequestType;
		UIWorkerState: UIWorkerState;
		WorkerState: WorkerState;
		WorkerToManagerMessageType: WorkerToManagerMessageType;
		ManagerToManagerMessageType: ManagerToManagerMessageType;
		ActorRole: ActorRole;
		
	}
}

declare module org.roylance.yadel {

	export interface UIYadelRequest extends ProtoBufModel {
		request_type?: UIYadelRequestType;
		getRequestType() : UIYadelRequestType;
		setRequestType(requestType : UIYadelRequestType): void;
		dag_id?: string;
		getDagId() : string;
		setDagId(dagId : string): void;
		token?: string;
		getToken() : string;
		setToken(token : string): void;
		user_name?: string;
		getUserName() : string;
		setUserName(userName : string): void;
		password?: string;
		getPassword() : string;
		setPassword(password : string): void;
		
	}
	
	export interface UIYadelRequestBuilder {
		new(): UIYadelRequest;
		decode(buffer: ArrayBuffer) : UIYadelRequest;
		//decode(buffer: NodeBuffer) : UIYadelRequest;
		//decode(buffer: ByteArrayBuffer) : UIYadelRequest;
		decode64(buffer: string) : UIYadelRequest;
		
	}	
}

declare module org.roylance.yadel {

	export interface UIYadelResponse extends ProtoBufModel {
		content?: string;
		getContent() : string;
		setContent(content : string): void;
		report?: UIDagReport;
		getReport() : UIDagReport;
		setReport(report : UIDagReport): void;
		dag?: UIDag;
		getDag() : UIDag;
		setDag(dag : UIDag): void;
		configurations: UIWorkerConfiguration[];
		getConfigurations() : UIWorkerConfiguration[];
		setConfigurations(configurations : UIWorkerConfiguration[]): void;
		
	}
	
	export interface UIYadelResponseBuilder {
		new(): UIYadelResponse;
		decode(buffer: ArrayBuffer) : UIYadelResponse;
		//decode(buffer: NodeBuffer) : UIYadelResponse;
		//decode(buffer: ByteArrayBuffer) : UIYadelResponse;
		decode64(buffer: string) : UIYadelResponse;
		
	}	
}

declare module org.roylance.yadel {

	export interface UIDagReport extends ProtoBufModel {
		workers: UIWorkerConfiguration[];
		getWorkers() : UIWorkerConfiguration[];
		setWorkers(workers : UIWorkerConfiguration[]): void;
		dags: UIDag[];
		getDags() : UIDag[];
		setDags(dags : UIDag[]): void;
		
	}
	
	export interface UIDagReportBuilder {
		new(): UIDagReport;
		decode(buffer: ArrayBuffer) : UIDagReport;
		//decode(buffer: NodeBuffer) : UIDagReport;
		//decode(buffer: ByteArrayBuffer) : UIDagReport;
		decode64(buffer: string) : UIDagReport;
		
	}	
}

declare module org.roylance.yadel {

	export interface UIDag extends ProtoBufModel {
		id?: string;
		getId() : string;
		setId(id : string): void;
		display?: string;
		getDisplay() : string;
		setDisplay(display : string): void;
		nodes: UINode[];
		getNodes() : UINode[];
		setNodes(nodes : UINode[]): void;
		edges: UIEdge[];
		getEdges() : UIEdge[];
		setEdges(edges : UIEdge[]): void;
		is_completed?: boolean;
		getIsCompleted() : boolean;
		setIsCompleted(isCompleted : boolean): void;
		is_processing?: boolean;
		getIsProcessing() : boolean;
		setIsProcessing(isProcessing : boolean): void;
		is_error?: boolean;
		getIsError() : boolean;
		setIsError(isError : boolean): void;
		logs: string[];
		getLogs() : string[];
		setLogs(logs : string[]): void;
		number_completed?: number;
		getNumberCompleted() : number;
		setNumberCompleted(numberCompleted : number): void;
		number_processing?: number;
		getNumberProcessing() : number;
		setNumberProcessing(numberProcessing : number): void;
		number_errored?: number;
		getNumberErrored() : number;
		setNumberErrored(numberErrored : number): void;
		number_unprocessed?: number;
		getNumberUnprocessed() : number;
		setNumberUnprocessed(numberUnprocessed : number): void;
		
	}
	
	export interface UIDagBuilder {
		new(): UIDag;
		decode(buffer: ArrayBuffer) : UIDag;
		//decode(buffer: NodeBuffer) : UIDag;
		//decode(buffer: ByteArrayBuffer) : UIDag;
		decode64(buffer: string) : UIDag;
		
	}	
}

declare module org.roylance.yadel {

	export interface UIEdge extends ProtoBufModel {
		node_id_1?: string;
		getNodeId_1() : string;
		setNodeId_1(nodeId_1 : string): void;
		node_id_2?: string;
		getNodeId_2() : string;
		setNodeId_2(nodeId_2 : string): void;
		
	}
	
	export interface UIEdgeBuilder {
		new(): UIEdge;
		decode(buffer: ArrayBuffer) : UIEdge;
		//decode(buffer: NodeBuffer) : UIEdge;
		//decode(buffer: ByteArrayBuffer) : UIEdge;
		decode64(buffer: string) : UIEdge;
		
	}	
}

declare module org.roylance.yadel {

	export interface UINode extends ProtoBufModel {
		id?: string;
		getId() : string;
		setId(id : string): void;
		display?: string;
		getDisplay() : string;
		setDisplay(display : string): void;
		execution_date?: number;
		getExecutionDate() : number;
		setExecutionDate(executionDate : number): void;
		start_date?: number;
		getStartDate() : number;
		setStartDate(startDate : number): void;
		end_date?: number;
		getEndDate() : number;
		setEndDate(endDate : number): void;
		duration?: number;
		getDuration() : number;
		setDuration(duration : number): void;
		is_completed?: boolean;
		getIsCompleted() : boolean;
		setIsCompleted(isCompleted : boolean): void;
		is_processing?: boolean;
		getIsProcessing() : boolean;
		setIsProcessing(isProcessing : boolean): void;
		is_error?: boolean;
		getIsError() : boolean;
		setIsError(isError : boolean): void;
		logs: string[];
		getLogs() : string[];
		setLogs(logs : string[]): void;
		
	}
	
	export interface UINodeBuilder {
		new(): UINode;
		decode(buffer: ArrayBuffer) : UINode;
		//decode(buffer: NodeBuffer) : UINode;
		//decode(buffer: ByteArrayBuffer) : UINode;
		decode64(buffer: string) : UINode;
		
	}	
}

declare module org.roylance.yadel {

	export interface UIWorkerConfiguration extends ProtoBufModel {
		ip?: string;
		getIp() : string;
		setIp(ip : string): void;
		port?: string;
		getPort() : string;
		setPort(port : string): void;
		host?: string;
		getHost() : string;
		setHost(host : string): void;
		initialized_time?: number;
		getInitializedTime() : number;
		setInitializedTime(initializedTime : number): void;
		state?: UIWorkerState;
		getState() : UIWorkerState;
		setState(state : UIWorkerState): void;
		
	}
	
	export interface UIWorkerConfigurationBuilder {
		new(): UIWorkerConfiguration;
		decode(buffer: ArrayBuffer) : UIWorkerConfiguration;
		//decode(buffer: NodeBuffer) : UIWorkerConfiguration;
		//decode(buffer: ByteArrayBuffer) : UIWorkerConfiguration;
		decode64(buffer: string) : UIWorkerConfiguration;
		
	}	
}

declare module org.roylance.yadel {

	export interface CommonAction extends ProtoBufModel {
		request?: UIYadelRequest;
		getRequest() : UIYadelRequest;
		setRequest(request : UIYadelRequest): void;
		response?: UIYadelResponse;
		getResponse() : UIYadelResponse;
		setResponse(response : UIYadelResponse): void;
		
	}
	
	export interface CommonActionBuilder {
		new(): CommonAction;
		decode(buffer: ArrayBuffer) : CommonAction;
		//decode(buffer: NodeBuffer) : CommonAction;
		//decode(buffer: ByteArrayBuffer) : CommonAction;
		decode64(buffer: string) : CommonAction;
		
	}	
}

declare module org.roylance.yadel {

	export interface ReportController extends ProtoBufModel {
		delete_dag?: CommonAction;
		getDeleteDag() : CommonAction;
		setDeleteDag(deleteDag : CommonAction): void;
		current?: CommonAction;
		getCurrent() : CommonAction;
		setCurrent(current : CommonAction): void;
		
	}
	
	export interface ReportControllerBuilder {
		new(): ReportController;
		decode(buffer: ArrayBuffer) : ReportController;
		//decode(buffer: NodeBuffer) : ReportController;
		//decode(buffer: ByteArrayBuffer) : ReportController;
		decode64(buffer: string) : ReportController;
		
	}	
}

declare module org.roylance.yadel {

	export interface WorkerConfiguration extends ProtoBufModel {
		id?: string;
		getId() : string;
		setId(id : string): void;
		ip?: string;
		getIp() : string;
		setIp(ip : string): void;
		port?: string;
		getPort() : string;
		setPort(port : string): void;
		host?: string;
		getHost() : string;
		setHost(host : string): void;
		initialized_time?: number;
		getInitializedTime() : number;
		setInitializedTime(initializedTime : number): void;
		state?: WorkerState;
		getState() : WorkerState;
		setState(state : WorkerState): void;
		
	}
	
	export interface WorkerConfigurationBuilder {
		new(): WorkerConfiguration;
		decode(buffer: ArrayBuffer) : WorkerConfiguration;
		//decode(buffer: NodeBuffer) : WorkerConfiguration;
		//decode(buffer: ByteArrayBuffer) : WorkerConfiguration;
		decode64(buffer: string) : WorkerConfiguration;
		
	}	
}

declare module org.roylance.yadel {

	export interface Log extends ProtoBufModel {
		id?: string;
		getId() : string;
		setId(id : string): void;
		message?: string;
		getMessage() : string;
		setMessage(message : string): void;
		
	}
	
	export interface LogBuilder {
		new(): Log;
		decode(buffer: ArrayBuffer) : Log;
		//decode(buffer: NodeBuffer) : Log;
		//decode(buffer: ByteArrayBuffer) : Log;
		decode64(buffer: string) : Log;
		
	}	
}

declare module org.roylance.yadel {

	export interface Dag extends ProtoBufModel {
		id?: string;
		getId() : string;
		setId(id : string): void;
		display?: string;
		getDisplay() : string;
		setDisplay(display : string): void;
		flattened_tasks: Task[];
		getFlattenedTasks() : Task[];
		setFlattenedTasks(flattenedTasks : Task[]): void;
		execution_date?: number;
		getExecutionDate() : number;
		setExecutionDate(executionDate : number): void;
		start_date?: number;
		getStartDate() : number;
		setStartDate(startDate : number): void;
		end_date?: number;
		getEndDate() : number;
		setEndDate(endDate : number): void;
		duration?: number;
		getDuration() : number;
		setDuration(duration : number): void;
		uncompleted_tasks: Task[];
		getUncompletedTasks() : Task[];
		setUncompletedTasks(uncompletedTasks : Task[]): void;
		processing_tasks: Task[];
		getProcessingTasks() : Task[];
		setProcessingTasks(processingTasks : Task[]): void;
		errored_tasks: Task[];
		getErroredTasks() : Task[];
		setErroredTasks(erroredTasks : Task[]): void;
		completed_tasks: Task[];
		getCompletedTasks() : Task[];
		setCompletedTasks(completedTasks : Task[]): void;
		
	}
	
	export interface DagBuilder {
		new(): Dag;
		decode(buffer: ArrayBuffer) : Dag;
		//decode(buffer: NodeBuffer) : Dag;
		//decode(buffer: ByteArrayBuffer) : Dag;
		decode64(buffer: string) : Dag;
		
	}	
}

declare module org.roylance.yadel {

	export interface Task extends ProtoBufModel {
		id?: string;
		getId() : string;
		setId(id : string): void;
		display?: string;
		getDisplay() : string;
		setDisplay(display : string): void;
		dependencies: TaskDependency[];
		getDependencies() : TaskDependency[];
		setDependencies(dependencies : TaskDependency[]): void;
		dag_id?: string;
		getDagId() : string;
		setDagId(dagId : string): void;
		logs: Log[];
		getLogs() : Log[];
		setLogs(logs : Log[]): void;
		execution_date?: number;
		getExecutionDate() : number;
		setExecutionDate(executionDate : number): void;
		start_date?: number;
		getStartDate() : number;
		setStartDate(startDate : number): void;
		end_date?: number;
		getEndDate() : number;
		setEndDate(endDate : number): void;
		duration?: number;
		getDuration() : number;
		setDuration(duration : number): void;
		first_context_base_64?: string;
		getFirstContextBase_64() : string;
		setFirstContextBase_64(firstContextBase_64 : string): void;
		second_context_base_64?: string;
		getSecondContextBase_64() : string;
		setSecondContextBase_64(secondContextBase_64 : string): void;
		third_context_base_64?: string;
		getThirdContextBase_64() : string;
		setThirdContextBase_64(thirdContextBase_64 : string): void;
		
	}
	
	export interface TaskBuilder {
		new(): Task;
		decode(buffer: ArrayBuffer) : Task;
		//decode(buffer: NodeBuffer) : Task;
		//decode(buffer: ByteArrayBuffer) : Task;
		decode64(buffer: string) : Task;
		
	}	
}

declare module org.roylance.yadel {

	export interface TaskDependency extends ProtoBufModel {
		id?: string;
		getId() : string;
		setId(id : string): void;
		parent_task_id?: string;
		getParentTaskId() : string;
		setParentTaskId(parentTaskId : string): void;
		
	}
	
	export interface TaskDependencyBuilder {
		new(): TaskDependency;
		decode(buffer: ArrayBuffer) : TaskDependency;
		//decode(buffer: NodeBuffer) : TaskDependency;
		//decode(buffer: ByteArrayBuffer) : TaskDependency;
		decode64(buffer: string) : TaskDependency;
		
	}	
}

declare module org.roylance.yadel {

	export interface AddTaskToDag extends ProtoBufModel {
		id?: string;
		getId() : string;
		setId(id : string): void;
		parent_task?: Task;
		getParentTask() : Task;
		setParentTask(parentTask : Task): void;
		new_task?: Task;
		getNewTask() : Task;
		setNewTask(newTask : Task): void;
		first_context_base_64?: string;
		getFirstContextBase_64() : string;
		setFirstContextBase_64(firstContextBase_64 : string): void;
		second_context_base_64?: string;
		getSecondContextBase_64() : string;
		setSecondContextBase_64(secondContextBase_64 : string): void;
		third_context_base_64?: string;
		getThirdContextBase_64() : string;
		setThirdContextBase_64(thirdContextBase_64 : string): void;
		
	}
	
	export interface AddTaskToDagBuilder {
		new(): AddTaskToDag;
		decode(buffer: ArrayBuffer) : AddTaskToDag;
		//decode(buffer: NodeBuffer) : AddTaskToDag;
		//decode(buffer: ByteArrayBuffer) : AddTaskToDag;
		decode64(buffer: string) : AddTaskToDag;
		
	}	
}

declare module org.roylance.yadel {

	export interface CompleteTask extends ProtoBufModel {
		id?: string;
		getId() : string;
		setId(id : string): void;
		task?: Task;
		getTask() : Task;
		setTask(task : Task): void;
		worker_configuration?: WorkerConfiguration;
		getWorkerConfiguration() : WorkerConfiguration;
		setWorkerConfiguration(workerConfiguration : WorkerConfiguration): void;
		logs: string[];
		getLogs() : string[];
		setLogs(logs : string[]): void;
		is_error?: boolean;
		getIsError() : boolean;
		setIsError(isError : boolean): void;
		
	}
	
	export interface CompleteTaskBuilder {
		new(): CompleteTask;
		decode(buffer: ArrayBuffer) : CompleteTask;
		//decode(buffer: NodeBuffer) : CompleteTask;
		//decode(buffer: ByteArrayBuffer) : CompleteTask;
		decode64(buffer: string) : CompleteTask;
		
	}	
}

declare module org.roylance.yadel {
	export const enum UIYadelRequestType {
		REPORT_DAGS = 0,
		DELETE_DAG = 1,
		
	}
}

declare module org.roylance.yadel {
	export const enum UIWorkerState {
		CURRENTLY_WORKING = 0,
		CURRENTLY_IDLE = 1,
		
	}
}

declare module org.roylance.yadel {
	export const enum WorkerState {
		WORKING = 0,
		IDLE = 1,
		
	}
}

declare module org.roylance.yadel {
	export const enum WorkerToManagerMessageType {
		REGISTRATION = 0,
		
	}
}

declare module org.roylance.yadel {
	export const enum ManagerToManagerMessageType {
		ENSURE_WORKERS_WORKING = 0,
		
	}
}

declare module org.roylance.yadel {
	export const enum ActorRole {
		MANAGER = 0,
		WORKER = 1,
		
	}
}
