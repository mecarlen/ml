package com.ml.base;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * 业务异常
 * 
 * 描述:
 *  1、非系统异常
 *  2、业务上预见异常
 *  3、非底层框架抛出的异常
 *  4、依赖服务异常
 * </pre>
 * 
 * @author mecarlen 2017年11月21日 下午5:39:58
 */
public class BussinessException extends RuntimeException {

    private static final long serialVersionUID = 3228559470335399309L;
    private int errorCode;
    private String errorMsg;

    public BussinessException() {
        super();
    }

    public BussinessException(BizErrorCode code) {
        super(code.alias);
        this.errorCode = code.code();
    }

    public BussinessException(BizErrorCode code, String errorMsg) {
        super(code.alias);
        this.errorCode = code.code();
        this.errorMsg = errorMsg;
    }

    public BussinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BussinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BussinessException(String message) {
        super(message);
    }

    public BussinessException(Throwable cause) {
        super(cause);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * <pre>
     * 业务异常编码
     * 
     * 业务异常以4开头，从400开始，如，401,4101,41001等
     * </pre>
     */
    public static enum BizErrorCode {
        /** 10-中继号不存在 */
        SERVER_NUMBER_NOTEXIST(422, "中继号不存在"),
        /** 11-中继号未开通 */
        SERVER_NUMBER_NOOPEN(423, "中继号未开通"),
        /** 12-号码所属的企业不存在 */
        COMPANY_NOTEXIST(424,"号码所属的企业不存在"),
        /** 13-号码所属的企业无效状态 */
        COMPANY_STATUS_INVALID(425,"号码所属的企业无效状态"),
        /** 14-号码所属的代理商不存在 */
        AGENT_NOTEXISTS(426,"号码所属的代理商不存在"),
        /** 15-号码所属的代理商无效状态 */
        AGENT_STATUS_INVALID(427,"号码所属的代理商无效状态"),
        /** 16-号码所属的代理商账户欠费停用状态 */
        AGENT_ACCOUNT_DISABLE(428,"号码所属的代理商账户欠费停用状态"),
        /** 17-企业试用额度已经用完*/
        COMPANY_TRYOUT_QUOTA_EXHAUSTED(429,"企业试用额度已经用完"),
        /** 421-拦截呼叫国际长途 */
        INTERCEPTED_CALL_IDD(421,"呼叫国际长途被拦截"),
        /** 420-风控拦截 */
        RISK_INTERCEPTED(420,"呼叫被风控拦截"),
        /** 410- 项目或企业未配置兜底号码 */
        UNCONFIG_BOTTOM_PHON(410, "企业{vccId:%d}或项目{projId:%d}未配置兜底号码"),
        /** 402-非法参数 */
        INVALID_PARAM(402, "invalid parameters"),
        /** 401-未经认证 */
        UN_CERTIFIED(401, "uncertified"),
        /** 400-默认 */
        DEFAULT(400, "business exception"),

        SUCCESS(0, "success");

        final private int code;
        final private String alias;
        private static Map<Integer, BizErrorCode> BIZ_ERROR_CODE = new HashMap<>();
        static {
            for (BizErrorCode code : BizErrorCode.values()) {
                BIZ_ERROR_CODE.put(code.code, code);
            }
        }

        private BizErrorCode(final int code, String alias) {
            this.code = code;
            this.alias = alias;
        }

        public int code() {
            return code;
        }

        public String alias() {
            return alias;
        }

        final public static BizErrorCode getInstance(int bizErrorCode) {
            return BIZ_ERROR_CODE.get(bizErrorCode);
        }
    }

}
