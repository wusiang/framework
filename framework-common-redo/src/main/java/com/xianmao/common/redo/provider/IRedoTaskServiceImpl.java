package com.xianmao.common.redo.provider;

import com.xianmao.common.redo.provider.entity.RedoTaskDO;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
public class IRedoTaskServiceImpl implements IRedoTaskService {

    private JdbcTemplate jdbcTemplate;

    @Override
    public int insert(RedoTaskDO redoTask) {
        String sql = "insert into tbl_redotask(redo_task_id,application_name,max_attempts,try_forever,expired_date,req_param,create_time)values(?,?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        return jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, redoTask.getRedoTaskId());
                ps.setString(2, redoTask.getApplicationName());
                ps.setInt(3, redoTask.getMaxAttempts());
                ps.setBoolean(4, redoTask.isTryForever());
                ps.setDate(5, redoTask.getExpiredDate());
                ps.setString(6, redoTask.getReqParam());
                ps.setDate(7, redoTask.getCreateTime());
                return null;
            }
        }, keyHolder);
    }

    @Override
    public List<RedoTaskDO> selectRedoTaskList(String applicationName) {
        String sql = "select redo_task_id,application_name,max_attempts,try_forever,expired_date,req_param,create_time where application_name=?";
        jdbcTemplate.queryForObject(sql, new Object[]{applicationName}, (rs, rowNum) -> {
            RedoTaskDO redoTaskDO = new RedoTaskDO();
            redoTaskDO.setRedoTaskId(rs.getString("redo_task_id"));
            redoTaskDO.setApplicationName(rs.getString("application_name"));
            redoTaskDO.setMaxAttempts(rs.getInt("max_attempts"));
            redoTaskDO.setTryForever(rs.getBoolean("try_forever"));
            redoTaskDO.setExpiredDate(rs.getDate("expired_date"));
            redoTaskDO.setReqParam(rs.getString("req_param"));
            redoTaskDO.setCreateTime(rs.getDate("create_time"));
            return redoTaskDO;
        });
        return null;
    }

    @Override
    public void delete(String redoTaskId) {

    }

    @Override
    public void updateExecTimes(String redoTaskId) {

    }
}
