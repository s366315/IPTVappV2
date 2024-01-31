package com.iptv.data.response

import com.google.gson.annotations.SerializedName

data class SuccessOperationResponse(
    @SerializedName("message")  val message: MessageResponse
) : ModelResponse()
