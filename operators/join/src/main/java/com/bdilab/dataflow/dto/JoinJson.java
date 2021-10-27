package com.bdilab.dataflow.dto;

import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Yu Shaochao
 * @create: 2021-10-24
 * @description:
 * {
 *   "job": "start_job",
 *   "jobType": "join",
 *   "jobDescription": {
 *     "inputLeft": "test1",
 *     "inputRight": "test2",
 *     "joinType":"innerJoin",
 *     "joinKeys":[{"left":"id","right":"id"},{"left":"id2","right":"id2"}],
 *     "includePrefixes":"false",
 *     "leftPrefix":"left_",
 *     "rightPrefix":"right_"
 *   },
 *   "jobId":"1212",
 *   "workspaceId": "4f5s4f25s4g8z5eg"
 * }
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinJson{
    private String job;
    private JoinDescription joinDescription;
    private String jobId;
    private String workspaceId;

}