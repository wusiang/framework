package com.xianmao.common.core.geo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 坐标点
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeoPoint implements Serializable {

	/**
	 * 经度
	 */
	private double lon;
	/**
	 * 纬度
	 */
	private double lat;

	/**
	 * 当前坐标偏移指定坐标
	 *
	 * @param offset 偏移量
	 * @return this
	 */
	public GeoPoint offset(GeoPoint offset) {
		this.lon += offset.lon;
		this.lat += offset.lat;
		return this;
	}

}
