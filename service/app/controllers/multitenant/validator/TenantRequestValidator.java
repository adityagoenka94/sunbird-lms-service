package controllers.multitenant.validator;

import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.util.CaminoJsonKey;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.request.BaseRequestValidator;
import org.sunbird.common.request.Request;
import org.sunbird.common.responsecode.ResponseCode;

import java.text.MessageFormat;

public class TenantRequestValidator extends BaseRequestValidator {

    /**
     * Validates request of create Tenant API.
     *
     * @param request Request containing following parameters: homeUrl: The homeUrl of the organisation
     *                for which Tenant is to be created and tenantPreferenceDetails containing Multi-Tenant themes.
     */
    // Validation for Two Mandatory Parameters : homeUrl and tenantPreferenceDetails
    public void validateCreateTenantRequest(Request request) {

        validateParam(
                (String) request.getRequest().get(JsonKey.HOME_URL),
                ResponseCode.mandatoryParamsMissing,
                JsonKey.HOME_URL);
        if (request.getRequest().get(CaminoJsonKey.TENANT_PREFERENCE_DETAILS)==null) {
            throw new ProjectCommonException(
                    ResponseCode.mandatoryParamsMissing.getErrorCode(),
                    MessageFormat.format(ResponseCode.mandatoryParamsMissing.getErrorMessage(), CaminoJsonKey.TENANT_PREFERENCE_DETAILS),
                    ResponseCode.CLIENT_ERROR.getResponseCode());
        }

    }

    /**
     * Validates request of update Tenant Info API.
     *
     * @param request Request containing following parameters: tenantInfoId as id: The ID of the Tenant Info.
     */
    // Validation for Mandatory Parameter : id (of Tenant_Info)
    public void validateUpdateTenantInfoRequest(Request request) {

        validateParam(
                (String) request.getRequest().get(JsonKey.ID),
                ResponseCode.mandatoryParamsMissing,
                JsonKey.ID);
    }

    /**
     * Validates request of update Tenant Preference Details API.
     *
     * @param request Request containing following parameters: tenantPreferenceDetailsId as id: The ID of the Tenant Preference Detail.
     */
    // Validation for Mandatory Parameter : id (of Tenant_Preference_Details)
    public void validateUpdateTenantPreferenceDetailsRequest(Request request) {

        validateParam(
                (String) request.getRequest().get(JsonKey.ID),
                ResponseCode.mandatoryParamsMissing,
                JsonKey.ID);
    }

    /**
     * Validates request of get Tenant API.
     *
     * @param request Request containing following parameters: homeUrl: The Home Url of the Organisation.
     */
    public void validateGetTenantDetails(Request request) {

        validateParam(
                (String) request.getRequest().get(JsonKey.HOME_URL),
                ResponseCode.mandatoryParamsMissing,
                JsonKey.HOME_URL);
    }



}
