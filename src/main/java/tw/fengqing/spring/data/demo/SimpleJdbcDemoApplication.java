package tw.fengqing.spring.data.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@SpringBootApplication
@Slf4j
public class SimpleJdbcDemoApplication implements CommandLineRunner {
    @Autowired
    private FooDao fooDao;
    @Autowired
    private BatchFooDao batchFooDao; // 演示2批次操作範例

    public static void main(String[] args) {
        SpringApplication.run(SimpleJdbcDemoApplication.class, args);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void run(String... args) throws Exception {
        // 演示1使用JdbcTemplate執行單條SQL語句
        // fooDao.insertData();
        // 演示2使用SimpleJdbcInsert執行批次SQL語句
        batchFooDao.batchInsert();
        fooDao.listData();
    } 

}

