// THIS FILE WAS AUTO-GENERATED. DO NOT ALTER!
package org.roylance.services;

public class ReportJNIBridge {
	private native byte[] delete_dagJNI(byte[] request);
	private native byte[] currentJNI(byte[] request);
	private native byte[] get_dag_statusJNI(byte[] request);
	public byte[] delete_dag(byte[] request) {
		return delete_dagJNI(request);
	}
	public byte[] current(byte[] request) {
		return currentJNI(request);
	}
	public byte[] get_dag_status(byte[] request) {
		return get_dag_statusJNI(request);
	}
}
