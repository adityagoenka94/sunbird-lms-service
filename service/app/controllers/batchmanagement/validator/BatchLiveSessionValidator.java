package controllers.batchmanagement.validator;

import org.apache.commons.lang.StringUtils;
import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.util.CaminoJsonKey;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.request.BaseRequestValidator;
import org.sunbird.common.request.Request;
import org.sunbird.common.responsecode.ResponseCode;

import java.text.MessageFormat;

public class BatchLiveSessionValidator extends BaseRequestValidator {

    /**
     * Validates request of create Batch Live Session API.
     *
     * @param request Request containing following parameters: batchId: The batchId of the course,
     *                unitId: The unitId of the course,
     *                contentId: The contentId of the live Session Content,
     *                startTime: The start time of the Live Session,
     *                endTime: The end time of the live Session,
     *                liveSessionUrl: The URL of the Live Session.
     */
    // Validation for Two Mandatory Parameters : homeUrl and tenantPreferenceDetails
    public void validateCreateBatchLiveSessionRequest(Request request) {

        validateParam(
                (request.getRequest().get(JsonKey.BATCH_ID)).toString(),
                ResponseCode.mandatoryParamsMissing,
                JsonKey.BATCH_ID);
        validateParam(
                (String)request.getRequest().get(CaminoJsonKey.UNIT_ID),
                ResponseCode.mandatoryParamsMissing,
                CaminoJsonKey.SESSION_DETAILS);
        validateParam(
                (String) request.getRequest().get(JsonKey.CONTENT_ID),
                ResponseCode.mandatoryParamsMissing,
                JsonKey.CONTENT_ID);
        validateParam(
                (String) request.getRequest().get(JsonKey.START_TIME),
                ResponseCode.mandatoryParamsMissing,
                JsonKey.START_TIME);
        validateParam(
                (String) request.getRequest().get(JsonKey.END_TIME),
                ResponseCode.mandatoryParamsMissing,
                JsonKey.END_TIME);
        validateParam(
                (String) request.getRequest().get(CaminoJsonKey.LIVE_SESSION_URL),
                ResponseCode.mandatoryParamsMissing,
                CaminoJsonKey.LIVE_SESSION_URL);

    }

    /**
     * Validates request of update Batch Live Session API.
     *
     * @param request Request containing following parameters: liveSessionId or contentId: The ID of the Content's Live Session or
     *               the contentId of the Live Session resource which needs to be updated.
     */
    // Validation for Mandatory Parameter : id (of Tenant_Info)
    public void validateUpdateBatchLiveSessionRequest(Request request) {

        if (!StringUtils.isBlank((String) request.getRequest().get(CaminoJsonKey.LIVE_SESSION_ID))) {
        } else if (!StringUtils.isBlank((String) request.getRequest().get(JsonKey.CONTENT_ID))) {
        } else {
            throw new ProjectCommonException(
                    ResponseCode.mandatoryParamsMissing.getErrorCode(),
                    MessageFormat.format(ResponseCode.mandatoryParamsMissing.getErrorMessage(), CaminoJsonKey.LIVE_SESSION_ID, " of the Content's Live Session or ", JsonKey.CONTENT_ID),
                    ResponseCode.CLIENT_ERROR.getResponseCode());
        }
    }




}
