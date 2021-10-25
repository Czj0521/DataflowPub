package com.bdilab.dataflow;

import com.bdilab.dataflow.dto.jobdescription.TableDescription;
import com.bdilab.dataflow.service.impl.TableJobServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @description: UT of the Table operator
 * @author:zhb
 * @createTime:2021/10/25 12:28
 */

@SpringBootTest
public class TableTest {
    @Autowired
    TableJobServiceImpl tableJobService;

    @Test
    public void table() {
        TableDescription tableDescription = new TableDescription();
        tableDescription.setDataSource("dataflow.airuuid");
        tableDescription.setFilter("");
        tableDescription.setGroup(new String[]{});
        tableDescription.setJobType("");
        tableDescription.setLimit(20);
        tableDescription.setProject(new String[]{"city"});

        long before = System.currentTimeMillis();
        System.out.println(tableJobService.table(tableDescription));
        long after = System.currentTimeMillis();

        System.out.println("Time spent :" + (after - before));
    }
}
