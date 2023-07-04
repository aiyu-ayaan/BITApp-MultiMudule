package com.atech.core.utils

enum class TAGS{
    BIT_ERROR,
    BIT_DEBUG,
}
enum class RowSubjectAdapterRequest {
    FROM_ATTENDANCE,
    FROM_HOME
}

enum class SharePrefKeys {
    SharedPreferenceName,
    ChooseSemLastSelectedSem,
    KeyToggleSyllabusSource,
}

const val MAX_SPAWN = 3

const val UPDATE_REQUEST = "Update"
const val REQUEST_EDIT_SUBJECT_FROM_LIST_ALL = 674 * 453
const val REQUEST_ADD_SUBJECT_FROM_SYLLABUS = 345 * 453
const val REQUEST_MENU_FROM_ARCHIVE = 345 * 453 + 1
const val REQUEST_EDIT_SUBJECT_FROM_SYLLABUS = 347 * 453

const val DEFAULT_DATE_FORMAT = "dd-MM-yyyy"
const val DEFAULT_TIME_FORMAT = "hh:mm a"
const val DD_MM = "dd MMM"
const val EDIT_TEXT_DATE_FORMAT = "MMM dd, yyyy"
const val ERROR_IN_UPDATE = 452 * 53

const val MAX_STACK_SIZE = 30

enum class RemoteConfigKeys {
    SYLLABUS_VISIBILITY,
    KEY_TOGGLE_SYLLABUS_SOURCE_ARRAY,
    SYLLABUS_BCA,
    SYLLABUS_BBA,
    KEY_TOGGLE_SYLLABUS_SOURCE,
    title,
    minVersion,
    link,
    isEnable,
    button_text,
    show_times,
    Github_Link,
    Current_Year,
    ann_version,
    ann_pos_button,
    ann_neg_button,
    ann_message,
    ann_link,
    ann_title
}