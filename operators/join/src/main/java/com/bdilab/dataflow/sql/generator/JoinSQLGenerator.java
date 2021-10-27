package com.bdilab.dataflow.sql.generator;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.enums.ExceptionMsgEnum;
import com.bdilab.dataflow.common.exception.UncheckException;
import com.bdilab.dataflow.dto.JoinDescription;
import com.bdilab.dataflow.dto.JoinJson;


/**
 * @author Yu Shaochao
 * @create: 2021-10-24
 * @description:
 * "JoinDescription": {
 *       "jobType":"join"
 *       "leftDataSource": "test1",
 *       "rightDataSource": "test2",
 *       "joinType":"innerJoin",
 *       "joinKeys":[{"left":"id","right":"id"},{"left":"id2","right":"id2"}],
 *       "includePrefixes":"false",
 *       "leftPrefix":"left_",
 *       "rightPrefix":"right_"
 *  }
 */
public class JoinSQLGenerator{
    private JoinDescription joinDescription;
    private String UUID;
    public JoinSQLGenerator(JoinJson joinJson, String UUID){
        this.joinDescription = joinJson.getJoinDescription();
        this.UUID = UUID;
    }
    public String project() {
        return "SELECT * ";
//        if(joinDescription.getIncludePrefixes().equals("false")){
//            return "SELECT * ";
//        }
//        else{
//
//        }
    }

    public String datasource(){
        String inputLeft = joinDescription.getLeftDataSource();
        String inputRight = joinDescription.getRightDataSource();
        String joinType = joinDescription.getJoinType();
        if(inputLeft == null|| inputRight == null || joinType == null){
            throw new UncheckException(ExceptionMsgEnum.TABLE_SQL_PARSE_ERROR.getMsg());
        }
        return  " FROM "+inputLeft+"  ds1 "+joinType+"  "+inputRight+"  ds2 ";
    }

    public String on() {
        JSONObject[] joinKeys = joinDescription.getJoinKeys();
        StringBuilder onString = new StringBuilder(" ON ");
        for(JSONObject joinKey : joinKeys){
            onString.append(" ds1."+joinKey.get("left"));
            onString.append(" = ");
            onString.append(" ds2."+joinKey.get("right"));
            if(!joinKey.equals(joinKeys[joinKeys.length-1])){
                onString.append(" AND");
            }
        }
        return  new String(onString);
    }

    public  String sql(){
        return project()+datasource()+on();
    }

    public String generate() {
        //String prefix = "CREATE VIEW dataflow." + UUID + " AS ";
        String prefix = "";
        //return prefix+sql()+super.limit();
        return prefix+sql();
    }

}
