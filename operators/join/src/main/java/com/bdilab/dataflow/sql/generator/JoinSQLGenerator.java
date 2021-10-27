package com.bdilab.dataflow.sql.generator;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.enums.ExceptionMsgEnum;
import com.bdilab.dataflow.common.exception.UncheckException;
import com.bdilab.dataflow.dto.JoinDescription;
import com.bdilab.dataflow.dto.JoinJson;
import com.bdilab.dataflow.operator.dto.jobdescription.SQLGeneratorBase;


/**
 * @author Yu Shaochao
 * @create: 2021-10-24
 * @description:
 * "JoinDescription": {
 *       "inputLeft": "test1",
 *       "inputRight": "test2",
 *       "joinType":"innerJoin",
 *       "joinKeys":[{"left":"id","right":"id"},{"left":"id2","right":"id2"}],
 *       "includePrefixes":"false",
 *       "leftPrefix":"left_",
 *       "rightPrefix":"right_"
 *  }
 */
public class JoinSQLGenerator extends SQLGeneratorBase {
    private JoinJson joinJson;
    private JoinDescription joinDescription;
    private String UUID;
    public JoinSQLGenerator(JoinJson joinJson, String UUID){
        super(joinJson);
        this.joinJson = joinJson;
        this.joinDescription = joinJson.getJoinDescription();
        this.UUID = UUID;
    }
    @Override
    public String project() {
        return "SELECT * ";
//        if(joinDescription.getIncludePrefixes().equals("false")){
//            return "SELECT * ";
//        }
//        else{
//
//        }
    }
    @Override
    public String datasource(){
        String inputLeft = joinDescription.getInputLeft();
        String inputRight = joinDescription.getInputRight();
        String joinType = joinDescription.getJointype();
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
    @Override
    public String generate() {
        //String prefix = "CREATE VIEW dataflow." + UUID + " AS ";
        String prefix = "";
        return prefix+sql()+super.limit();
    }

}
