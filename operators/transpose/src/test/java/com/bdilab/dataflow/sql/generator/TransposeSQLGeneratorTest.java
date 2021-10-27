package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.TransposeDescription;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Zunjing Chen
 * @create: 2021-10-25
 * @description:
 **/
public class TransposeSQLGeneratorTest {
    TransposeSQLGenerator transposeSQLGenerator;
    @Before
    void init(){
        List<String> columnValues = new ArrayList<>();
    }
    @Test
    void construct(){
        TransposeDescription td1 = new TransposeDescription("transpose","student",2000);
//        td1.setColumn();
//        td1.setGroupCol();
//        td1.setGroupFunc();
//        td1.setAttribute();
//        td1.setColumnIsNumeric(false);
    }
    @Test
    void appendGroupColumnsTest(){

    }
}
