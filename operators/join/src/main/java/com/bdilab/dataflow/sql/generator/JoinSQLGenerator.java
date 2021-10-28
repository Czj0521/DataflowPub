package com.bdilab.dataflow.sql.generator;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.enums.ExceptionMsgEnum;
import com.bdilab.dataflow.common.exception.UncheckException;
import com.bdilab.dataflow.dto.JoinDescription;
import com.bdilab.dataflow.service.impl.TableMetadataServiceImpl;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


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
@Service
public class JoinSQLGenerator{
    @Autowired
    TableMetadataServiceImpl tableMetadataService;

    public String project(JoinDescription joinDescription) {
        String inputLeft = joinDescription.getLeftDataSource();
        String inputRight = joinDescription.getRightDataSource();
        String leftPrefix = joinDescription.getLeftPrefix();
        String rightPrefix = joinDescription.getRightPrefix();
        Set<String> joinkeysRight = new HashSet<>();
        for(JSONObject jsonObject : joinDescription.getJoinKeys()){
            joinkeysRight.add((String)jsonObject.get("right"));
        }
        if(joinDescription.getIncludePrefixes().equals("false")){
            leftPrefix = "";
            rightPrefix = "";
        }


        Set<String> leftColumnSet= tableMetadataService.metadata("SELECT * FROM "+inputLeft).keySet();
        Set<String> rightColumnSet= tableMetadataService.metadata("SELECT * FROM "+inputRight).keySet();

        StringBuilder selectString = new StringBuilder("SELECT ");

        for(String str: leftColumnSet){
            selectString.append(" ds1."+str+" "+leftPrefix+str+",");
        }
        for(String str:rightColumnSet){
            if(!joinkeysRight.contains(str) &&
                    (!(leftColumnSet.contains(str)&&leftPrefix.equals(rightPrefix)))){
                selectString.append(" ds2."+str+" "+rightPrefix+str+",");
            }
        }
        return new String(selectString).substring(0,selectString.length()-1);
    }

    public String datasource(JoinDescription joinDescription){
        String inputLeft = joinDescription.getLeftDataSource();
        String inputRight = joinDescription.getRightDataSource();
        String joinType = joinDescription.getJoinType();
        if(inputLeft == null|| inputRight == null || joinType == null){
            throw new UncheckException(ExceptionMsgEnum.TABLE_SQL_PARSE_ERROR.getMsg());
        }
        return  " FROM "+inputLeft+"  ds1 "+joinType+"  "+inputRight+"  ds2 ";
    }

    public String on(JoinDescription joinDescription) {
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
    public  String sql(JoinDescription joinDescription){
        return project(joinDescription)+datasource(joinDescription)+on(joinDescription);
    }
    public String generate(JoinDescription joinDescription) {
        //String prefix = "CREATE VIEW dataflow." + UUID + " AS ";
        String prefix = "";
        //return prefix+sql()+super.limit();
        return prefix+sql(joinDescription);
    }
}
