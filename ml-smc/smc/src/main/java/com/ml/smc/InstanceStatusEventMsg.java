package com.ml.smc;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 实例状态变更事件消息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年10月22日 下午4:09:33
 */
@Setter
@Getter
public class InstanceStatusEventMsg {
    private String appCode;
    private String businessId;
    private String businessType;
    private String msgTitle;
    private String msgContent;
    private String dingdingUrl;
}
