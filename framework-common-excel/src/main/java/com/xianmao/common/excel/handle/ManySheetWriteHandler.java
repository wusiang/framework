package com.xianmao.common.excel.handle;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.xianmao.common.excel.annotation.ResponseExcel;
import com.xianmao.common.excel.annotation.Sheet;
import com.xianmao.common.excel.config.ExcelConfigProperties;
import com.xianmao.common.excel.enhance.WriterBuilderEnhancer;
import com.xianmao.common.excel.kit.ExcelException;
import org.springframework.beans.factory.ObjectProvider;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ManySheetWriteHandler extends AbstractSheetWriteHandler {

	public ManySheetWriteHandler(ExcelConfigProperties configProperties,
								 ObjectProvider<List<Converter<?>>> converterProvider, WriterBuilderEnhancer excelWriterBuilderEnhance) {
		super(configProperties, converterProvider, excelWriterBuilderEnhance);
	}

	/**
	 * 当且仅当List不为空且List中的元素也是List 才返回true
	 * @param obj 返回对象
	 * @return boolean
	 */
	@Override
	public boolean support(Object obj) {
		if (obj instanceof List) {
			List<?> objList = (List<?>) obj;
			return !objList.isEmpty() && objList.get(0) instanceof List;
		}
		else {
			throw new ExcelException("@ResponseExcel 返回值必须为List类型");
		}
	}

	@Override
	public void write(Object obj, HttpServletResponse response, ResponseExcel responseExcel) {
		List<?> objList = (List<?>) obj;
		ExcelWriter excelWriter = getExcelWriter(response, responseExcel);

		Sheet[] sheets = responseExcel.sheets();
		WriteSheet sheet;
		for (int i = 0; i < sheets.length; i++) {
			List<?> eleList = (List<?>) objList.get(i);
			Class<?> dataClass = eleList.get(0).getClass();
			// 创建sheet
			sheet = this.sheet(sheets[i], dataClass, responseExcel.template(), responseExcel.headGenerator());
			// 写入sheet
			excelWriter.write(eleList, sheet);
		}
		excelWriter.finish();
	}

}
