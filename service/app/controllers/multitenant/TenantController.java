package controllers.multitenant;

import controllers.BaseController;
import org.apache.commons.lang.StringUtils;
import org.sunbird.common.models.util.*;
import org.sunbird.common.request.Request;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Result;
import controllers.multitenant.validator.TenantRequestValidator;

import java.util.Map;

public class TenantController extends BaseController {

    public Promise<Result> createTenant() {
        ProjectLogger.log("createTenant called.", LoggerEnum.DEBUG.name());
        return handleRequest(
                CaminoActorOperations.CREATE_TENANT.getValue(),
                request().body().asJson(),
                (request) -> {
                    new TenantRequestValidator().validateCreateTenantRequest((Request) request);
                    return null;
                },
                getAllRequestHeaders(request()));
    }

    public Promise<Result> updateTenantInfo() {
        ProjectLogger.log("updateTenantInfo called.", LoggerEnum.DEBUG.name());
        return handleRequest(
                CaminoActorOperations.UPDATE_TENANT_INFO.getValue(),
                request().body().asJson(),
                orgRequest -> {
                    new TenantRequestValidator().validateUpdateTenantInfoRequest((Request) orgRequest);
                    return null;
                },
                getAllRequestHeaders(request()));
    }

    public Promise<Result> updateTenantPreferenceDetails() {
        ProjectLogger.log("updateTenantPreferenceDetails called.", LoggerEnum.DEBUG.name());
        return handleRequest(
                CaminoActorOperations.UPDATE_TENANT_PREFERENCE_DETAILS.getValue(),
                request().body().asJson(),
                orgRequest -> {
                    new TenantRequestValidator().validateUpdateTenantPreferenceDetailsRequest((Request) orgRequest);
                    return null;
                },
                getAllRequestHeaders(request()));
    }


    public F.Promise<Result> getTenantDetailsByHomeUrl() {
        ProjectLogger.log("getTenantDetails called.", LoggerEnum.DEBUG.name());

        Map<String,String> headers =getAllRequestHeaders(request());
        try {
            Request request = createAndInitRequest(CaminoActorOperations.GET_TENANT_INFO.getValue());

            String[] data=new TenantRequestValidator().validateGetTenantDetails(headers);
            request.put(data[0],data[1]);

            return actorResponseHandler(getActorRef(), request, timeout, null, request());
        } catch (Exception e) {
            ProjectLogger.log("getTenantDetails: exception = ", e);

            return F.Promise.pure(createCommonExceptionResponse(e, request()));
        }
    }


    // this function uses validateCreateTenantRequest() function for basic validation of request parameters as the request is same as the create
    public Promise<Result> addTenantPreferenceDetails() {
        ProjectLogger.log("addTenantPreferenceDetails called.", LoggerEnum.DEBUG.name());
        return handleRequest(
                CaminoActorOperations.ADD_TENANT_PREFERENCE_DETAILS.getValue(),
                request().body().asJson(),
                orgRequest -> {
                    new TenantRequestValidator().validateCreateTenantRequest((Request) orgRequest);
                    return null;
                },
                getAllRequestHeaders(request()));
    }

}
