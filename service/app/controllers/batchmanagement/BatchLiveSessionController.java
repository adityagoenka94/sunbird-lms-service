package controllers.batchmanagement;

import controllers.BaseController;
import controllers.batchmanagement.validator.BatchLiveSessionValidator;
import org.sunbird.common.models.util.*;
import org.sunbird.common.request.Request;
import play.libs.F;
import play.mvc.Result;


public class BatchLiveSessionController extends BaseController {

    public F.Promise<Result> createBatchLiveSessions() {
        ProjectLogger.log("createBatchLiveSessions called.", LoggerEnum.DEBUG.name());
        return handleRequest(
                CaminoActorOperations.CREATE_BATCH_LIVESESSIONS.getValue(),
                request().body().asJson(),
                (request) -> {
                    new BatchLiveSessionValidator().validateCreateBatchLiveSessionRequest((Request) request);
                    return null;
                },
                getAllRequestHeaders(request()));
    }

    public F.Promise<Result> updateBatchLiveSession() {
        ProjectLogger.log("updateBatchLiveSession called.", LoggerEnum.DEBUG.name());
        return handleRequest(
                CaminoActorOperations.UPDATE_BATCH_LIVESESSION.getValue(),
                request().body().asJson(),
                orgRequest -> {
                    new BatchLiveSessionValidator().validateUpdateBatchLiveSessionRequest((Request) orgRequest);
                    return null;
                },
                getAllRequestHeaders(request()));
    }


    public F.Promise<Result> getBatchLiveSessions(String batchId) {
        ProjectLogger.log("getBatchLiveSessions called.", LoggerEnum.DEBUG.name());

        return handleRequest(CaminoActorOperations.READ_BATCH_LIVESESSIONS.getValue(), batchId, JsonKey.BATCH_ID, false);
    }


    public F.Promise<Result> deleteBatchLiveSessions(String contentId) {
        ProjectLogger.log("deleteBatchLiveSessions called.", LoggerEnum.DEBUG.name());
        return handleRequest(
                CaminoActorOperations.DELETE_BATCH_LIVESESSION.getValue(),
                contentId,
                JsonKey.CONTENT_ID,
                false);
    }

}

