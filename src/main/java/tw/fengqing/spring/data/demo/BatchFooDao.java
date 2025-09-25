package tw.fengqing.spring.data.demo;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

@Repository
public class BatchFooDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BatchFooDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void batchInsert() {
        // 使用JdbcTemplate進行批次插入操作，這裡示範插入兩筆資料到FOO表格
        jdbcTemplate.batchUpdate("INSERT INTO FOO (BAR) VALUES (?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                        // 設定每一筆資料的BAR欄位值，分別為b-0與b-1
                        ps.setString(1, "b-" + i);
                    }

                    @Override
                    public int getBatchSize() {
                        // 批次數量設定為2，代表會執行兩次insert
                        return 2;
                    }
                });

        // 使用NamedParameterJdbcTemplate進行批次插入操作
        // 準備要插入的Foo物件清單，這裡示範插入兩筆資料，ID分別為100與101
        List<Foo> list = new ArrayList<>();
        list.add(Foo.builder().id(100L).bar("b-100").build());
        list.add(Foo.builder().id(101L).bar("b-101").build());
        // 執行批次插入，將list中的每一個Foo物件對應到SQL中的:id與:bar參數
        namedParameterJdbcTemplate
                .batchUpdate("INSERT INTO FOO (ID, BAR) VALUES (:id, :bar)",
                        SqlParameterSourceUtils.createBatch(list));
    }
}
