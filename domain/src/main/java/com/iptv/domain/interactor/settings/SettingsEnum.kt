package com.iptv.domain.interactor.settings

enum class SettingsEnum(private val value: String) {
    PCODE("pcode"),
    HTTP_CACHING("http_caching"),
    STREAM_SERVER("stream_server"),
    TIMESHIFT("timeshift"),
    TIMEZONE("timezone");

    operator fun invoke() = value
}