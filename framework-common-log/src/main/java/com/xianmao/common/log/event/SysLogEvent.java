package com.xianmao.common.log.event;

import com.xianmao.common.log.model.SysLogInfo;
import org.springframework.context.ApplicationEvent;


public class SysLogEvent extends ApplicationEvent {

	public SysLogEvent(SysLogInfo source) {
		super(source);
	}

}
