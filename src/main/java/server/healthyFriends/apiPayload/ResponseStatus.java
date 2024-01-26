package server.healthyFriends.apiPayload;

public enum ResponseStatus {

    //Success
    OK, //200
    CREATED, //201
    NO_CONTENT, //204

    //ClientError
    BAD_REQUEST, //400
    UNAUTHORIZED, // 401
    FORBIDDEN, // 403
    NOT_FOUND, // 404
    CONFLICT, //409

    EXPECTATION_FAILED, //417
    METHOD_ARGUMENT_NOT_VALID,

    //ServerError
    INTERNAL_SERVER_ERROR, // 500
    SERVICE_UNAVAILABLE, //503

}
