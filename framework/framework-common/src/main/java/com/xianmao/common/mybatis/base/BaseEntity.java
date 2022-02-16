/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xianmao.common.mybatis.base;


import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xianmao.common.date.DatePattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体类
 *
 * @author Chill
 */
public class BaseEntity implements Serializable {

	/**
	 * 创建时间
	 */
	@DateTimeFormat(pattern = DatePattern.YYYY_MM_DD_HH_MM_SS)
	@JsonFormat(pattern = DatePattern.YYYY_MM_DD_HH_MM_SS)
	private LocalDateTime createTime;
	/**
	 * 更新时间
	 */
	@DateTimeFormat(pattern = DatePattern.YYYY_MM_DD_HH_MM_SS)
	@JsonFormat(pattern = DatePattern.YYYY_MM_DD_HH_MM_SS)
	private LocalDateTime updateTime;

	/**
	 * 状态[0:未删除,1:删除]
	 */
	@TableLogic
	private Integer delFlag;

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
}
