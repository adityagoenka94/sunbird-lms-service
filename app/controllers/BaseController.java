package controllers;
import java.text.MessageFormat;

import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.response.Response;
import org.sunbird.common.models.response.ResponseParams;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.models.util.LogHelper;
import org.sunbird.common.models.util.ProjectUtil;
import org.sunbird.common.request.ExecutionContext;
import org.sunbird.common.responsecode.ResponseCode;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;
import play.mvc.Results;
import util.AuthenticationHelper;
import util.Global;
/**
 * This controller we can use for writing some
 * common method.
 * @author Manzarul
 */
public class BaseController extends Controller {
	private static final LogHelper logger = LogHelper.getInstance(BaseController.class.getName());
	public static final int Akka_wait_time =6;
	private static ActorSelection selection=null;
	static {
		ActorSystem system = ActorSystem.create("ActorApplication", ConfigFactory.load().getConfig("ActorConfig"));
		String path = "akka.tcp://RemoteMiddlewareSystem@127.0.0.1:8088/user/RequestRouterActor";
		try {
			path = play.Play.application().configuration().getString("remote.actor.path");
			if (!ProjectUtil.isStringNullOREmpty(System.getenv(JsonKey.SUNBIRD_ACTOR_IP))
					&& !ProjectUtil.isStringNullOREmpty(System.getenv(JsonKey.SUNBIRD_ACTOR_PORT))) {
				path = MessageFormat.format(play.Play.application().configuration().getString("remote.actor.env.path"),
						System.getenv(JsonKey.SUNBIRD_ACTOR_IP), System.getenv(JsonKey.SUNBIRD_ACTOR_PORT));
			}
		} catch (Exception e) {

		}

		selection = system.actorSelection(path);
	}
	
	/**
	 * This method will provide remote Actor selection
	 * @return
	 */
	public ActorSelection getRemoteActor() {
		return selection;
	}
	/**
	 * This method will create failure response
	 * @param request Request
	 * @param code ResponseCode
	 * @param headerCode ResponseCode
	 * @return Response
	 */
	public static Response createFailureResponse(Request request,ResponseCode code , ResponseCode headerCode) {
		Response response = new Response();
		response.setVer(getApiVersion(request.path()));
		response.setId(ExecutionContext.getRequestId());
		response.setTs(ProjectUtil.getFormattedDate());
		response.setResponseCode(headerCode);
		response.setParams(createResponseParamObj(code));
		return response;
	}
   
    /**
     * This method will create response param 
     * @param code ResponseCode
     * @return ResponseParams
     */
	public static ResponseParams createResponseParamObj(ResponseCode code) {
		ResponseParams params = new ResponseParams();
		if(code.getResponseCode()!=200) {
		params.setErr(code.getErrorCode());
		params.setErrmsg(code.getErrorMessage());
		}
		params.setMsgid(ExecutionContext.getRequestId());
		params.setStatus(ResponseCode.getHeaderResponseCode(code.getResponseCode()).name());
		return params;
	}
	
	/**
	 * This method will create data for success response.
	 * @param request String
	 * @param response Response
	 * @return Response
	 */
	public static Response createSuccessResponse(String request, Response response) {
		response.setVer(getApiVersion(request));
		response.setId(ExecutionContext.getRequestId());
		response.setTs(ProjectUtil.getFormattedDate());
		ResponseCode code = ResponseCode.getResponse(ResponseCode.success.getErrorCode());
		code.setResponseCode(ResponseCode.OK.getResponseCode());
		response.setParams(createResponseParamObj(code));
		return response;
	}
	
	/**
	 * This method will provide api version.
	 * @param request String
	 * @return String
	 */
  public static String getApiVersion(String request) {
	    return request.split("[/]")[1];
  }
  
  
  /**
   * This method will handle response in case of exception
   * @param request String
   * @param exception ProjectCommonException
   * @return Response
   */
	public static Response createResponseOnException(String request, ProjectCommonException exception) {
		Response response = new Response();
		response.setVer(getApiVersion(request));
		response.setId(ExecutionContext.getRequestId());
		response.setTs(ProjectUtil.getFormattedDate());
		response.setResponseCode(ResponseCode.getHeaderResponseCode(exception.getResponseCode()));
		ResponseCode code = ResponseCode.getResponse(exception.getCode());
		response.setParams(createResponseParamObj(code));
		return response;
	}
	
	
	
	/**
	 * This method will create common response for all controller method
	 * @param response Object
	 * @return Result
	 */
	public Result createCommonResponse(Object response,String key) {
		if (response instanceof Response) {
			Response courseResponse = (Response) response;
			if(!ProjectUtil.isStringNullOREmpty(key)){
				Object value = courseResponse.getResult().get(JsonKey.RESPONSE);
				courseResponse.getResult().remove(JsonKey.RESPONSE);
				courseResponse.getResult().put(key, value);
			}
		    return Results.ok(Json.toJson(BaseController.createSuccessResponse(request().path(), (Response) courseResponse)));
		} else {
			 ProjectCommonException exception = (ProjectCommonException) response;
			 return Results.ok(Json.toJson(BaseController.createResponseOnException(request().path(), exception)));
		}
	}
	
	/**
	 * Common exception response handler method.
	 * @param e Exception
	 * @return Result
	 */
	public Result createCommonExceptionResponse (Exception e) {
		logger.error(e);
		ProjectCommonException exception = null;
		if(e instanceof ProjectCommonException) {
			exception = (ProjectCommonException) e;
		}else {
		 exception = new ProjectCommonException(ResponseCode.internalError.getErrorCode(),
				ResponseCode.internalError.getErrorMessage(), ResponseCode.SERVER_ERROR.getResponseCode());
		}
		return Results.ok(Json.toJson(BaseController.createResponseOnException(request().path(), exception)));
	}
	
	/**
	 * This method will make a call to Akka actor and return promise.
	 * @param selection ActorSelection
	 * @param request Request
	 * @param timeout Timeout
	 * @param responseKey String
	 * @return Promise<Result>
	 */
	public Promise<Result> actorResponseHandler(ActorSelection selection, org.sunbird.common.request.Request request, Timeout timeout,String responseKey) {
		Promise<Result> res = Promise.wrap(Patterns.ask(selection, request, timeout))
				.map(new Function<Object, Result>() {
					public Result apply(Object result) {
						if (result instanceof Response) {
							Response response = (Response) result;
							return createCommonResponse(response, responseKey);
						} else if (result instanceof ProjectCommonException) {
							return createCommonExceptionResponse((ProjectCommonException) result);
						} else {
							logger.info("Unsupported Actor Response format");
							return createCommonExceptionResponse(new Exception());
						}
					}
				});
		return res;
	}

	/**
	 * This method will provide environment id.
	 * @return int
	 */
	public int getEnvironment() {
		if (Global.env != null) {
			return Global.env.getValue();
		}
		return ProjectUtil.Environment.prod.getValue();
	}
	
	public String getUserIdByAuthToken(String token){
		return AuthenticationHelper.verifyUserAccesToken(token);
		
	}
	
}