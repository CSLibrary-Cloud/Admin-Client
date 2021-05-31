package com.cslibrary.admin.data

class ReportData(
    var reportUserId: String, // Who reported - as ID
    var reportContent: ReportRequest,
    var isReportHandlingDone: Boolean,
    var reportIdentifier: String
) {
    override fun toString(): String {
        return """
            |Report Ticket: $reportIdentifier
            |Report From: $reportUserId
            |Report Content: $reportContent
            |Report Handle Done: $isReportHandlingDone
        """.trimMargin()
    }
}

data class ReportRequest(
    var reportMessage: String
) {
    override fun toString(): String {
        return reportMessage
    }
}
