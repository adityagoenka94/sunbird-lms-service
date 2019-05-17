package controllers.multitenant;

import controllers.BaseControllerTest;
import org.junit.Ignore;
import org.junit.Test;
import org.sunbird.common.models.util.CaminoJsonKey;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.responsecode.ResponseCode;
import play.mvc.Result;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@Ignore
public class TenantControllerTest extends BaseControllerTest {

    private static String orgHomeUrl = "someOrgHomeUrl";
    private static String preferenceDetails = "someTenantPreferenceDetails";
    private static String tenantInfoId = "someTenantInfoId";
    private static String tenantPreferenceDetailsId = "someTenantPreferenceDetailsId";

    @Test
    public void testCreateTenantSuccess() {
        Result result =
                performTest(
                        "/v1/tenant/create",
                        "POST",
                        createAndUpdateTenantRequest(orgHomeUrl,preferenceDetails, null, null));
        assertEquals(getResponseCode(result), ResponseCode.success.getErrorCode().toLowerCase());
        assertTrue(getResponseStatus(result) == 200);
    }

    @Test
    public void testCreateTenantSuccessWithEmptyTenantPreferenceDetails() {
        Result result =
                performTest(
                        "/v1/tenant/create",
                        "POST",
                        createAndUpdateTenantRequest(orgHomeUrl,"", null, null));
        assertEquals(getResponseCode(result), ResponseCode.success.getErrorCode().toLowerCase());
        assertTrue(getResponseStatus(result) == 200);
    }

    @Test
    public void testCreateTenantFailureWithoutHomeUrl() {
        Result result =
                performTest(
                        "/v1/tenant/create",
                        "POST",
                        createAndUpdateTenantRequest(null,preferenceDetails, null, null));
        assertEquals(getResponseCode(result), ResponseCode.mandatoryParamsMissing.getErrorCode());
        assertTrue(getResponseStatus(result) == 400);
    }

    @Test
    public void testCreateTenantFailureWithHomeUrlWithoutTenantPreferenceDetails() {
        Result result =
                performTest(
                        "/v1/tenant/create",
                        "POST",
                        createAndUpdateTenantRequest(orgHomeUrl,null,null, null));
        assertEquals(getResponseCode(result), ResponseCode.mandatoryParamsMissing.getErrorCode());
        assertTrue(getResponseStatus(result) == 400);
    }
    
    @Test
    public void testUpdateTenantInfoSuccess() {
        Result result =
                performTest(
                        "/v1/tenant/info/update",
                        "PATCH",
                        createAndUpdateTenantRequest(null, null, tenantInfoId, null));
        assertEquals(getResponseCode(result), ResponseCode.success.getErrorCode().toLowerCase());
        assertTrue(getResponseStatus(result) == 200);
    }

    @Test
    public void testUpdateTenantInfoFailure() {
        Result result =
                performTest(
                        "/v1/tenant/info/update",
                        "PATCH",
                        createAndUpdateTenantRequest(null,null,null, null));
        assertEquals(getResponseCode(result), ResponseCode.mandatoryParamsMissing.getErrorCode());
        assertTrue(getResponseStatus(result) == 400);
    }

    @Test
    public void testUpdateTenantPreferenceDetailsSuccess() {
        Result result =
                performTest(
                        "/v1/tenant/preference/update",
                        "PATCH",
                        createAndUpdateTenantRequest(null, preferenceDetails, null, tenantPreferenceDetailsId));
        assertEquals(getResponseCode(result), ResponseCode.success.getErrorCode().toLowerCase());
        assertTrue(getResponseStatus(result) == 200);
    }

    public void testUpdateTenantPreferenceDetailsFailureWithTenantPreferenceIdWithoutTenantPreferenceNewDetails() {
        Result result =
                performTest(
                        "/v1/tenant/preference/update",
                        "POST",
                        createAndUpdateTenantRequest(null,null,null, tenantPreferenceDetailsId));
        assertEquals(getResponseCode(result), ResponseCode.mandatoryParamsMissing.getErrorCode());
        assertTrue(getResponseStatus(result) == 400);
    }

    public void testUpdateTenantPreferenceDetailsFailureWithTenantPreferenceNewDetailsWithoutTenantPreferenceId() {
        Result result =
                performTest(
                        "/v1/tenant/preference/update",
                        "PATCH",
                        createAndUpdateTenantRequest(null,preferenceDetails,null, null));
        assertEquals(getResponseCode(result), ResponseCode.mandatoryParamsMissing.getErrorCode());
        assertTrue(getResponseStatus(result) == 400);
    }

    @Test
    public void testGetTenantDetailsByHomeUrlSuccess() {
        Result result = performTest("/v1/tenant/read/" + orgHomeUrl, "GET", null);
        assertEquals(getResponseCode(result), ResponseCode.success.getErrorCode().toLowerCase());
        assertTrue(getResponseStatus(result) == 200);
    }

    @Test
    public void testAddTenantPreferenceDetailsSuccess() {
        Result result = performTest(
                "/v1/tenant/preference/add",
                "POST",
                createAndUpdateTenantRequest(orgHomeUrl,preferenceDetails,null, null));
        assertEquals(getResponseCode(result), ResponseCode.success.getErrorCode().toLowerCase());
        assertTrue(getResponseStatus(result) == 200);
    }

    @Test
    public void testAddTenantPreferenceDetailsFailureWithOrgHomeUrlWithoutTenantPreferenceDetails() {
        Result result = performTest(
                "/v1/tenant/preference/add",
                "POST",
                createAndUpdateTenantRequest(orgHomeUrl,null,null, null));
        assertEquals(getResponseCode(result), ResponseCode.mandatoryParamsMissing.getErrorCode());
        assertTrue(getResponseStatus(result) == 400);
    }

    private Map<String, Object> createAndUpdateTenantRequest(
            String orgHomeUrl,
            String tenantPreferenceDetails,
            String tenantInfoId,
            String tenantPreferenceDetailsId) {
        Map<String, Object> innerMap = new HashMap<>();
        Map<String, Object> requestMap = new HashMap<>();
        if(orgHomeUrl != null)
        innerMap.put(JsonKey.HOME_URL,orgHomeUrl);
        if(tenantInfoId != null)
        innerMap.put(CaminoJsonKey.TENANT_PREFERENCE_DETAILS,tenantPreferenceDetails);
        if(tenantPreferenceDetails != null)
            innerMap.put(CaminoJsonKey.TENANT_INFO_ID,tenantInfoId);
        if(tenantPreferenceDetailsId != null)
            innerMap.put(CaminoJsonKey.TENANT_PREFERENCE_DETAIL_ID,tenantPreferenceDetailsId);

        requestMap.put(JsonKey.REQUEST, innerMap);
        
        return requestMap;
}
}