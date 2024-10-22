package com.xianmao.common.excel.handler;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.xianmao.common.excel.converters.DictTypeConvert;

/**
 * dict cache clear analysis 事件监听器
 *
 * @author lengleng
 * @date 2024/08/31
 */
public class DictCacheClearAnalysisEventListener implements ReadListener<Object> {

	@Override
	public void invoke(Object o, AnalysisContext analysisContext) {
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext analysisContext) {
		DictTypeConvert.cache.clear();
	}

}
