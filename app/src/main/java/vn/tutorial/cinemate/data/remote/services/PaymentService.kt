package vn.tutorial.cinemate.data.remote.services

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import vn.tutorial.cinemate.core.constant.api_endpoint.PaymentEndpoint
import vn.tutorial.cinemate.data.remote.requests.payment.AcceptInvitationRequest
import vn.tutorial.cinemate.data.remote.requests.payment.ChildrenModeRequest
import vn.tutorial.cinemate.data.remote.requests.payment.InviteMemberRequest
import vn.tutorial.cinemate.data.remote.requests.payment.PlanRequest
import vn.tutorial.cinemate.data.remote.responses.BaseResponse
import vn.tutorial.cinemate.data.remote.responses.payment.CreatePlanResponse
import vn.tutorial.cinemate.data.remote.responses.payment.CurrentPlanResponse
import vn.tutorial.cinemate.data.remote.responses.payment.MemberResponse
import vn.tutorial.cinemate.data.remote.responses.payment.PlanResponse

interface PaymentService {
    @GET(PaymentEndpoint.GET_ALL)
    suspend fun getAllSubscriptions(): Response<BaseResponse<List<PlanResponse>>>

    @POST(PaymentEndpoint.CREATE_SUBSCRIPTION)
    suspend fun createSubscription(
        @Body planRequest: PlanRequest
    ): Response<BaseResponse<CreatePlanResponse>>

    @GET(PaymentEndpoint.GET_SUBSCRIPTION)
    suspend fun getCurrentSubscription(): Response<BaseResponse<CurrentPlanResponse>>

    @GET(PaymentEndpoint.GET_BY_ID)
    suspend fun getSubscriptionById(
        @Path("planId") planId: String
    ): Response<BaseResponse<PlanResponse>>


    @GET(PaymentEndpoint.GET_MEMBERS)
    suspend fun getMembers(): Response<BaseResponse<List<MemberResponse>>>

    @POST(PaymentEndpoint.INVITE_MEMBER)
    suspend fun inviteMember(
        @Body inviteMemberRequest: InviteMemberRequest
    ): Response<BaseResponse<Unit>>

    @POST(PaymentEndpoint.ACCEPT_INVITATION)
    suspend fun acceptInvitation(
        @Body acceptInvitationRequest: AcceptInvitationRequest
    ): Response<BaseResponse<Unit>>



    @PUT(PaymentEndpoint.SET_CHILDREN_MODE)
    suspend fun setChildrenMode(
        @Query("kidId") kidId: String,
        @Body childrenModeRequest: ChildrenModeRequest
    ): Response<BaseResponse<Unit>>

}