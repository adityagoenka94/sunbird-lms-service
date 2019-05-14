package controllers.multitenant.validator;

import org.apache.commons.lang.StringUtils;
import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.util.CaminoJsonKey;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.request.BaseRequestValidator;
import org.sunbird.common.request.Request;
import org.sunbird.common.responsecode.ResponseCode;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;

public class TenantRequestValidator extends BaseRequestValidator {
    private static final int ERROR_CODE = ResponseCode.CLIENT_ERROR.getResponseCode();

    // Validation for Two Mandatory Parameters : homeUrl and tenantPreferenceDetails
    public void validateCreateTenantRequest(Request request) {

        validateParam(
                (String) request.getRequest().get(JsonKey.HOME_URL),
                ResponseCode.mandatoryParamsMissing,
                JsonKey.HOME_URL);
//                Map<String,Object> tenantPref=(LinkedHashMap) request.getRequest().get(CaminoJsonKey.TENANT_PREFERENCE_DETAILS);
        if (request.getRequest().get(CaminoJsonKey.TENANT_PREFERENCE_DETAILS)==null) {
            throw new ProjectCommonException(
                    ResponseCode.mandatoryParamsMissing.getErrorCode(),
                    MessageFormat.format(ResponseCode.mandatoryParamsMissing.getErrorMessage(), CaminoJsonKey.TENANT_PREFERENCE_DETAILS),
                    ResponseCode.CLIENT_ERROR.getResponseCode());
        }

    }

    // Validation for Mandatory Parameter : id (of Tenant_Info)
    public void validateUpdateTenantInfoRequest(Request request) {

        validateParam(
                (String) request.getRequest().get(JsonKey.ID),
                ResponseCode.mandatoryParamsMissing,
                JsonKey.ID);
    }

    // Validation for Mandatory Parameter : id (of Tenant_Preference_Details)
    public void validateUpdateTenantPreferenceDetailsRequest(Request request) {

        validateParam(
                (String) request.getRequest().get(JsonKey.ID),
                ResponseCode.mandatoryParamsMissing,
                JsonKey.ID);
    }



}
