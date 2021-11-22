package com.bdilab.dataflow.dto.joboutputjson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : [zhangpeiliang]
 * @description : [参数类型响应对象]
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamTypeRespObj {

    private List<String> String;

    private List<String> Boolean;

    private List<String> Date;

    private List<String> Numeric;
}
