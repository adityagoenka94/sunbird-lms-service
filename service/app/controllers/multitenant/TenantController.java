package controllers.multitenant;

import controllers.BaseController;
import org.sunbird.common.models.util.*;
import org.sunbird.common.request.Request;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Result;
import controllers.multitenant.validator.TenantRequestValidator;

import java.util.Map;


public class TenantController extends BaseController {

    public Promise<Result> createMultiTenantInfo() {
        ProjectLogger.log("createMultiTenantInfo called.", LoggerEnum.DEBUG.name());
        return handleRequest(
                CaminoActorOperations.CREATE_MULTI_TENANT_INFO.getValue(),
                request().body().asJson(),
                (request) -> {
                    new TenantRequestValidator().validateCreateMultiTenantInfoRequest((Request) request);
                    return null;
                },
                getAllRequestHeaders(request()));
    }

    public Promise<Result> updateMultiTenantInfo() {
        ProjectLogger.log("updateMultiTenantInfo called.", LoggerEnum.DEBUG.name());
        return handleRequest(
                CaminoActorOperations.UPDATE_MULTI_TENANT_INFO.getValue(),
                request().body().asJson(),
                orgRequest -> {
                    new TenantRequestValidator().validateUpdateMultiTenantInfoRequest((Request) orgRequest);
                    return null;
                },
                getAllRequestHeaders(request()));
    }


    public F.Promise<Result> getMultiTenantInfoByHomeUrlorOrgId() {
        ProjectLogger.log("getMultiTenantInfoByHomeUrlorOrgId called.", LoggerEnum.DEBUG.name());

        try {
            Request request = createAndInitRequest(CaminoActorOperations.GET_MULTI_TENANT_INFO.getValue());

            Map<String,String> headers = getAllRequestHeaders(request());
            String[] data=new TenantRequestValidator().validateGetMultiTenantInfoRequest(headers.get(JsonKey.HOME_URL),headers.get(JsonKey.ORGANISATION_ID));
            request.put(data[0],data[1]);

            return actorResponseHandler(getActorRef(), request, timeout, null, request());
        } catch (Exception e) {
            ProjectLogger.log("getTenantDetails: exception = ", e);

            return F.Promise.pure(createCommonExceptionResponse(e, request()));
        }
    }


    public Promise<Result> deleteMultiTenantInfoById(String multiTenantId) {
        ProjectLogger.log("deleteMultiTenantInfoById called.", LoggerEnum.DEBUG.name());
        return handleRequest(
                CaminoActorOperations.DELETE_MULTI_TENANT_INFO.getValue(),
                multiTenantId,
                CaminoJsonKey.MULTI_TENANT_ID,
                false);
    }

}
