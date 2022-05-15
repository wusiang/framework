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

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xianmao.common.enums.DeleteEnum;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 业务封装基础类
 *
 * @param <M> mapper
 * @param <T> model
 * @author Chill
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T> {

    @Override
    public boolean deleteLogic(List<Long> ids) {
        if (null == ids || CollectionUtils.isEmpty(ids)) {
            throw new RuntimeException("ids must not empty");
        }
        List<T> tList = this.listByIds(ids);
        if (!CollectionUtils.isEmpty(tList)) {
            tList.forEach(t -> t.setDelFlag(DeleteEnum.YES.getCode()));
            this.updateBatchById(tList);
        }
        return true;
    }

    @Override
    public boolean save(T entity) {
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        entity.setDelFlag(DeleteEnum.NO.getCode());
        return super.save(entity);
    }

    @Override
    public boolean updateById(T entity) {
        entity.setUpdateTime(LocalDateTime.now());
        return super.updateById(entity);
    }

}
