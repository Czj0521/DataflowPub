package com.bdilab.dataflow.dto.jobdescription;

import lombok.Data;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-18
 * @description:
 * 	"TableDescription":{
 * 			"filter":"(age>30 AND name = 'jack' AND startWith(street,"czk"))",
 * 			"project":["name","age","max(age)"],
 * 			"group":["name","age"],
 * 			"limit":2000
 *        }
 **/
@Data
public class TableDescription  extends  JobDescription{
    String filter;
    String[] project;
    String[] group;
}
