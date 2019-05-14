package controllers.multitenant;

import controllers.BaseController;
import org.sunbird.common.models.util.*;
import org.sunbird.common.request.Request;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Result;
import controllers.multitenant.validator.TenantRequestValidator;

public class TenantController extends BaseController {

    public Promise<Result> createTenant() {
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
        return handleRequest(
                CaminoActorOperations.UPDATE_TENANT_PREFERENCE_DETAILS.getValue(),
                request().body().asJson(),
                orgRequest -> {
                    new TenantRequestValidator().validateUpdateTenantPreferenceDetailsRequest((Request) orgRequest);
                    return null;
                },
                getAllRequestHeaders(request()));
    }


    public F.Promise<Result> getTenantDetailsByHomeUrl(String homeUrl) {
        return handleGetTenantDetails(CaminoActorOperations.GET_TENANT_INFO.getValue(), homeUrl);
    }

//    public F.Promise<Result> search() {
//        return handleSearchRequest(
//                ActorOperations.COMPOSITE_SEARCH.getValue(),
//                request().body().asJson(),
//                orgRequest -> {
//                    new BaseRequestValidator().validateSearchRequest((Request) orgRequest);
//                    return null;
//                },
//                null,
//                null,
//                getAllRequestHeaders(request()),
//                ProjectUtil.EsType.organisation.getTypeName());
//    }


    // this function uses validateCreateTenantRequest() function for basic validation of request parameters as the request is same as the create
    public Promise<Result> addTenantPreferenceDetails() {
        return handleRequest(
                CaminoActorOperations.ADD_TENANT_PREFERENCE_DETAILS.getValue(),
                request().body().asJson(),
                orgRequest -> {
                    new TenantRequestValidator().validateCreateTenantRequest((Request) orgRequest);
                    return null;
                },
                getAllRequestHeaders(request()));
    }

    private Promise<Result> handleGetTenantDetails(String operation, String homeUrl) {
        final String requestedFields = request().getQueryString(JsonKey.FIELDS);

        return handleRequest(
                operation,
                null,
                (req) -> {
                    Request request = (Request) req;
                    request.getContext().put(JsonKey.FIELDS, requestedFields);
                    return null;
                },
                homeUrl,
                JsonKey.HOME_URL,
                false);
    }
}
