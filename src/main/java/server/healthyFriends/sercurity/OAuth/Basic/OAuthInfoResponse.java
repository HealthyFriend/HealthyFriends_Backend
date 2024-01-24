package server.healthyFriends.sercurity.OAuth.Basic;

public interface OAuthInfoResponse {
    String getEmail();
    String getNickname();
    OAuthProvider getOAuthProvider();
}