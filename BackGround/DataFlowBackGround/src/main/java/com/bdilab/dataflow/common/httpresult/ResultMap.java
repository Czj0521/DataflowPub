package com.bdilab.dataflow.common.httpresult;


import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static com.bdilab.dataflow.common.httpresult.HttpResultConstants.EMPTY;


/**
 * @author wh
 * @version 1.0
 * @date 2021/09/12
 */
public class ResultMap extends HashMap<String, Object> {

    private HashMap<String, Object> header;

    private int code = HttpCodeEnum.OK.getCode();
    //private TokenUtils tokenUtils;
    public ResultMap() {
    }

    public ResultMap success() {
        this.code = HttpCodeEnum.OK.getCode();
        this.header = new HashMap<>();
        this.header.put("code", this.code);
        this.header.put("msg", "Success");
        this.put("header", header);
        this.put("payload", EMPTY);
        return this;
    }

    /*public ResultMap success(String token) {
        success();
        this.header.put("token", token);
        return this;
    }
*/
   /* public ResultMap successAndRefreshToken(HttpServletRequest request) {
        String token = request.getHeader(Constants.TOKEN_HEADER_STRING);
        return success(this.tokenUtils.refreshToken(token));
    }*/


    public ResultMap fail() {
        this.code = HttpCodeEnum.FAIL.getCode();
        this.header = new HashMap<>();
        this.header.put("code", code);
        this.put("header", header);
        this.put("payload", EMPTY);
        return this;
    }

    public ResultMap fail(int code) {
        this.code = code;
        this.header = new HashMap<>();
        this.header.put("code", code);
        this.put("header", header);
        this.put("payload", EMPTY);
        return this;
    }


/*    public ResultMap failWithToken(String token) {
        this.code = HttpCodeEnum.FAIL.getCode();
        this.header = new HashMap<>();
        this.header.put("code", code);
        this.header.put("msg", HttpCodeEnum.FAIL.getMessage());
        this.header.put("token", tokenUtils.refreshToken(token));
        this.put("header", header);
        this.put("payload", EMPTY);
        return this;
    }*/


   /* public ResultMap failAndRefreshToken(HttpServletRequest request) {
        this.code = HttpCodeEnum.FAIL.getCode();
        this.header = new HashMap<>();
        this.header.put("code", code);
        this.header.put("msg", HttpCodeEnum.FAIL.getMessage());

        String token = request.getHeader(Constants.TOKEN_HEADER_STRING);

        if (!StringUtils.isEmpty(token)) {
            this.header.put("token", this.tokenUtils.refreshToken(token));
        }
        this.put("header", header);
        this.put("payload", EMPTY);
        return this;
    }*/

   /* public ResultMap failAndRefreshToken(HttpServletRequest request, HttpCodeEnum httpCodeEnum) {
        this.code = httpCodeEnum.getCode();
        this.header = new HashMap<>();
        this.header.put("code", code);
        this.header.put("msg", httpCodeEnum.getMessage());

        String token = request.getHeader(Constants.TOKEN_HEADER_STRING);
        if (!StringUtils.isEmpty(token)) {
            this.header.put("token", this.tokenUtils.refreshToken(token));
        }

        this.put("header", header);
        this.put("payload", EMPTY);
        return this;
    }*/

    public ResultMap message(String message) {
        this.header.put("msg", message);
        this.put("header", header);
        return this;
    }

    public ResultMap payload(Object object) {
        this.put("payload", null == object ? EMPTY : object);
        return this;
    }

    public ResultMap payloads(Collection list) {
        this.put("payload", null == list ? new List[0] : list);
        return this;
    }

    public int getCode() {
        return code;
    }
}
