package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.TransposeDescription;
import org.junit.jupiter.api.Test;

/**
 * @author: Zunjing Chen
 * @create: 2021-10-25
 * @description:
 **/
public class TransposeSQLGeneratorTest {
    @Test
    void construct(){
        TransposeDescription td1 = new TransposeDescription("transpose","student",2000);
//        td1.setColumn();
//        td1.setGroupCol();
//        td1.setGroupFunc();
//        td1.setAttribute();
//        td1.setColumnIsNumeric(false);
    }
}
