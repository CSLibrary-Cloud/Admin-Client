package com.cslibrary.admin.data

class ReportData(
    var reportUserId: String, // Who reported - as ID
    var reportContent: ReportRequest,
    var isReportHandlingDone: Boolean,
    var reportIdentifier: String
)

data class ReportRequest(
    var reportMessage: String
)
