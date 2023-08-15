package sa.med.imc.myimc.SYSQUO.Network;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.SYSQUO.Chat.chat.accesstoken.ChatAccessTokenRequetModel;
import sa.med.imc.myimc.SYSQUO.Chat.chat.accesstoken.ChatAccessTokenResponseModel;
import sa.med.imc.myimc.SYSQUO.EmergencyCall.Model.EmergencyCallRequestModel;
import sa.med.imc.myimc.SYSQUO.EmergencyCall.Model.EmergencyCallResponseModel;
import sa.med.imc.myimc.SYSQUO.EmergencyCall.Model.LeaveEmergencyCallRequestModel;
import sa.med.imc.myimc.SYSQUO.EmergencyCall.Model.LeaveEmergencyCallResponseModel;
import sa.med.imc.myimc.SYSQUO.Selection.Model.CreateRoomRequestModel;
import sa.med.imc.myimc.SYSQUO.Selection.Model.CreateRoomResponseModel;
import sa.med.imc.myimc.SYSQUO.Video.ExitRoom.Model.DisconnectRoomRequestModel;
import sa.med.imc.myimc.SYSQUO.Video.ExitRoom.Model.DisconnectRoomResponseModel;
import sa.med.imc.myimc.SYSQUO.Video.ExitRoom.Model.ExitRoomRequestModel;
import sa.med.imc.myimc.SYSQUO.Video.ExitRoom.Model.ExitRoomResponseModel;
import sa.med.imc.myimc.SYSQUO.Video.JoningRequest.ConferenceJoiningRequestMode;
import sa.med.imc.myimc.SYSQUO.Video.JoningRequest.ConferenceJoiningResponseMode;
import sa.med.imc.myimc.SYSQUO.Video.VideoToken.ServerTokenResponseModel;

public interface APIClass {

//    @FormUrlEncoded
//    @POST("HostApi/createRoom")
    @POST("api/twilio/create-room")
    Call<CreateRoomResponseModel> createRoom(@Header("Authorization") String token, @Header("Accept-Language") String lng, @Header("HospitalID") String HospitalID, @Body CreateRoomRequestModel createRoomRequestModel);

//    @FormUrlEncoded
//    @POST("HostApi/createRoom")
    @POST("api/twilio/conference-joining-request")
    Call<ConferenceJoiningResponseMode> conferenceJoiningReq(@Header("Authorization") String token
                                                             , @Header(Constants.HOSPITAL_CODE_FOR_HEADER) String HospitalID,
                                                             @Header("Accept-Language") String lng,
                                                             @Body ConferenceJoiningRequestMode conferenceJoiningRequestMode);

//    @FormUrlEncoded
    @POST("api/twilio/call-end")
    Call<ExitRoomResponseModel> exitRoomEmergency(@Header("Authorization") String token, @Header("Accept-Language") String lng, @Body ExitRoomRequestModel exitRoomRequestModel);

//    @FormUrlEncoded
    @POST("api/twilio/chat/get-access-token")
    Call<ChatAccessTokenResponseModel> chatAccessToken(@Header("Authorization") String token, @Header("Accept-Language") String lng, @Body ChatAccessTokenRequetModel chatAccessTokenRequetModel);

    @POST("api/twilio/disconnected-event-timer-start")
    Call<DisconnectRoomResponseModel> disconnectRoom(@Header("Authorization") String token, @Header("Accept-Language") String lng, @Body DisconnectRoomRequestModel disconnectRoomRequestModel);

    @POST("api/twilio/join-emergency-room")
    Call<EmergencyCallResponseModel> emergencyCall(@Header("Authorization") String token, @Header("Accept-Language") String lng, @Body EmergencyCallRequestModel  emergencyCallRequestModel);

    @POST("api/twilio/leave-emergency-room")
    Call<LeaveEmergencyCallResponseModel> leaveEmergencyCall(@Header("Authorization") String token, @Header("Accept-Language") String lng, @Body LeaveEmergencyCallRequestModel leaveEmergencyCallRequestModel);

    @FormUrlEncoded
    @POST("HostApi/getTwilioToken")
    Call<ServerTokenResponseModel> serverToken(@Field("identity") String identity, @Header("Accept-Language") String lng, @Field("roomName") String roomName);

    @FormUrlEncoded
    @POST("HostApi/deleteFromWaitingList")
    Call<CreateRoomResponseModel> exitRoom(@Field("identity") String identity);
}
