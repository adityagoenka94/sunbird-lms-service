package controllers.multitenant.validator;

import org.apache.commons.lang.StringUtils;
import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.util.CaminoJsonKey;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.request.BaseRequestValidator;
import org.sunbird.common.request.Request;
import org.sunbird.common.responsecode.ResponseCode;

import java.text.MessageFormat;
public class TenantRequestValidator extends BaseRequestValidator {

    /**
     * Validates request of create Multi Tenant API.
     *
     * @param request Request containing following parameters: homeUrl(mandatory): The homeUrl of the organisation
     *                for which Tenant is to be created , tenantPreferenceDetails(mandatory): containing Multi-Tenant themes and
     *                framework: containing framework of the organisation.
     */
    // Validation for Two Mandatory Parameters : homeUrl and tenantPreferenceDetails
    public void validateCreateMultiTenantInfoRequest(Request request) {

        validateParam(
                (String) request.getRequest().get(JsonKey.HOME_URL),
                ResponseCode.mandatoryParamsMissing,
                JsonKey.HOME_URL);
        if (request.getRequest().get(CaminoJsonKey.PREFERENCE_DETAILS)==null) {
            throw new ProjectCommonException(
                    ResponseCode.mandatoryParamsMissing.getErrorCode(),
                    MessageFormat.format(ResponseCode.mandatoryParamsMissing.getErrorMessage(), CaminoJsonKey.PREFERENCE_DETAILS),
                    ResponseCode.CLIENT_ERROR.getResponseCode());
        }

    }

    /**
     * Validates request of update Multi Tenant Info API.
     *
     * @param request Request containing following parameters: tenantInfoId(mandatory): The ID of the Tenant Info,
     *                tenantPreferenceDetails and framework : if updation is needed.
     */
    // Validation for Mandatory Parameter : id (of Tenant_Info)
    public void validateUpdateMultiTenantInfoRequest(Request request) {

        validateParam(
                (String) request.getRequest().get(CaminoJsonKey.TENANT_INFO_ID),
                ResponseCode.mandatoryParamsMissing,
                CaminoJsonKey.TENANT_INFO_ID);
    }


    /**
     * Validates request of get Multi Tenant API.
     *
     * @param homeUrl,orgId : homeUrl or organisationId: The Home Url or ID of the Organisation.
     *                      homeUrl has higher presedence over organisationId.
     */
    public String[] validateGetMultiTenantInfoRequest(String homeUrl, String orgId) {

        String[] search=new String[2];

        if(!StringUtils.isBlank(homeUrl)) {
            search[0]=JsonKey.HOME_URL;
            search[1]=homeUrl;
        }
        else if(!StringUtils.isBlank(orgId)) {
            search[0]=JsonKey.ORGANISATION_ID;
            search[1]=orgId;
        }
        else {
            throw new ProjectCommonException(
                    ResponseCode.mandatoryParamsMissing.getErrorCode(),
                    MessageFormat.format(ResponseCode.mandatoryParamsMissing.getErrorMessage(), JsonKey.HOME_URL+" or "+JsonKey.ORGANISATION_ID),
                    ResponseCode.CLIENT_ERROR.getResponseCode());
        }
        return search;
    }



}
