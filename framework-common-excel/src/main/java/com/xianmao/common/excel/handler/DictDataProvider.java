package com.xianmao.common.excel.handler;


import com.xianmao.common.excel.vo.DictEnum;

/**
 * dict 数据提供程序
 */
public interface DictDataProvider {

	/**
	 * 获取 dict
	 * @param type 类型
	 * @return {@link DictEnum[] }
	 */
	DictEnum[] getDict(String type);

}
