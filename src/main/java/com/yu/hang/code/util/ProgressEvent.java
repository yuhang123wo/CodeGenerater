package com.yu.hang.code.util;

/**
 * 
 * @author Administrator
 *
 */
public class ProgressEvent {

	private EventType type; // 事件类型
	private int curr; // 进度
	private String info; // 说明性文字

	public enum EventType {
		error, normal, end;
	}

	public ProgressEvent(EventType type, int curr, String info) {
		this.type = type;
		this.curr = curr;
		this.info = info;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public int getCurr() {
		return curr;
	}

	public void setCurr(int curr) {
		this.curr = curr;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return "EventType=" + type + "&curr=" + curr + "&info=" + info;
	}
}
