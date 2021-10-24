package com.bdilab.dataflow.common.httpresult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wh
 * @version 1.0
 * @date 2021/09/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class TokenDetail {
    public String username;
    public String password;
}
