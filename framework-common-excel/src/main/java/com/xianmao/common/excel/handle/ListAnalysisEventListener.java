package com.xianmao.common.excel.handle;

import com.alibaba.excel.event.AnalysisEventListener;
import com.xianmao.common.excel.vo.ErrorMessage;

import java.util.List;

public abstract class ListAnalysisEventListener<T> extends AnalysisEventListener<T> {

	/**
	 * 获取 excel 解析的对象列表
	 * @return 集合
	 */
	public abstract List<T> getList();

	/**
	 * 获取异常校验结果
	 * @return 集合
	 */
	public abstract List<ErrorMessage> getErrors();

}
