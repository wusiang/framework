package com.xianmao.common.excel.handle;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.xianmao.common.excel.annotation.ResponseExcel;
import com.xianmao.common.excel.config.ExcelConfigProperties;
import com.xianmao.common.excel.enhance.WriterBuilderEnhancer;
import com.xianmao.common.excel.kit.ExcelException;
import org.springframework.beans.factory.ObjectProvider;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 处理单sheet 页面
 */
public class SingleSheetWriteHandler extends AbstractSheetWriteHandler {

	public SingleSheetWriteHandler(ExcelConfigProperties configProperties,
								   ObjectProvider<List<Converter<?>>> converterProvider, WriterBuilderEnhancer excelWriterBuilderEnhance) {
		super(configProperties, converterProvider, excelWriterBuilderEnhance);
	}

	/**
	 * obj 是List 且list不为空同时list中的元素不是是List 才返回true
	 * @param obj 返回对象
	 * @return boolean
	 */
	@Override
	public boolean support(Object obj) {
		if (obj instanceof List) {
			List<?> objList = (List<?>) obj;
			return !objList.isEmpty() && !(objList.get(0) instanceof List);
		}
		else {
			throw new ExcelException("@ResponseExcel 返回值必须为List类型");
		}
	}

	@Override
	public void write(Object obj, HttpServletResponse response, ResponseExcel responseExcel) {
		List<?> list = (List<?>) obj;
		ExcelWriter excelWriter = getExcelWriter(response, responseExcel);

		// 有模板则不指定sheet名
		Class<?> dataClass = list.get(0).getClass();
		WriteSheet sheet = this.sheet(responseExcel.sheets()[0], dataClass, responseExcel.template(),
				responseExcel.headGenerator());
		excelWriter.write(list, sheet);
		excelWriter.finish();
	}

}
